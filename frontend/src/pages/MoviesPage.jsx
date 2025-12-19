import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMovies } from "../api/movies";
import { getFavorites, addToFavorites } from "../api/favorites";
import { useToast } from "../components/ToastProvider";
import "../styles/movies.css";
import defaultPoster from "../assets/film-default.jpg";

const MoviesPage = () => {
    const [movies, setMovies] = useState([]);
    const [favoritesIds, setFavoritesIds] = useState(new Set());

    const [search, setSearch] = useState("");
    const [genre, setGenre] = useState("");
    const [year, setYear] = useState("");
    const [country, setCountry] = useState("");

    const navigate = useNavigate();
    const { showToast } = useToast();

    useEffect(() => {
        const loadData = async () => {
            const moviesData = await getMovies();
            setMovies(moviesData);

            const favorites = await getFavorites();
            setFavoritesIds(
                new Set(favorites.map((f) => f.movieId))
            );
        };

        loadData();
    }, []);

    const handleAddFavorite = async (movieId) => {
        try {
            await addToFavorites(movieId);

            setFavoritesIds((prev) => {
                const next = new Set(prev);
                next.add(movieId);
                return next;
            });

            showToast("Added to favorites", "success");
        } catch (err) {
            showToast(
                err.response?.data?.message || "Failed to add favorite",
                "error"
            );
        }
    };

    const filteredMovies = movies.filter((movie) => {
        return (
            movie.title.toLowerCase().includes(search.toLowerCase()) &&
            (!genre || movie.genres.includes(genre)) &&
            (!year || movie.releaseYear === Number(year)) &&
            (!country || movie.country === country)
        );
    });

    return (
        <div className="movies-page">
            <h2>Movies</h2>

            <div className="filters">
                <input
                    placeholder="Search..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

                <input
                    placeholder="Year"
                    value={year}
                    onChange={(e) => setYear(e.target.value)}
                />

                <input
                    placeholder="Country"
                    value={country}
                    onChange={(e) => setCountry(e.target.value)}
                />

                <select
                    value={genre}
                    onChange={(e) => setGenre(e.target.value)}
                >
                    <option value="">All genres</option>
                    <option value="Horror">Horror</option>
                    <option value="Battle">Battle</option>
                    <option value="Cartoon">Cartoon</option>
                </select>
            </div>

            <div className="movies-grid">
                {filteredMovies.map((movie) => {
                    const isFavorite = favoritesIds.has(movie.id);

                    return (
                        <div key={movie.id} className="movie-card">
                            <img
                                src={movie.posterUrl || defaultPoster}
                                alt={movie.title}
                                onClick={() =>
                                    navigate(`/watch/${movie.id}`)
                                }
                                onError={(e) => {
                                    e.target.onerror = null;
                                    e.target.src = defaultPoster;
                                }}
                            />

                            <div className="movie-info">
                                <h3>{movie.title}</h3>
                                <span>{movie.releaseYear}</span>
                            </div>

                            {!isFavorite && (
                                <button
                                    className="favorite-button"
                                    onClick={() =>
                                        handleAddFavorite(movie.id)
                                    }
                                >
                                    Add to favorites
                                </button>
                            )}

                            {isFavorite && (
                                <div className="favorite-label">
                                    In favorites
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default MoviesPage;
