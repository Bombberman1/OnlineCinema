import { useState } from "react";
import { useDispatch } from "react-redux";
import { loginSuccess, setCurrentUser } from "../store/authSlice";
import { login } from "../api/auth";
import { getCurrentUser } from "../api/users";
import { Link, useNavigate } from "react-router-dom";
import "../styles/auth.css";
import { getErrorMessage } from "../api/error.js";

const LoginPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const data = await login({ email, password });
            dispatch(loginSuccess(data.token));

            const user = await getCurrentUser();
            dispatch(setCurrentUser(user));

            navigate("/");
        } catch (err) {
            setError(getErrorMessage(err));
        }
    };

    return (
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2>Login</h2>

                {error && <p className="error">{error}</p>}

                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button type="submit">Login</button>

                <div className="auth-footer">
                    Don’t have an account? <Link to="/signup">Sign up</Link>
                </div>
            </form>
        </div>
    );
};

export default LoginPage;
