#! /bin/bash

i=0
event_cnt=3
until [ $i = '1' ]
do
	echo " ======== Shell Windows Info ======== "
	echo " Mode 1 is open OBD-II sh "
	echo " Mode 2 is open CheckParamter sh "
	echo " others Num is stop this shell. "
	printf " Please Enter Mode Num: "
	read input
	echo "you are input is Mode $input "
	echo " ======== Shell Windows Info ======== "

	if [ $input = '1' ]
	then
		echo "Using command_OBD2.sh"
		cd Desktop
		cd FileDist
		sudo rm $event_cnt".mp4"
		sudo rm $event_cnt".h264"
		sudo raspivid -vf -hf -t 3000 -w 640 -h 480 -b 1200000 -p 0,0,640,480 -fps 25 -o $event_cnt".h264" 
		sudo MP4Box -add $event_cnt".h264" -fps 25 $event_cnt".mp4"
		sudo omxplayer $event_cnt".mp4"
		sudo python uart_rpi3_for_OBD2.py $event_cnt
		event_cnt=`expr $event_cnt + 1`
	elif [ $input = '2' ]
	then
		cd Desktop
		cd FileDist
		echo "Using command_Parameter.sh"
		sudo java -jar updatePara.jar
		sudo sh Parameter.sh
	else
		echo "Stop"
		i=`expr $i + 1`
	fi
done

