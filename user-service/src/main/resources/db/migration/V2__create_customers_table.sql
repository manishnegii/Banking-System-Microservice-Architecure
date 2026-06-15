CREATE TABLE customers(
    customer_id bigserial primary key,
    auth_id bigint not null unique,
    name varchar(255) not null,
    role varchar(10) not null,
    email varchar(255) not null unique,
    date_of_birth date,
    gender varchar(10) not null,
    mobile_number varchar(255) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE INDEX idx_customer_auth_id ON customers(auth_id);
