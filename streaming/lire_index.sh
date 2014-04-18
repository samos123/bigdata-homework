#!/bin/bash

directory=$1

lib=$(find /home/dxp/lire/lib -name \*.jar | tr "\\n" ":")
export CLASSPATH=$lib:/home/dxp/lire/build:$CLASSPATH

java net.semanticmetadata.lire.sampleapp.ParallelIndexing $directory

echo "Running indexer on the following directory of images $directory"
