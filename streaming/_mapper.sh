#!/bin/bash

mkdir m
mount -t tmpfs -o size=2G,mode=0744 tmpfs m
cd m

while read file
do

file=`echo $file | cut -d " " -f2-`
filename=`basename $file`
mkdir -p $filename-images
/home/hadoop/hadoop/bin/hadoop fs -get $file $filename

# Extract images from every video
# 1 frame per seconds is -r 1
/usr/local/ffmpeg/bin/ffmpeg -i $filename -r 1 $filename-images/image%d.jpg < /dev/null

#ls $filename-images | xargs -I {} -n 1 java -jar /home/dxp/bigdata.jar $filename-images/{} $filename-features/{}
ls -1 $filename-images > $filename-images-created

# Add the path /user/mrtest/output/$filename-images to every image
sed -i s+^+/user/mrtest/output/$filename-images/+g $filename-images-created

# Upload generated images to hadoop
/home/hadoop/hadoop/bin/hadoop fs -copyFromLocal $filename-images \
    /user/mrtest/output/$filename-images
/home/hadoop/hadoop/bin/hadoop fs -put $filename-images-created \
    /user/mrtest/output/images-created/$filename-images-created

# Extract audio from video
/usr/local/ffmpeg/bin/ffmpeg -i $filename -vn -ac 1 -ar 16000  -f wav $filename.wav < /dev/null
/home/hadoop/hadoop/bin/hadoop fs -put $filename.wav /user/mrtest/output/$filename.wav

rm {$filename-images,$filename,$filename-images-created,$filename.wav} -rf

done

cd ..
umount m
