import { Link } from "react-router-dom";
import "../../styles/admin-dashboard.css";

const AdminDashboard = () => {
    return (
        <div className="admin-dashboard">
            <h2 className="admin-title">Admin Panel</h2>

            <div className="admin-cards">
                <Link to="/admin/movies" className="admin-card">
                    <h3>Movies</h3>
                    <p>Create, view and delete movies</p>
                </Link>

                <Link to="/admin/video_files" className="admin-card">
                    <h3>Video Files</h3>
                    <p>Upload and manage video qualities</p>
                </Link>
            </div>
        </div>
    );
};

export default AdminDashboard;
