#!/bin/sh
hadoop jar /home/hadoop/hadoop/contrib/streaming/hadoop-streaming-1.0.4.jar \
        -D mapred.task.timeout=999999999 \
        -D mapred.reduce.tasks=0\
        -input /user/mrtest/input_txt/input.txt \
        -output /user/mrtest/output \
        -mapper _mapper.sh \
        -file /home/dxp/_mapper.sh \
        -file /home/dxp/bigdata.jar
#-D mapred.map.tasks.speculative.execution=false \
#        -reducer _reducer.sh \
#        -file /home/dxp/_reducer.sh \
