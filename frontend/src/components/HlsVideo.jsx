import { useEffect, useRef } from "react";
import Hls from "hls.js";
import { store } from "../store/store";

const HlsVideo = ({
    src,
    autoPlay = false,
    controls = true,
    muted = false,
    className = "",
}) => {
    const videoRef = useRef(null);
    const hlsRef = useRef(null);

    useEffect(() => {
        const video = videoRef.current;
        if (!video || !src) return;

        if (hlsRef.current) {
            hlsRef.current.destroy();
            hlsRef.current = null;
        }

        const token = store.getState().auth.token;

        if (Hls.isSupported()) {
            const hls = new Hls({
                xhrSetup: (xhr) => {
                    if (token) {
                        xhr.setRequestHeader(
                            "Authorization",
                            `Bearer ${token}`
                        );
                    }
                },
            });

            hls.loadSource(src);
            hls.attachMedia(video);
            hlsRef.current = hls;
        } else if (video.canPlayType("application/vnd.apple.mpegurl")) {
            video.src = src;
        }

        return () => {
            hlsRef.current?.destroy();
            hlsRef.current = null;
        };
    }, [src]);

    return (
        <video
            ref={videoRef}
            autoPlay={autoPlay}
            controls={controls}
            muted={muted}
            className={className}
            playsInline
        />
    );
};

export default HlsVideo;
