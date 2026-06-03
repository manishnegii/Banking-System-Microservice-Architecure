create or replace function update_timestamp()
    returns trigger as $$
       begin
        NEW.updated_at = NOW();
        return new;
       end;
       $$ language plpgsql;

alter table customers
    add column if not exists updated_at timestamp default now();

drop trigger if exists trg_employees_updated_at on customers;

create trigger trg_employee_updated_at before
    update on customers
    for each row
    execute function update_timestamp();