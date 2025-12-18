import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import AvatarMenu from "./AvatarMenu";
import "../styles/navbar.css";

const Navbar = () => {
    const isAuthenticated = useSelector(
        (state) => state.auth.isAuthenticated
    );

    return (
        <nav className="navbar">
            <div className="navbar-left">
                <Link to="/">OnlineCinema</Link>
            </div>

            <div className="navbar-right">
                {isAuthenticated && <AvatarMenu />}
            </div>
        </nav>
    );
};

export default Navbar;
