# @Builder에 관하여

## 세터 vs 생성자
세터는 값을 까먹을 수도 있는 위험이 있음. 근데 생성자는 값을 강제하니까 훨씬 안전함.

## 생성자 vs 빌더
생성자가 Setter보다 안전하긴 하지만, 필드 개수가 많아질수록 한계가 옴. 반면 Builder는 가독성이나 인자 순서로 부터 자유로움.

## 클래스 레벨 빌더 vs 생성자 레벨 빌더
클래스 레벨에 빌더 쓰면 모든 필드가 다 적용됨. 무조건 생성자 레벨이 나은 구조.

## NoArgsConstructor(access = AccessLevel.PROTECTED) + @Builder(access = AccessLevel.PRIVATE) + protected Constructor 조합

``` java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Document extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentStatus documentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder(access = AccessLevel.PRIVATE)
    protected Document(String title, String content, DocumentStatus documentStatus, Project project) {
        this.title = title;
        this.content = content;
        this.documentStatus = documentStatus;
        this.project = project;
    }

    public static Document createDocument(String title, String content, Project project) {
        return Document.builder()
                .title(title)
                .content(content)
                .documentStatus(DocumentStatus.DRAFT)
                .project(project)
                .build();
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
```
이렇게 했을 때 두 가지 장점이 있음.

1. 생성자 레벨 @Builder로 필요한 값들로만 객체 생성

2. protected 걸어서 외부에서 무조건 Builder 쓰게 일관성 유지

이 구조에서 다 좋은데 빌더 패턴도 세터처럼 값 넣는 걸 까먹으면 큰일나는 이슈가 있음.

여기서 당장 선택할 수 있는 옵션이 생성자에 assert로 값을 검증하는 건데 이건 직접 돌려야 예외가 터짐.

근데 @Builder(access = AccessLevel.PRIVATE) 달고 객체 내부에 생성 메서드 쓰면 코드 작성 과정에서 부터 빨간줄이 떠버리니까 더 편함.

그래서 최종적으로 NoArgsConstructor(access = AccessLevel.PROTECTED) + @Builder(access = AccessLevel.PRIVATE) + protected Constructor 조합이 꽤 괜찮은 선택임.
