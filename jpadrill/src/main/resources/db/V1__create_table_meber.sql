
CREATE TABLE member (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY
    email VARCHAR(30) UNIQUE NOT NULL,
    name VARCHAR(30) NOT NULL,
    role VARCHAR(30) NOT NULL,
    city VARCHAR(30) NOT NULL, -- @Embeddable
    street VARCHAR(30) NOT NULL, -- @Embeddable
    zipcode VARCHAR(30) NOT NULL, -- @Embeddable
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL -- @Auditing
);