create table addresses(
    address_id bigserial primary key,
    customer_id bigint not null,
    address_type varchar(20),
    address_line1 varchar(255) not null,
    address_line2 varchar(255) not null,
    city varchar(100) not null,
    state varchar(100) not null,
    postal_code varchar(20) not null,
    country varchar(100) not null,
    is_primary boolean default false,
    created_at timestamp default current_timestamp,
    constraint fk_address_customer foreign key (customer_id) references customers(customer_id) on delete cascade
);

CREATE INDEX idx_address_customer_id ON addresses(customer_id);