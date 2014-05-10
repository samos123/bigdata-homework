#!/bin/bash

set -x

#rm audio -rf
mkdir audio

for file in dataset/*
do


filename=${file:8}


# Extract images from every video
# 1 frame per seconds is -r 1
#ffmpeg -i "$file" -r 1 "images/$filename-i%d.jpg" < /dev/null


# Extract audio from video
ffmpeg -i "$file" -vn -ac 1 -ar 16000 -f wav "audio/$filename.wav" < /dev/null
#/home/hadoop/hadoop/bin/hadoop fs -put $filename.wav /user/mrtest/output/$filename.wav

#rm {$filename-images,$filename,$filename-images-created,$filename.wav} -rf

done
