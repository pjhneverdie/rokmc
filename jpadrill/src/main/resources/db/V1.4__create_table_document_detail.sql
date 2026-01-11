CREATE TABLE document_detail (
    id INT PRIMARY KEY AUTO_INCREMENT, -- GenerationType.IDENTITY, MapsId
    word_count INT NOT NULL,
    last_edited_by VARCHAR(30) NOT NULL, -- @Auditing
    CONSTRAINT prevent_blank_last_edited_by CHECK (last_edited_by <> ''),
    version INT NOT NULL,
    confidential BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL, -- @Auditing
    updated_at DATETIME NOT NULL -- @Auditing
);