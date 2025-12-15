USE online_cinema;

CREATE TABLE movies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    release_year INT NOT NULL,
    country VARCHAR(100) NOT NULL,
    rating DECIMAL(3,1),
    poster_url VARCHAR(255) NOT NULL
);
