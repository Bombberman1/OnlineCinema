import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getVideoQualities } from "../api/videoFiles";
import { getMovieById } from "../api/movies";
import { getFavorites, addToFavorites } from "../api/favorites";
import HlsVideo from "../components/HlsVideo";
import defaultPoster from "../assets/film-default.jpg";
import "../styles/watch.css";
import "../styles/movies.css";

const WatchPage = () => {
    const { movieId } = useParams();

    const [movie, setMovie] = useState(null);
    const [qualities, setQualities] = useState([]);
    const [quality, setQuality] = useState(null);
    const [favoritesIds, setFavoritesIds] = useState(new Set());
    const [play, setPlay] = useState(false);

    useEffect(() => {
        const load = async () => {
            const m = await getMovieById(movieId);
            setMovie(m);

            const q = await getVideoQualities(movieId);
            const sorted = q.map(x => x.quality).sort((a, b) => b - a);
            setQualities(sorted);
            setQuality(sorted[0]);

            const favs = await getFavorites();
            setFavoritesIds(new Set(favs.map(f => f.movieId)));
        };

        load();
    }, [movieId]);

    if (!movie || !quality) return null;

    const isFavorite = favoritesIds.has(movie.id);

    const handleAddFavorite = async () => {
        await addToFavorites(movie.id);
        setFavoritesIds(prev => new Set([...prev, movie.id]));
    };

    return (
        <div className="watch-page">

            <div className="watch-header">
                <h1>{movie.title}</h1>

                <div className="watch-meta">
                    <span>📅 {movie.releaseYear}</span>
                    <span>🌍 {movie.country}</span>
                    <span>⭐ {movie.rating}</span>
                    <span>🎭 {movie.genres.join(", ")}</span>
                </div>
            </div>

            <div className="quality-selector top">
                {qualities.map(q => (
                    <button
                        key={q}
                        className={
                            q === quality
                                ? "quality-button active"
                                : "quality-button"
                        }
                        onClick={() => {
                            setQuality(q);
                            setPlay(false);
                        }}
                    >
                        {q}p
                    </button>
                ))}
            </div>

            <div className="video-wrapper">
                {!play ? (
                    <img
                        src={movie.posterUrl || defaultPoster}
                        className="video-poster"
                        onClick={() => setPlay(true)}
                        alt={movie.title}
                    />
                ) : (
                    <HlsVideo
                        src={`/api/video_files/${movieId}/watch?quality=${quality}`}
                        controls
                        autoPlay
                        className="video-player"
                    />
                )}
            </div>

            <div className="watch-actions">
                {!isFavorite ? (
                    <button
                        className="favorite-button"
                        onClick={handleAddFavorite}
                    >
                        Add to favorites
                    </button>
                ) : (
                    <div className="favorite-label">
                        In favorites
                    </div>
                )}
            </div>

            <div className="watch-description">
                <h3>Description</h3>
                <p>{movie.description}</p>
            </div>
        </div>
    );
};

export default WatchPage;
