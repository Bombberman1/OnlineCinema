import { api } from "./axios";

export const getVideoQualities = async (movieId) => {
    const response = await api.get(`/video_files/${movieId}/qualities`);
    return response.data;
};

// export const watchVideo = async (movieId, quality) => {
//     const res = await api.get(
//         `/video_files/${movieId}/watch?quality=${quality}`
//     );
//     return res.data;
// };
