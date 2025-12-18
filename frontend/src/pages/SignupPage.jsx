import { useState } from "react";
import { signup } from "../api/auth";
import { Link, useNavigate } from "react-router-dom";
import "../styles/auth.css";
import { getErrorMessage } from "../api/error.js";

const SignupPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isAdmin, setIsAdmin] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            await signup({
                email: email,
                password: password,
                admin: isAdmin,
            });

            setSuccess(true);

            setTimeout(() => {
                navigate("/login");
            }, 1500);
        } catch (err) {
            setError(getErrorMessage(err));
        }
    };

    return (
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2>Sign Up</h2>

                {error && <p className="error">{error}</p>}
                {success && <p className="success">Account created. Redirecting…</p>}

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

                <div className="toggle-container">
                    <label className="toggle-switch">
                        <input
                            type="checkbox"
                            checked={isAdmin}
                            onChange={(e) => setIsAdmin(e.target.checked)}
                        />
                        <span className="toggle-slider"></span>
                    </label>
                    <span>Register as admin</span>
                </div>

                <button type="submit">Create Account</button>

                <div className="auth-footer">
                    Already have an account? <Link to="/login">Login</Link>
                </div>
            </form>
        </div>
    );
};

export default SignupPage;
