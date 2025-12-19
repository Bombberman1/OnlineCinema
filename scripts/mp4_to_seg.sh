#!/usr/bin/bash

mkdir -p "$2"

./scripts/ffmpeg-8.0.1-essentials_build/bin/ffmpeg.exe -y -i "$1" \
    -c:v h264 -c:a aac -ar 48000 -b:a 128k \
    -hls_time 4 -hls_playlist_type vod \
    -hls_segment_filename "$2/seg_%05d.ts" \
    "$2/index.m3u8"
