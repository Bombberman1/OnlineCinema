import { api } from "./axios";

export const addWatchHistory = async (movieId) => {
    await api.post(`/watch_history/${movieId}`);
};

export const getWatchHistory = async () => {
    const response = await api.get("/watch_history");
    return response.data;
};
