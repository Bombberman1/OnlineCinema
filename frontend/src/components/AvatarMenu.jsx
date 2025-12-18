import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "../store/authSlice";
import defaultAvatar from "../assets/avatar-default.png";
import "../styles/avatar-menu.css";

const AvatarMenu = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const profile = useSelector((state) => state.profile.profile);

    const [open, setOpen] = useState(false);
    const menuRef = useRef(null);

    const avatarUrl = profile?.avatarUrl || defaultAvatar;

    const handleLogout = () => {
        dispatch(logout());
        navigate("/login");
    };

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    return (
        <div className="avatar-menu" ref={menuRef}>
            <div
                className="avatar-button"
                onClick={() => setOpen((prev) => !prev)}
            >
                <img src={avatarUrl} alt="Avatar" />
            </div>

            {open && (
                <div className="avatar-dropdown">
                    <button onClick={() => navigate("/profile")}>
                        Profile
                    </button>

                    <button onClick={() => navigate("/change_password")}>
                        Change password
                    </button>

                    <div className="divider" />

                    <button className="logout" onClick={handleLogout}>
                        Logout
                    </button>
                </div>
            )}
        </div>
    );
};

export default AvatarMenu;
