    ├── common
    │   ├── entity
    │   │   └── BaseEntity.java
    │   ├── enumtype
    │   │   ├── BaseStatus.java
    │   │   └── RoleType.java
    │   └── config
    │       └── JpaConfig.java

    ├── member
    │   ├── entity
    │   │   ├── Member.java
    │   │   ├── Address.java
    │   │   └── NotificationSetting.java
    │   ├── repository
    │   │   └── MemberRepository.java
    │   └── enumtype
    │       └── MemberRole.java

    ├── project
    │   ├── entity
    │   │   ├── Project.java
    │   │   ├── ProjectMember.java
    │   │   └── Tag.java
    │   ├── repository
    │   │   └── ProjectRepository.java
    │   ├── enumtype
    │   │   ├── ProjectStatus.java
    │   │   └── ProjectRole.java

    ├── document
    │   ├── entity
    │   │   ├── Document.java
    │   │   └── DocumentDetail.java
    │   ├── repository
    │   │   └── DocumentRepository.java
    │   └── enumtype
    │       └── DocumentStatus.java

    ├── issue
    │   ├── entity
    │   │   ├── Issue.java
    │   │   ├── BugIssue.java
    │   │   └── TaskIssue.java
    │   ├── repository
    │   │   └── IssueRepository.java
    │   └── enumtype
    │       ├── IssuePriority.java
    │       └── IssueStatus.java

# 📌 도메인 시나리오: 기업용 프로젝트 관리 시스템 (PMS)

> Jira + Notion + Slack을 섞은 느낌의 기업용 프로젝트 관리 시스템  
>  
> 이 도메인 하나로 아래 JPA 매핑을 전부 연습한다.
>
> - `@ManyToMany` (순수 / 중간 엔티티 분리)
> - `@OneToOne` (주 테이블 vs 대상 테이블 FK)
> - `@MappedSuperclass`
> - `@Inheritance + @DiscriminatorColumn`
> - `@Embeddable / @Embedded`
> - 연관관계 주인 개념

---

## 1️⃣ 공통 엔티티 (MappedSuperclass)

### 🔹 BaseEntity

**요구사항**
- id
- createdAt
- updatedAt

**매핑 요구**
- `@MappedSuperclass` 사용
- 모든 엔티티는 BaseEntity를 상속받는다

---

## 2️⃣ 사용자 도메인

### 🔹 Member (엔티티)

**속성**
- id
- email (unique)
- name
- role (USER, MANAGER, ADMIN)
- address (값 타입)

---

### 🔹 Address (값 타입)

**속성**
- city
- street
- zipcode

**매핑 요구**
- `@Embeddable`
- Member에서 `@Embedded`로 사용

---

## 3️⃣ 프로젝트 도메인 (ManyToMany 핵심)

### 🔹 Project (엔티티)

**속성**
- id
- name
- description
- status (READY, IN_PROGRESS, DONE)
- startDate
- endDate

---

### 🔹 요구사항 1: 프로젝트 ↔ 멤버 (논리적 ManyToMany)

- 한 프로젝트에는 여러 멤버가 참여할 수 있다
- 한 멤버는 여러 프로젝트에 참여할 수 있다

➡️ **논리적으로 ManyToMany 관계**

---

### 🔹 요구사항 2: 참여 정보가 필요함 (실무 핵심)

프로젝트 참여 시 다음 정보를 관리해야 한다.

- 참여 역할 (DEVELOPER, DESIGNER, QA)
- 참여 시작일
- 주당 투입 시간 (hoursPerWeek)

➡️ **순수 `@ManyToMany` 사용 불가**

---

### 🔹 해결책: 중간 엔티티 분리

### 🔹 ProjectMember (연결 엔티티)

**속성**
- id
- project
- member
- projectRole
- joinedAt
- hoursPerWeek

**관계**
- Project 1 : N ProjectMember
- Member 1 : N ProjectMember

