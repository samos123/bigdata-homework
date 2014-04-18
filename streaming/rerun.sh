#!/bin/bash

# Copy script to all servers from master 101
for i in {2..4}; do 
#    scp lire_index.sh root@pc20$i:/home/dxp/
#    scp -r lire root@pc20$i:/home/dxp/
    scp _reducer.sh root@pc20$i:/home/dxp/
    scp _mapper.sh root@pc20$i:/home/dxp/
    scp bigdata.jar root@pc20$i:/home/dxp/
    ssh root@pc20$i "rm /tmp/video* -r"
done

rm /tmp/video* -rf

# Remove mrtest 

# Remove hadoop output directory
hadoop fs -rmr /user/mrtest/output

./begin.sh
