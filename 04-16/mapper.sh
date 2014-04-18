#!/bin/sh

id = root

mkdir -p /tmp/$id
mkdir -p /tmp/$id/target

while read line; do
input=$line
filename = $input
hadoop fs -get $input /tmp/$id/$filename

##/usr/local/ffmpeg/bin/ffmpeg  -r:v '75/1' -i /tmp/$id/$filename -an -r:v '15/1' /tmp/$id/target/$filename

hadoop fs -put /tmp/$id/target/$filename /user/mrtest/output/$filename

done
##rm -rf /tmp/$id
