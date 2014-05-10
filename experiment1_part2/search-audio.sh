#!/bin/bash

set -x

java -cp mfcc.jar hMFCC.HEMfcc "$1" $PWD/audio-features 10
