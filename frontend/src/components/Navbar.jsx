import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import AvatarMenu from "./AvatarMenu";
import "../styles/navbar.css";

const Navbar = () => {
    const { isAuthenticated, isAdmin } = useSelector(
        (state) => state.auth
    );

    return (
        <nav className="navbar">
            <div className="navbar-left">
                <Link to="/" className="navbar-logo">
                    OnlineCinema
                </Link>

                {isAuthenticated && (
                    <>
                        <Link to="/" className="navbar-link">
                            Movies
                        </Link>

                        {isAdmin && (
                            <Link to="/admin" className="navbar-link">
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
