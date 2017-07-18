#! /bin/bash

event_cnt=3
sudo rm $event_cnt".h264"
sudo raspivid -vf -hf -t 5000 -w 640 -h 480 -b 1200000 -p 0,0,640,480 -fps 25 -o $event_cnt"1.h264" 
echo $event_cnt"1.mp4"