import { api } from "./axios";

export const getSubscription = async () => {
    const res = await api.get("/subscription");
    return res.data;
};

export const createSubscription = async (payload) => {
    await api.post("/subscription", payload);
};