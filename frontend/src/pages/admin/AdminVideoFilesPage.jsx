import { useEffect, useState } from "react";
import { getMovies, uploadVideoFile } from "../../api/admin";
import { useToast } from "../../components/ToastProvider";
import "../../styles/forms.css";

const AdminVideoFilesPage = () => {
    const { showToast } = useToast();

    const [movies, setMovies] = useState([]);
    const [movieId, setMovieId] = useState("");
    const [quality, setQuality] = useState("");
    const [file, setFile] = useState(null);

    useEffect(() => {
        Promise.resolve().then(async () => {
            try {
                const data = await getMovies();
                setMovies(Array.isArray(data) ? data : []);
            } catch {
                showToast("Failed to load movies", "error");
            }
        });
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await uploadVideoFile(file, {
                movieId: Number(movieId),
                quality: Number(quality),
            });

            showToast("Video uploaded", "success");

            setFile(null);
            setQuality("");
        } catch (err) {
            showToast(err.response?.data?.message || "Upload failed", "error");
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Upload Video</h2>

            <form className="form" onSubmit={handleSubmit}>
                <select value={movieId} onChange={(e) => setMovieId(e.target.value)} required>
                    <option value="">Select movie</option>
                    {movies.map((movie) => (
                        <option key={movie.id} value={movie.id}>
                            {movie.title}
                        </option>
                    ))}
                </select>

                <input placeholder="Quality (360 / 720 / 1080)" value={quality} onChange={(e) => setQuality(e.target.value)} required />

                <input type="file" accept="video/mp4" onChange={(e) => setFile(e.target.files[0])} required />

                <button type="submit">Upload Video</button>
            </form>
        </div>
    );
};

export default AdminVideoFilesPage;
