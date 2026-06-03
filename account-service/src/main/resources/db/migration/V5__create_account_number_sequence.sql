create table if not exists account_number_sequence (
    id bigserial primary key,
    branch_code varchar(3) not null,
    account_type varchar(50) not null,
    next_value bigint not null,
    max_value bigint not null,
    active boolean default true,
    version bigint default 0,
    constraint uq_branch_account_type unique(branch_code,account_type)
);