import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getFavorites } from "../api/favorites";
import "../styles/movies.css";

const FavoritesPage = () => {
    const [favorites, setFavorites] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        getFavorites().then(setFavorites);
    }, []);

    return (
        <div className="movies-page">
            <h2>My Favorites</h2>

            <div className="movies-grid">
                {favorites.map((movie) => (
                    <div
                        key={movie.movieId}
                        className="movie-card"
                        onClick={() =>
                            navigate(`/watch/${movie.movieId}`)
                        }
                    >
                        <img
                            src={movie.posterUrl}
                            alt={movie.title}
                        />

                        <div className="movie-info">
                            <h3>{movie.title}</h3>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default FavoritesPage;
