CREATE TABLE kyc_documents (
    document_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    document_number VARCHAR(100) NOT NULL UNIQUE,
    document_hash VARCHAR(64) NOT NULL UNIQUE ,
    key_version VARCHAR(20) NOT NULL,
    document_url VARCHAR(500),
    verification_status VARCHAR(30) DEFAULT 'PENDING',
    verified_at TIMESTAMP NULL,
    expiry_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_kyc_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

CREATE INDEX idx_kyc_customer_id ON kyc_documents(customer_id);
CREATE INDEX idx_kyc_document_number ON kyc_documents(document_number);
CREATE INDEX idx_kyc_document_type ON kyc_documents(document_type);
CREATE INDEX idx_kyc_document_hash ON kyc_documents(document_hash);
CREATE INDEX idx_kyc_status ON kyc_documents(verification_status);