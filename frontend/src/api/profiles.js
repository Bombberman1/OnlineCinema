import { api } from "./axios";

export const getCurrentProfile = async () => {
    const response = await api.get("/profiles/current");
    return response.data;
};

export const createProfile = async (data) => {
    const response = await api.post("/profiles", data);
    return response.data;
};

export const updateProfile = async (data) => {
    const response = await api.put("/profiles", data);
    return response.data;
};
