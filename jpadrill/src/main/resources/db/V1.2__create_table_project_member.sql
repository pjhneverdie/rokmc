CREATE TABLE project_member (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    project_id INT NOT NULL,
    member_id INT NOT NULL,
    project_role VARCHAR(30) NOT NULL,
    joined_at DATETIME NOT NULL, -- @Auditing
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT prevent_duplicated_project_member UNIQUE (project_id, member_id) -- 같은 프로젝트에 같은 멤버가 중복으로 들어가는 것을 방지
);