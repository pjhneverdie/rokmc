CREATE TABLE member (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    email VARCHAR(30) UNIQUE NOT NULL,
    CONSTRAINT prevent_blank_email CHECK (email <> ''),
    CONSTRAINT prevent_not_email_format CHECK (
        email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
    ),
    name VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_name CHECK (name <> ''),
    role VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_role CHECK (role <> ''),
    city VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_city CHECK (city <> ''),
    street VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_street CHECK (street <> ''),
    zipcode VARCHAR(30) NOT NULL,
    CONSTRAINT prevent_blank_zipcode CHECK (zipcode <> ''),
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL -- @Auditing
);