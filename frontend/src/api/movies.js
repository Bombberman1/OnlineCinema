import { api } from "./axios";

export const getMovies = async () => {
    const response = await api.get("/movies");
    return response.data;
};
