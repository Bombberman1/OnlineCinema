package com.iot.course.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iot.course.dto.video_file.VideoFileQualityResponseDTO;
import com.iot.course.dto.video_file.VideoFileRequestDTO;
import com.iot.course.dto.video_file.VideoFileUploadRequestDTO;
import com.iot.course.exception.Exists;
import com.iot.course.exception.NotFound;
import com.iot.course.model.Movie;
import com.iot.course.model.User;
import com.iot.course.model.VideoFile;
import com.iot.course.repository.MovieRepository;
import com.iot.course.repository.UserRepository;
import com.iot.course.repository.VideoFileRepository;
import com.iot.course.util.CustomUserDetails;

@Service
public class VideoFileService {
    @Autowired
    private VideoFileRepository videoFileRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WatchHistoryService watchHistoryService;

    @Value("${videos-static-path}")
    private String staticPath;
    @Value("${videos-path}")
    private String locationPath;

    public void create(VideoFileRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.movieId())
                                    .orElseThrow(() -> new NotFound("Movie not found"));

        if (videoFileRepository.findByMovieIdAndQuality(
                dto.movieId(),
                dto.quality()
            )
            .isPresent()
        ) {
            throw new Exists("Video with this quality already exists");
        }

        if (Files.notExists(Paths.get(dto.filePath()))) {
            throw new NotFound("Video not found on server");
        }

        VideoFile video = VideoFile.builder()
                                .movie(movie)
                                .quality(dto.quality())
                                .filePath(dto.filePath())
                                .build();

        videoFileRepository.save(video);
    }

    public void uploadVideo(MultipartFile file, VideoFileUploadRequestDTO dto) {
        Movie movie = movieRepository.findById(dto.movieId())
                                    .orElseThrow(() -> new NotFound("Movie not found"));

        try {
            Path hlsDir = Paths.get(
                locationPath,
                movie.getId().toString(),
                dto.quality().toString()
            );
            Files.createDirectories(hlsDir);

            Path sourceMp4 = hlsDir.resolve("source.mp4");
            Files.copy(file.getInputStream(), sourceMp4, StandardCopyOption.REPLACE_EXISTING);

            ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y", "-i", sourceMp4.toString(),
                "-c:v", "h264", "-c:a", "aac",
                "-ar", "48000", "-b:a", "128k",
                "-hls_time", "4", "-hls_playlist_type", "vod",
                "-hls_segment_filename",
                hlsDir.resolve("seg_%05d.ts").toString(),
                hlsDir.resolve("index.m3u8").toString()
            );

            pb.redirectErrorStream(true);
            Process p = pb.start();
            if (p.waitFor() != 0) {
                throw new InternalError("Video encoding failed");
            }

            Files.deleteIfExists(sourceMp4);

            videoFileRepository.findByMovieIdAndQuality(movie.getId(), dto.quality())
                            .ifPresent(videoFileRepository::delete);

            videoFileRepository.save(
                VideoFile.builder()
                        .movie(movie)
                        .quality(dto.quality())
                        .filePath(hlsDir.resolve("index.m3u8").toString())
                        .build()
            );

        } catch (InterruptedException ex) {
            throw new InternalError("Failed to store video file", ex);
        } catch (IOException ex) {
            throw new InternalError("Failed to store video file", ex);
        }
    }

    public List<VideoFileQualityResponseDTO> getAvailableQualities(Long movieId) {
        return videoFileRepository.findByMovieId(movieId)
                                .stream()
                                .map(v -> new VideoFileQualityResponseDTO(v.getQuality()))
                                .toList();
    }

    public String loadHlsPlaylist(Long movieId, Integer quality) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getPrincipal();

        User user = userRepository.findById(userDetails.getId())
                                .orElseThrow(() -> new NotFound("User not found"));

        boolean hasSub = subscriptionService.hasActiveSubscription(user.getId());

        VideoFile video = videoFileRepository.findByMovieIdAndQuality(movieId, quality)
                                            .orElseThrow(() -> new NotFound("Video not found or wrong quality"));

        watchHistoryService.create(movieId);

        Path moviePlaylist = Paths.get(video.getFilePath());
        if (!Files.exists(moviePlaylist)) {
            throw new NotFound("Movie playlist not found");
        }

        try {
            List<String> finalPlaylist = new java.util.ArrayList<>();

            Path basePlaylist = hasSub ? Paths.get(video.getFilePath())
                                    : Paths.get(locationPath + "/ads/coca_cola/index.m3u8");

            List<String> baseLines = Files.readAllLines(basePlaylist);

            for (String line : baseLines) {
                if (line.startsWith("#EXTINF") || line.endsWith(".ts")) {
                    break;
                }
                finalPlaylist.add(line);
            }

            if (!hasSub) {
                appendSegments(
                    finalPlaylist,
                    Paths.get(locationPath + "/ads/coca_cola/index.m3u8")
                );
                finalPlaylist.add("#EXT-X-DISCONTINUITY");
            }

            appendSegments(
                finalPlaylist,
                Paths.get(video.getFilePath())
            );

            finalPlaylist.add("#EXT-X-ENDLIST");

            return String.join("\n", finalPlaylist) + "\n";
        } catch (IOException e) {
            throw new NotFound("Failed to load Movie playlist");
        }
    }

    private void appendSegments(List<String> out, Path playlist) throws IOException {
        for (String line : Files.readAllLines(playlist)) {
            line = line.trim();

            if (line.isEmpty()) continue;

            if (line.startsWith("#EXTINF") || line.endsWith(".ts")) {
                out.add(line);
            }
        }
    }

    public List<VideoFile> getAll() {
        return videoFileRepository.findAll();
    }
}
