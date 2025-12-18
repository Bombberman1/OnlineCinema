import { api } from "./axios";

export const getAllMovies = async () => {
    const response = await api.get("/movies");
    return response.data;
};
