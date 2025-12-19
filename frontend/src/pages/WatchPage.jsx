import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getVideoQualities } from "../api/videoFiles";
import HlsVideo from "../components/HlsVideo";
import "../styles/watch.css";

const WatchPage = () => {
    const { movieId } = useParams();

    const [qualities, setQualities] = useState([]);
    const [quality, setQuality] = useState(null);

    useEffect(() => {
        getVideoQualities(movieId).then((data) => {
            const sorted = data
                .map((q) => q.quality)
                .sort((a, b) => b - a);

            setQualities(sorted);
            setQuality(sorted[0]);
        });
    }, [movieId]);

    if (!quality) return null;

    return (
        <div className="watch-page">
            <div className="quality-selector">
                {qualities.map((q) => (
                    <button
                        key={q}
                        className={
                            q === quality
                                ? "quality-button active"
                                : "quality-button"
                        }
                        onClick={() => setQuality(q)}
                    >
                        {q}p
                    </button>
                ))}
            </div>

            <HlsVideo
                src={`/api/video_files/${movieId}/watch?quality=${quality}`}
                autoPlay
                controls
                className="video-player"
            />
        </div>
    );
};

export default WatchPage;
