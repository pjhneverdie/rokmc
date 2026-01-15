CREATE TABLE project (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    name VARCHAR(30) NOT NULL, -- + NOT BLANK 
    description VARCHAR(255),
    status VARCHAR(30) NOT NULL, -- Enumerated
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL

-- + start_date < end_date
);