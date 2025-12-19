import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, Link } from "react-router-dom";
import { login } from "../api/auth";
import { loginSuccess } from "../store/authSlice";
import { useToast } from "../components/ToastProvider";
import "../styles/forms.css";

const LoginPage = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { showToast } = useToast();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const data = await login({ email, password });
            dispatch(loginSuccess(data.token));
            navigate("/");
        } catch (err) {
            showToast(err.response?.data?.message || "Login failed", "error");
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Login</h2>

            <form className="form" onSubmit={handleSubmit}>
                <input
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
            </form>

            <p style={{ textAlign: "center", marginTop: "16px", color: "#aaa" }}>
                No account? <Link to="/signup">Sign up</Link>
            </p>
        </div>
    );
};

export default LoginPage;
