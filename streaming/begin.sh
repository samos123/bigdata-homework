#!/bin/sh


# Extract the images and audio files of the videos using ffmpeg
hadoop jar /home/hadoop/hadoop/contrib/streaming/hadoop-streaming-1.0.4.jar \
        -D mapred.task.timeout=999999999 \
        -D mapred.reduce.tasks=0\
        -input /user/mrtest/input_txt/input.txt \
        -output /user/mrtest/output \
        -mapper _mapper.sh \
        -file /home/dxp/_mapper.sh \


# Extract the features of the images
hadoop fs -rmr /user/mrtest/tmp/feature-output
hadoop jar /home/dxp/bigdata.jar /user/mrtest/output/images-created/ /user/mrtest/tmp/feature-output


# Extract the features of the audio files
hadoop fs -ls /user/mrtest/output/*.wav | awk '{ print $8 }' > audio-files
hadoop fs -put audio-files /user/mrtest/output/audio-files
hadoop fs -rmr /user/mrtest/tmp/audio-feature-output
hadoop jar /home/dxp/mrtest.jar org.tisnghua.bigdata.WordCount /user/mrtest/output/audio-files /user/mrtest/tmp/audio-feature-output
