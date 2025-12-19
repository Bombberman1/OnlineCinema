import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getRecommends } from "../api/movies";
import HlsVideo from "../components/HlsVideo";
import "../styles/movies.css";
import "../styles/video.css";
import defaultPoster from "../assets/film-default.jpg";

const HomePage = () => {
    const [recommended, setRecommended] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadRecommends = async () => {
            const data = await getRecommends();
            setRecommended(data);
        };

        loadRecommends();
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
                {recommended.map((movie) => (
                    <div
                        key={movie.id}
                        className="movie-card"
                        onClick={() => navigate(`/watch/${movie.id}`)}
                    >
                        <HlsVideo
                            src={`/api/video_files/${movie.id}/watch?quality=360`}
                            poster={movie.posterUrl || defaultPoster}
                            muted
                            autoPlay
                            loop
                            controls={false}
                            className="preview-video"
                        />

                        <div className="movie-info">
                            <h3>{movie.title}</h3>
                            <span>{movie.releaseYear}</span>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default HomePage;
