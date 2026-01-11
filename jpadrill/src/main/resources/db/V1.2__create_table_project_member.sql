CREATE TABLE project_member (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    project_id INT NOT NULL,
    member_id INT NOT NULL,
    CONSTRAINT prevent_duplicated_project_member UNIQUE (project_id, member_id),
    role VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_role CHECK (role <> ''),
    joined_at DATETIME NOT NULL, -- @Auditing
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
);