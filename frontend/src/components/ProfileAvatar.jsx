import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import defaultAvatar from "../assets/avatar-default.png";
import "../styles/profile.css";

const ProfileAvatar = () => {
    const profile = useSelector((state) => state.profile.profile);
    const navigate = useNavigate();

    const avatarUrl = profile?.avatarUrl || defaultAvatar;

    return (
        <div
            className="profile-avatar"
            onClick={() => navigate("/profile")}
            title="Profile"
        >
            <img src={avatarUrl} alt="Avatar" />
        </div>
    );
};

export default ProfileAvatar;
