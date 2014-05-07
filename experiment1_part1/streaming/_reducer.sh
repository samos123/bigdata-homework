#!/bin/bash

echo "Test reducer" > /tmp/reducer.log

while read file
do
filename=`cut -f1 $file`
logfile=`basename $filename`
echo "Test"
echo "Executing java program with $filename" > /tmp/$logfile.log
java -jar bigdata.jar $filename >> /tmp/$logfile.log

done
