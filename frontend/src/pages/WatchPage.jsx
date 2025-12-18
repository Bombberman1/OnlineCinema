import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { getVideoQualities, watchVideo } from "../api/videoFiles";
import "../styles/watch.css";

const WatchPage = () => {
    const { movieId } = useParams();
    const videoRef = useRef(null);

    const [qualities, setQualities] = useState([]);
    const [selectedQuality, setSelectedQuality] = useState(null);
    const [videoUrl, setVideoUrl] = useState(null);

    useEffect(() => {
        getVideoQualities(movieId).then((data) => {
            const sorted = data
                .map((q) => q.quality)
                .sort((a, b) => b - a);

            setQualities(sorted);
            setSelectedQuality(sorted[0]);
        });
    }, [movieId]);

    useEffect(() => {
        if (!selectedQuality) {
            return;
        }

        const loadVideo = async () => {
            if (videoUrl) {
                URL.revokeObjectURL(videoUrl);
            }

            const blob = await watchVideo(movieId, selectedQuality);
            const url = URL.createObjectURL(blob);

            setVideoUrl(url);

            requestAnimationFrame(() => {
                if (videoRef.current) {
                    videoRef.current.load();
                    videoRef.current.play();
                }
            });
        };

        loadVideo();

        return () => {
            if (videoUrl) {
                URL.revokeObjectURL(videoUrl);
            }
        };
    }, [movieId, selectedQuality]);

    return (
        <div className="watch-page">
            <div className="quality-selector">
                {qualities.map((q) => (
                    <button
                        key={q}
                        className={
                            q === selectedQuality
                                ? "quality-button active"
                                : "quality-button"
                        }
                        onClick={() => setSelectedQuality(q)}
                    >
                        {q}p
                    </button>
                ))}
            </div>

            {videoUrl && (
                <video
                    ref={videoRef}
                    controls
                    autoPlay
                    className="video-player"
                >
                    <source src={videoUrl} type="video/mp4" />
                </video>
            )}
        </div>
    );
};

export default WatchPage;
