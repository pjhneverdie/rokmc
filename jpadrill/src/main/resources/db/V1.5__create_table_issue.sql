CREATE TABLE issue (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    issue_type VARCHAR(30) NOT NULL, -- DiscriminatorColumn
    title VARCHAR(30) NOT NULL,
    description TEXT,
    priority INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    assignee_id INT,
    due_date DATETIME,
    severity VARCHAR(30),
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (assignee_id) REFERENCES member (id) ON DELETE SET NULL
);