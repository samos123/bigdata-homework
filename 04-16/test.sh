#!/bin/sh
/usr/local/ffmpeg/bin/ffmpeg  -r:v '75/1' -i /home/dxp/Titanic.avi -an -r:v '15/1'  /home/dxp/BT.avi
