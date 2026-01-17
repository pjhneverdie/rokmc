CREATE TABLE issue (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    issue_type VARCHAR(30) NOT NULL, -- DiscriminatorColumn
    title VARCHAR(30) NOT NULL, -- + NOT BLANK
    description TEXT,
    status VARCHAR(30) NOT NULL, -- Enumerated
    member_id INT,
    due_date DATETIME NOT NULL,
    severity VARCHAR(30) NOT NULL, -- Enumerated
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE SET NULL
);