import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { signup } from "../api/auth";
import { useToast } from "../components/ToastProvider";
import "../styles/forms.css";
import "../styles/toggle.css";

const SignupPage = () => {
    const navigate = useNavigate();
    const { showToast } = useToast();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isAdmin, setIsAdmin] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await signup({
                email: email,
                password: password,
                admin: isAdmin,
            });

            showToast("Account created", "success");
            navigate("/login");
        } catch (err) {
            showToast(err.response?.data?.message || "Signup failed", "error");
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Sign Up</h2>

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

                <div className="toggle-row">
                    <span className="toggle-label">Admin account</span>

                    <label className="toggle">
                        <input
                            type="checkbox"
                            checked={isAdmin}
                            onChange={(e) => setIsAdmin(e.target.checked)}
                        />
                        <span className="toggle-slider"></span>
                    </label>
                </div>

                <button type="submit">Create Account</button>
            </form>

            <p style={{ textAlign: "center", marginTop: "16px", color: "#aaa" }}>
                Already have an account? <Link to="/login">Login</Link>
            </p>
        </div>
    );
};

export default SignupPage;
