CREATE TABLE IF NOT EXISTS branch (
    branch_id SERIAL NOT NULL,
    branch_code VARCHAR(10) NOT NULL UNIQUE,
    branch_name VARCHAR(100) NOT NULL,
    ifsc_code VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255),
    phone VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_branch PRIMARY KEY (branch_id)
    );

INSERT INTO branch(branch_id,branch_code,branch_name,ifsc_code,address,phone) values(1,'001808','xandar Central Branch','NOVA001808','Nova Central xandar','1X23459');