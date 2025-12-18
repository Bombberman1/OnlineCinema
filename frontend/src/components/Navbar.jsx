import { Link, useLocation } from "react-router-dom";
import { useSelector } from "react-redux";
import AvatarMenu from "./AvatarMenu";
import "../styles/navbar.css";

const Navbar = () => {
    const location = useLocation();
    const { isAuthenticated, isAdmin } = useSelector(
        (state) => state.auth
    );

    const isActive = (path) => {
        return location.pathname === path
            ? "navbar-link active"
            : "navbar-link";
    };

    return (
        <nav className="navbar">
            <div className="navbar-left">
                <Link to="/" className="navbar-logo">
                    <span className="brand-red">Online</span>Cinema
                </Link>

                {isAuthenticated && (
                    <>
                        <Link to="/movies" className={isActive("/movies")}>
                            Movies
                        </Link>

                        <Link to="/favorites" className={isActive("/favorites")}>
                            Favorites
                        </Link>

                        {isAdmin && (
                            <Link to="/admin" className={isActive("/admin")}>
                                Admin
                            </Link>
                        )}
                    </>
                )}
            </div>

            <div className="navbar-right">
                {isAuthenticated && <AvatarMenu />}
            </div>
        </nav>
    );
};

export default Navbar;
