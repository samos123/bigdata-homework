#!/bin/bash

count=1
for f in dataset/*
do
    echo $count
    mv "$f" dataset/video$count.mp4
    (( count += 1 ))
done

