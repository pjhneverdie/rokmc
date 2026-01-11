CREATE TABLE document (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    project_id INT NOT NULL,
    title VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_title CHECK (title <> ''),
    content TEXT,
    status VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_status CHECK (status <> ''),
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);