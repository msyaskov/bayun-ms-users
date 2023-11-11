CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(320) UNIQUE NOT NULL,
    nickname VARCHAR(63) UNIQUE NOT NULL,
    given_name VARCHAR(255) NOT NULL,
    family_name VARCHAR(255) NOT NULL,
    picture VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX users_email_idx ON users(email);
CREATE UNIQUE INDEX users_nickname_idx ON users(nickname);