create or replace function update_timestamp()
    returns trigger as $$
begin
        NEW.updated_at = NOW();
return new;
end;
       $$ language plpgsql;

alter table accounts
    add column if not exists updated_at timestamp default now();

drop trigger if exists trg_employees_updated_at on accounts;

create trigger trg_employee_updated_at before
    update on accounts
    for each row
    execute function update_timestamp();