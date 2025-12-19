import { api } from "./axios";

export const login = async (credentials) => {
    const response = await api.post("/auth/login", credentials);
    return response.data;
};

export const signup = async (credentials) => {
    const response = await api.post("/auth/signup", credentials);
    return response.data;
};

export const changePassword = async (payload) => {
    await api.put("/auth/change_password", payload);
};
