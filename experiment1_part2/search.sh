#!/bin/bash

set -x

java -cp ../experiment1_part1/lib/*:imagesearcher.jar org.tsinghua.bigdata.Searcher "$1"