**연습 포인트**
- 왜 `@ManyToMany`를 직접 쓰면 안 되는지
- 연관관계의 주인은 ProjectMember
- 복합키 vs 대체키(id) 선택

---

## 4️⃣ 문서 도메인 (OneToOne 핵심)

### 🔹 Document (엔티티)

**속성**
- id
- title
- content
- project
- status (DRAFT, PUBLISHED)

---

### 🔹 DocumentDetail (엔티티)

**속성**
- id
- wordCount
- lastEditedAt
- version
- confidential (boolean)

---

### 🔹 요구사항

- 하나의 문서는 하나의 상세 정보만 가진다
- 상세 정보는 문서 없이는 존재할 수 없다
- 대부분의 조회는 Document 기준이다

**매핑 요구**
- `@OneToOne`
- FK는 DocumentDetail 테이블이 가진다
- LAZY 로딩 설정

**연습 포인트**
- `mappedBy` vs FK 주인 개념
- `@MapsId`를 사용해 PK를 공유하는 구조도 연습

---

## 5️⃣ 이슈 도메인 (Inheritance + Discriminator)

### 🔹 Issue (추상 엔티티)

**공통 속성**
- id
- title
- description
- priority
- status
- assignee (Member)

**매핑 요구**
- `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`
- `@DiscriminatorColumn(name = "issue_type")`

---

### 🔹 BugIssue

**추가 속성**
- severity (CRITICAL, MAJOR, MINOR)

---

### 🔹 TaskIssue

**추가 속성**
- dueDate

---

## 6️⃣ 알림 설정 (OneToOne 실전 케이스)

### 🔹 NotificationSetting (엔티티)

**속성**
- id
- emailEnabled
- slackEnabled
- pushEnabled

---

### 🔹 요구사항

- 멤버는 알림 설정을 하나만 가진다
- 알림 설정은 자주 조회되지 않는다
- 멤버가 삭제되면 알림 설정도 같이 삭제된다

**매핑 요구**
- Member 1 : 1 NotificationSetting
- Member가 연관관계의 주인
- `cascade = CascadeType.ALL`
- `orphanRemoval = true`
- LAZY 로딩 설정

---

## 7️⃣ ManyToMany “순수 버전” 연습용 (비교용)

### 🔹 Tag (엔티티)

**속성**
- id
- name

---

### 🔹 요구사항

- 프로젝트는 여러 태그를 가질 수 있다
- 태그는 여러 프로젝트에 속할 수 있다
- 추가 속성은 없다

**매핑 요구**
- 여기서만 `@ManyToMany` 직접 사용
- 조인 테이블 명시 (`@JoinTable`)

---

## 8️⃣ 미션 (실력 상승용 🔥)

### ✅ 필수

1. Project ↔ Member 관계를
   - (1) `@ManyToMany`로 먼저 구현
   - (2) ProjectMember 연결 엔티티로 리팩토링

2. Document ↔ DocumentDetail 관계를
   - FK 방식
   - `@MapsId` 방식  
   두 가지로 각각 구현

---

### 💣 심화

- OneToOne 관계에서 FK를 어느 테이블에 두는 것이 좋은지 이유 정리
- NotificationSetting이 Member에서 LAZY 로딩이 실제로 동작하는지 확인
- Project 삭제 시 ProjectMember 정리 전략 설계

> Jira + Notion + Slack을 섞은 느낌의 기업용 프로젝트 관리 시스템  
>  
> 이 도메인 하나로 아래 JPA 매핑을 전부 연습한다.
>
> - `@ManyToMany` (순수 / 중간 엔티티 분리)
> - `@OneToOne` (주 테이블 vs 대상 테이블 FK)
> - `@MappedSuperclass`
> - `@Inheritance + @DiscriminatorColumn`
> - `@Embeddable / @Embedded`
> - 연관관계 주인 개념

---