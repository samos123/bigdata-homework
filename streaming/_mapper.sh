#!/bin/bash

while read file
do

filename=`basename $file`
/home/hadoop/hadoop/bin/hadoop fs -get $file $filename

mkdir -p $filename-images

# Extract images from every video
/usr/local/ffmpeg/bin/ffmpeg -i $filename $filename-images/image%d.jpg < /dev/null

#ls $filename-images | xargs -I {} -n 1 java -jar /home/dxp/bigdata.jar $filename-images/{} $filename-features/{}
ls -1 $filename-images > $filename-images-created

# Add the path /user/mrtest/output/$filename-images to every image
sed -i s+^+/user/mrtest/output/$filename-images/+g $filename-images-created

# Upload generated images to hadoop
/home/hadoop/hadoop/bin/hadoop fs -copyFromLocal $filename-images \
    /user/mrtest/output/$filename-images
/home/hadoop/hadoop/bin/hadoop fs -put $filename-images-created \
    /user/mrtest/output/images-created/$filename-images-created

done
