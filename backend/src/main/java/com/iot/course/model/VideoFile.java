package com.iot.course.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
    name = "video_files",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"movie_id", "quality"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "quality", nullable = false)
    private Integer quality;

    @Column(name = "file_path", nullable = false)
    private String filePath;
}
