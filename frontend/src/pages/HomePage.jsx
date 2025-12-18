import "../styles/movies.css";

const HomePage = () => {
    return (
        <div className="movies-grid">
            <div className="movie-card">
                <img src="https://via.placeholder.com/300x450" />
                <div className="movie-card-content">
                    <div className="movie-title">Movie title</div>
                </div>
            </div>
        </div>
    );
};

export default HomePage;
