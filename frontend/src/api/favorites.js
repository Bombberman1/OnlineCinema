import { api } from "./axios";

export const getFavorites = async () => {
    const response = await api.get("/favorites/current");
    return response.data;
};

export const addToFavorites = async (movieId) => {
    const response = await api.post(`/favorites/${movieId}`);
    return response.data;
};
