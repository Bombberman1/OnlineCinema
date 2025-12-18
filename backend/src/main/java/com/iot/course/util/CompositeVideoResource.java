package com.iot.course.util;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.InputStreamResource;

public class CompositeVideoResource extends InputStreamResource {
    public CompositeVideoResource(Path ad, Path movie) throws IOException {
        super(new SequenceInputStream(
            Files.newInputStream(ad),
            Files.newInputStream(movie)
        ));
    }
}