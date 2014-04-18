#!/bin/bash

while read file
do

filename=`basename $file`
/home/hadoop/hadoop/bin/hadoop fs -get $file /tmp/$filename

mkdir -p $filename-images
mkdir -p $filename-features

# Extract images from every video
/usr/local/ffmpeg/bin/ffmpeg -i /tmp/$filename $filename-images/image%d.jpg < /dev/null

for image in $filename-images/*; do
    output=`basename $image`
    java -jar /home/dxp/bigdata.jar $image $filename-features/$output
done

#ls $filename-images | xargs -I {} -n 1 java -jar /home/dxp/bigdata.jar $filename-images/{} $filename-features/{}


# Upload generated images to hadoop
/home/hadoop/hadoop/bin/hadoop fs -copyFromLocal $filename-images /user/mrtest/output/$filename-images
/home/hadoop/hadoop/bin/hadoop fs -copyFromLocal $filename-features /user/mrtest/output/$filename-features




# Output the image path as key to the reducer
#hadoop fs -ls /user/mrtest/output/$filename-images | awk '{ print $8}' | sed 's/$/\t1/g' | xargs -n 1 java -jar bigdata.jar

done
