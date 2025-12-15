USE online_cinema;

CREATE TABLE IF NOT EXISTS profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    username VARCHAR(100),
    avatar_url VARCHAR(255),
    birth_date DATE,
    country VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
