CREATE TABLE IF NOT EXISTS accounts (
    account_id BIGSERIAL NOT NULL,
    user_id BIGINT NOT NULL,
    branch_id serial NOT NULL,
    account_number VARCHAR(64) NOT NULL,
    account_type VARCHAR(32) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_accounts PRIMARY KEY (account_id),
    CONSTRAINT uk_accounts_account_number UNIQUE (account_number),
    CONSTRAINT fk_accounts_branch FOREIGN KEY (branch_id) REFERENCES branch(branch_id) ON DELETE CASCADE
    );

CREATE INDEX idx_accounts_user_id ON accounts (user_id);
CREATE INDEX idx_accounts_account_type ON accounts (account_type);
CREATE INDEX idx_accounts_status ON accounts (status);
CREATE INDEX idx_accounts_user_status ON accounts (user_id, status);

