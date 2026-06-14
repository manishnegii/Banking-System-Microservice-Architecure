create table account_operations(
    id serial not null,
    txn varchar(20) not null unique,
    operation_type varchar(10) not null,
    status varchar(10) not null,
    amount decimal(19,2) not null,
    created_at timestamp not null default current_timestamp
)
