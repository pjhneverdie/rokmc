CREATE TABLE document (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    project_id INT NOT NULL,
    title VARCHAR(30) NOT NULL, -- + NOT BLANK 
    content TEXT,
    status VARCHAR(30) NOT NULL, -- Enumerated
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL, -- @Auditing
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);