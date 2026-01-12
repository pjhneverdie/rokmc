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

