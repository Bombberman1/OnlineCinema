import { useEffect, useState } from "react";
import { getMovies, createMovie } from "../../api/admin";
import { useToast } from "../../components/ToastProvider";
import "../../styles/forms.css";

const AdminMoviesPage = () => {
    const { showToast } = useToast();

    const [movies, setMovies] = useState([]);

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [releaseYear, setReleaseYear] = useState("");
    const [country, setCountry] = useState("");
    const [rating, setRating] = useState("");
    const [posterUrl, setPosterUrl] = useState("");
    const [genres, setGenres] = useState("");

    const loadMovies = async () => {
        try {
            const data = await getMovies();
            setMovies(Array.isArray(data) ? data : []);
        } catch {
            showToast("Failed to load movies", "error");
        }
    };

    useEffect(() => {
        Promise.resolve().then(loadMovies);
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await createMovie({
                title: title,
                description: description,
                releaseYear: Number(releaseYear),
                country: country,
                rating: Number(rating),
                posterUrl: posterUrl,
                genres: genres.split(",").map((g) => g.trim()),
            });

            showToast("Movie created", "success");

            setTitle("");
            setDescription("");
            setReleaseYear("");
            setCountry("");
            setRating("");
            setPosterUrl("");
            setGenres("");

            loadMovies();
        } catch (err) {
            showToast(err.response?.data?.message || "Create failed", "error");
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Add Movie</h2>

            <form className="form" onSubmit={handleSubmit}>
                <input placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} required />
                <textarea placeholder="Description" value={description} onChange={(e) => setDescription(e.target.value)} />
                <input type="number" placeholder="Release year" value={releaseYear} onChange={(e) => setReleaseYear(e.target.value)} required />
                <input placeholder="Country" value={country} onChange={(e) => setCountry(e.target.value)} required />
                <input type="number" step="0.01" placeholder="Rating" value={rating} onChange={(e) => setRating(e.target.value)} />
                <input placeholder="Poster URL" value={posterUrl} onChange={(e) => setPosterUrl(e.target.value)} />
                <input placeholder="Genres (comma separated)" value={genres} onChange={(e) => setGenres(e.target.value)} />

                <button type="submit">Create Movie</button>
            </form>

            <ul style={{ marginTop: "24px", color: "white" }}>
                {movies.map((movie) => (
                    <li key={movie.id}>
                        {movie.title} ({movie.releaseYear})
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AdminMoviesPage;
