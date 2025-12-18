import { api } from "./axios";

export const getMovies = async () => {
    const response = await api.get("/movies");
    return response.data;
};

export const getRecommends = async () => {
    const response = await api.get("/movies/recommends");
    return response.data;
};
