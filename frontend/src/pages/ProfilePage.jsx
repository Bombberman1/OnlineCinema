import { useDispatch, useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { createProfile, updateProfile, getCurrentProfile } from "../api/profiles";
import { setProfile } from "../store/profileSlice";
import { getErrorMessage } from "../api/error";
import { useToast } from "../components/ToastProvider";
import defaultAvatar from "../assets/avatar-default.png";
import "../styles/profile.css";

const ProfilePage = () => {
    const dispatch = useDispatch();
    const { showToast } = useToast();
    const existingProfile = useSelector((state) => state.profile.profile);

    const [username, setUsername] = useState("");
    const [avatarUrl, setAvatarUrl] = useState("");
    const [birthDate, setBirthDate] = useState("");
    const [country, setCountry] = useState("");

    useEffect(() => {
        if (!existingProfile) {
            setUsername("");
            setAvatarUrl("");
            setBirthDate("");
            setCountry("");
            return;
        }

        setUsername(existingProfile.username || "");
        setAvatarUrl(existingProfile.avatarUrl || "");
        setBirthDate(existingProfile.birthDate || "");
        setCountry(existingProfile.country || "");
    }, [existingProfile]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            username: username,
            avatarUrl: avatarUrl || null,
            birthDate: birthDate || null,
            country: country,
        };

        try {
            if (existingProfile) {
                await updateProfile(payload);
            } else {
                await createProfile(payload);
            }

            const freshProfile = await getCurrentProfile();
            dispatch(setProfile(freshProfile));

            showToast("Profile saved successfully", "success");
        } catch (err) {
            showToast(getErrorMessage(err), "error");
        }
    };

    return (
        <div className="profile-page">
            <form className="profile-form" onSubmit={handleSubmit}>
                <h2>Profile</h2>

                <div className="profile-avatar-preview">
                    <img
                        src={avatarUrl || defaultAvatar}
                        alt="Avatar preview"
                    />
                </div>

                <input
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <input
                    placeholder="Avatar URL"
                    value={avatarUrl}
                    onChange={(e) => setAvatarUrl(e.target.value)}
                />

                <input
                    type="date"
                    value={birthDate}
                    onChange={(e) => setBirthDate(e.target.value)}
                />

                <input
                    placeholder="Country"
                    value={country}
                    onChange={(e) => setCountry(e.target.value)}
                />

                <button type="submit">
                    {existingProfile ? "Update Profile" : "Create Profile"}
                </button>
            </form>
        </div>
    );
};

export default ProfilePage;
