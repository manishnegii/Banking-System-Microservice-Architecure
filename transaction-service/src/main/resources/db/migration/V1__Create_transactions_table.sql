-- V1__Create_transactions_table.sql
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    merchant_id VARCHAR(255),
    upi_id VARCHAR(100),
    gateway_txn_id VARCHAR(255) UNIQUE,
    status VARCHAR(50) NOT NULL,
    idempotency_key VARCHAR(255) UNIQUE,
    failure_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);