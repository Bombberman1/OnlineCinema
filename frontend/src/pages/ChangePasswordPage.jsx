import { useState } from "react";
import { changePassword } from "../api/auth";
import { useToast } from "../components/ToastProvider";
import "../styles/auth.css";

const ChangePasswordPage = () => {
    const { showToast } = useToast();

    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await changePassword({
                oldPassword: oldPassword,
                newPassword: newPassword,
            });

            showToast("Password changed successfully", "success");

            setOldPassword("");
            setNewPassword("");
        } catch (err) {
            showToast(
                err.response?.data?.message || "Failed to change password",
                "error"
            );
        }
    };

    return (
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2>Change Password</h2>

                <input
                    type="password"
                    placeholder="Old password"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="New password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                />

                <button type="submit">Change Password</button>
            </form>
        </div>
    );
};

export default ChangePasswordPage;
