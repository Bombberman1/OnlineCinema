USE online_cinema;

CREATE TABLE video_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    movie_id BIGINT NOT NULL,
    quality INT NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);
