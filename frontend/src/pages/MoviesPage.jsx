import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMovies } from "../api/movies";
import "../styles/movies.css";

const MoviesPage = () => {
    const [movies, setMovies] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        getMovies().then(setMovies);
    }, []);

    return (
        <div className="movies-page">
            <h2>Movies</h2>

            <div className="movies-grid">
                {movies.map((movie) => (
                    <div
                        key={movie.id}
                        className="movie-card"
                        onClick={() => navigate(`/watch/${movie.id}`)}
                    >
                        <img src={movie.posterUrl} alt={movie.title} />
                        <h3>{movie.title}</h3>
                        <p>{movie.releaseYear}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MoviesPage;
