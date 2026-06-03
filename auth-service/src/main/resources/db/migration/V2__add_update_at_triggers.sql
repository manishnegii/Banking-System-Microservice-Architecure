create or replace function update_timestamp()
    returns trigger as $$
       begin
        NEW.updated_at = NOW();
        return new;
       end;
       $$ language plpgsql;

alter table auth_users
add column if not exists updated_at timestamp default now();

drop trigger if exists trg_employees_updated_at on auth_users;

create trigger trg_employee_updated_at before
    update on auth_users
    for each row
    execute function update_timestamp();
