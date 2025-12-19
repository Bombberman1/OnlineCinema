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
        const status = error.response?.status;
        const message = error.response?.data?.message;

        if (
            status === 401 ||
            (status === 403 &&
                (message === "Invalid token" ||
                 message === "JWT Token Expired"))
        ) {
            store.dispatch(logout());
            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
);
