drop table if exists ideas;
drop table if exists users;

-- table utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER'
);

Create table if not exists ideas (
    id bigint generated always as identity primary key,
    user_id bigint not null,
    title varchar(255) not null,
    description text default '',
    duration interval default '00:00:00',

    constraint fk_user_id foreign key (user_id) references users(id) on delete cascade
);

-- données de test
insert into users(email, password_hash) values('user@test.com', '$2a$10$fbnKhjS88bBfDKe2648sH.nLpNI30jC3F/Rv1f8GmfdEDoKMF2qrC'); -- password : password
insert into users(email, password_hash) values('admin@test.com', '$2a$10$fbnKhjS88bBfDKe2648sH.nLpNI30jC3F/Rv1f8GmfdEDoKMF2qrC');

update users set role = 'ROLE_ADMIN' where email = 'admin@test.com';

insert into ideas(user_id, title, description, duration) values(1, 'first idea', 'first idea''s description', '00:15:00');
insert into ideas(user_id, title, description, duration) values(2, 'second idea', 'second idea''s description', '01:00:00');
