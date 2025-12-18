import axios from "axios";
import { store } from "../store/store";
import { logout } from "../store/authSlice";

export const api = axios.create({
    baseURL: "/api",
});

api.interceptors.request.use((config) => {
    const token = store.getState().auth.token;
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (
            error.response &&
            error.response.status === 403 &&
            error.response.data?.message === "Invalid token"
        ) {
            store.dispatch(logout());
            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
);
