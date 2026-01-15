CREATE TABLE project_member (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    project_id INT NOT NULL,
    member_id INT NOT NULL,
    role VARCHAR(30) NOT NULL, -- + NOT BLANK 
    joined_at DATETIME NOT NULL, -- @Auditing
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
);