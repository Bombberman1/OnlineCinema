import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getRecommends } from "../api/movies";
import "../styles/movies.css";
import defaultPoster from "../assets/film-default.jpg";

const HomePage = () => {
    const [recommended, setRecommended] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        getRecommends().then(setRecommended);
    }, []);

    if (recommended.length === 0) {
        return (
            <div className="movies-page">
                <h2>No recommendations yet</h2>
            </div>
        );
    }

    return (
        <div className="movies-page">
            <h2>Recommended for you</h2>

            <div className="movies-grid">
                {recommended.map(movie => (
                    <div
                        key={movie.id}
                        className="movie-card"
                        onClick={() => navigate(`/watch/${movie.id}`)}
                    >
                        <img
                            src={movie.posterUrl || defaultPoster}
                            alt={movie.title}
                            onError={e => {
                                e.target.onerror = null;
                                e.target.src = defaultPoster;
                            }}
                        />

                        <div className="movie-info">
                            <h3>{movie.title}</h3>
                            <span>{movie.releaseYear}</span>
                            <div className="movie-rating">
                                ⭐ {movie.rating}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default HomePage;
