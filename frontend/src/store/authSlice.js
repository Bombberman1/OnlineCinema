import { createSlice } from "@reduxjs/toolkit";

const tokenFromStorage = localStorage.getItem("token");

const authSlice = createSlice({
    name: "auth",
    initialState: {
        token: tokenFromStorage,
        isAuthenticated: !!tokenFromStorage,
        isAdmin: false,
        user: null,
    },
    reducers: {
        loginSuccess: (state, action) => {
            state.token = action.payload;
            state.isAuthenticated = true;
            localStorage.setItem("token", action.payload);
        },
        setCurrentUser: (state, action) => {
            state.user = action.payload;
            state.isAdmin = action.payload.admin === true;
        },
        logout: (state) => {
            state.token = null;
            state.isAuthenticated = false;
            state.isAdmin = false;
            state.user = null;
            localStorage.removeItem("token");
        },
    },
});

export const { loginSuccess, setCurrentUser, logout } = authSlice.actions;
export default authSlice.reducer;
