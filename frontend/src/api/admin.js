import { api } from "./axios";

export const getMovies = async () => {
    const response = await api.get("/movies");
    return response.data;
};

export const createMovie = async (payload) => {
    const response = await api.post("/movies", payload);
    return response.data;
};

export const deleteMovie = async (id) => {
    await api.delete(`/movies/${id}`);
};

export const uploadVideoFile = async (file, metadata) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append(
        "metadata",
        new Blob(
            [JSON.stringify(metadata)],
            { type: "application/json" }
        )
    );

    const response = await api.post(
        "/video_files/upload",
        formData
    );

    return response.data;
};

export const getVideoQualities = async (movieId) => {
    const response = await api.get(`/video_files/${movieId}/qualities`);
    return response.data;
};

// export const watchVideo = async (movieId, quality) => {
//     const response = await api.get(
//         `/video_files/${movieId}/watch`,
//         {
//             params: { quality: quality },
//         }
//     );

//     return response.data;
// };
