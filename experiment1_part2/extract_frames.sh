#!/bin/bash

set -x

#rm images -rf
mkdir images

for file in dataset/*
do


filename=${file:8}


# Extract images from every video
# 1 frame per seconds is -r 1
ffmpeg -i "$file" -r 1 "images/$filename-i%d.jpg" < /dev/null

#ls $filename-images | xargs -I {} -n 1 java -jar /home/dxp/bigdata.jar $filename-images/{} $filename-features/{}
#ls -1 $filename-images > $filename-images-created

# Add the path /user/mrtest/output/$filename-images to every image
#sed -i s+^+/user/mrtest/output/$filename-images/+g $filename-images-created

# Extract audio from video
#/usr/local/ffmpeg/bin/ffmpeg -i $filename -vn -ac 1 -ar 16000  -f wav $filename.wav < /dev/null
#/home/hadoop/hadoop/bin/hadoop fs -put $filename.wav /user/mrtest/output/$filename.wav

#rm {$filename-images,$filename,$filename-images-created,$filename.wav} -rf

done
