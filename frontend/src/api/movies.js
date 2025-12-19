import { api } from "./axios";

export const getMovies = async () => {
    const response = await api.get("/movies");
    return response.data;
};

export const getMovieById = async (movieId) => {
    const res = await api.get(`/movies/${movieId}`);
    return res.data;
};

export const getRecommends = async () => {
    const response = await api.get("/movies/recommends");
    return response.data;
};
