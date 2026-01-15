CREATE TABLE member (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    email VARCHAR(30) UNIQUE NOT NULL, -- + NOT BLANK + REGEX
    name VARCHAR(30) NOT NULL, -- + NOT BLANK 
    role VARCHAR(30) NOT NULL, -- Enumerated
    city VARCHAR(30) NOT NULL, -- + NOT BLANK 
    street VARCHAR(30) NOT NULL, -- + NOT BLANK 
    zipcode VARCHAR(30) NOT NULL, -- + NOT BLANK 
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL -- @Auditing
);