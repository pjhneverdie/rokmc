CREATE TABLE project (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_name CHECK (name <> ''),
    description VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_status CHECK (status <> ''),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    CONSTRAINT prevent_impossible_date_case CHECK (start_date < end_date),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);