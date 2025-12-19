export const getErrorMessage = (error) => {
    if (error.response && error.response.data) {
        return error.response.data.message || "Request failed";
    }

    return "Server is unavailable";
};
