#!/usr/bin/bash

files=(\
01_insert_movies.sql \
02_insert_genres.sql \
03_insert_movie_genres.sql \
04_insert_video_files.sql \
)

for file in "${files[@]}"; do
    docker exec -i mysql mysql -u cinema -pcinema online_cinema < "database/init/$file"
done
