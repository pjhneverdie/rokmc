CREATE TABLE project (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    name VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL -- @Auditing
);
