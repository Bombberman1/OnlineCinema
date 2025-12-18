import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setCurrentUser, logout } from "../store/authSlice";
import { setProfile, clearProfile } from "../store/profileSlice";
import { getCurrentUser } from "../api/users";
import { getCurrentProfile } from "../api/profiles";

const AuthInitializer = ({ children }) => {
    const dispatch = useDispatch();
    const token = useSelector((state) => state.auth.token);

    useEffect(() => {
        const initAuth = async () => {
            if (!token) {
                dispatch(clearProfile());
                return;
            }

            try {
                const user = await getCurrentUser();
                dispatch(setCurrentUser(user));

                try {
                    const profile = await getCurrentProfile();
                    dispatch(setProfile(profile));
                } catch {
                    dispatch(clearProfile());
                }
            } catch {
                dispatch(logout());
                dispatch(clearProfile());
            }
        };

        initAuth();
    }, [dispatch, token]);

    return children;
};

export default AuthInitializer;
