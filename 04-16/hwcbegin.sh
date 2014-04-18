#!/bin/sh
hadoop jar /home/hadoop/hadoop/contrib/streaming/hadoop-streaming-1.0.4.jar \
        -D mapred.reduce.tasks=0 \
        -D mapred.task.timeout=999999999  -input /user/mrtest/input_txt/input.txt \
        -output /user/mrtest/output -mapper _mapper.sh -file /home/dxp/_mapper.sh
        
#-D mapred.map.tasks.speculative.execution=false \
