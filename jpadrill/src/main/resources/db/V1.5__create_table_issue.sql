CREATE TABLE issue (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    issue_type VARCHAR(30) NOT NULL, -- DiscriminatorColumn
    CONSTRAINT prevent_blank_issue_type CHECK (issue_type <> ''),
    title VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_title CHECK (title <> ''),
    description TEXT,
    status VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_status CHECK (status <> ''),
    assignee_id INT NOT NULL,
    due_date DATETIME NOT NULL,
    severity VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_severity CHECK (severity <> ''),
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (assignee_id) REFERENCES member (id) ON DELETE SET NULL
);