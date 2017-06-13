# -*- coding: UTF-8 -*-
import cv2
import numpy as np
import io
from matplotlib import pyplot as plt
import time
import datetime
import matplotlib.pyplot as plt
from skimage.feature import hog
from skimage import data, color, exposure
import winsound
import threading    #導入 threading 模組
import thread
cap = cv2.VideoCapture('sample_fix.mp4')


stepNum = 50
l_candidate_num = 0
LC = np.zeros((30,2),np.int16)
VideoEndCnt = 0
WarningFlag = 0

OneThreadFlag = 1

class MyThread(threading.Thread):
	def __init__(self,flag):
		threading.Thread.__init__(self)
		self.a = True
		self.flag = flag

	def run(self):
		self.Warnning_Beep(self.flag)

	def Warnning_Beep(self,flag):
		while self.a:
			if( 1 == flag ):
				winsound.Beep(600,500)
	def stop(self):
		self.a = False

t1 = MyThread(OneThreadFlag)
font = cv2.FONT_HERSHEY_SIMPLEX
debug_cnt = 0
start_flag = 0
end_flag = 0

while(VideoEndCnt < 88):
	# print VideoEndCnt
	# ticks = time.time()

	ret, frame = cap.read()
	ret, frame2 = cap.read()
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

	
	# ====================================================================================================== #
	analyze_img = gray
	img = frame
	img2 = frame2
	pixelY = img.shape[0]
	pixelX = img.shape[1]		
	
	svm = cv2.SVM()
	svm.load('HoG_3type_HW_API_64x64v2.dat')
	
	# Image_data = np.zeros((1,32,32),np.float32)
	temp_LC_cnt = 0
	temp_LC = np.zeros((5,2),np.int16)

	y_start = 0
	y_end = img.shape[0]/2

	# x_start = 0
	x_start = 200
	x_end = img.shape[1]-200
	ScanX = 64
	ScanY = 64
	y1 = y_start
	y2 = y_start + ScanY

	# 由下至上 掃描
	# y2 = y_end
	# y1 = y_end - ScanY
	x1 = x_start
	x2 = x_start + ScanX

	for i in range(0,pixelY,1):
		for j in range(0,pixelX,1):
			# if y1 <= (pixelY/2):# 由下至上 掃描
			# 	continue
			if y2 >= y_end:
				continue
			elif x2 >= x_end:
				continue
			else:
				# if(x2 > ((1280/2)+(1280/4))):
				# 	continue
				# else:
				# unit2.ImgSplit(using_name,x1,y1,x2,y2,"svm_predict.jpg")
				img_test = analyze_img[y1:y2,x1:x2]
								
				fd, hog_image = hog(img_test, orientations=8, pixels_per_cell=(8, 8),
				                    cells_per_block=(8, 8), visualise=True)
				data = np.float32(hog_image).reshape(-1,64*64)
					
				result = svm.predict_all(data)
				
				############################################# SVM testing Func ############################################# 
				Midx = (x1+x2)/2
				Midy = (y1+y2)/2
				if( 0 == result ): # Left
					cv2.rectangle(img,(x1,y1),(x2,y2),(0,0,255),3)			
					cv2.circle(img,(Midx,Midy), 5, (0,0,255), -1)					
					temp_LC[temp_LC_cnt][0] = Midx
					temp_LC[temp_LC_cnt][1] = Midy
					temp_LC_cnt += 1
									
				x1 = x1 + stepNum
				x2 = x2 + stepNum			
		x1 = 0
		x2 = 0 + ScanX		
		y1 = y1 + stepNum
		y2 = y2 + stepNum

	
	if( 2 == temp_LC_cnt ):
		Midx = (temp_LC[0][0]+temp_LC[1][0])/2
		Midy = (temp_LC[0][1]+temp_LC[1][1])/2
	else:
		Midx = temp_LC[0][0]
		Midy = temp_LC[0][1]
	if(0 == l_candidate_num):
		LC[l_candidate_num][0] = Midx
		LC[l_candidate_num][1] = Midy
		l_candidate_num += 1
		# cv2.circle(img2,(50,50), 15, (0,0,255), -1)
	elif( LC[l_candidate_num-1][0] != Midx):
		LC[l_candidate_num][0] = Midx
		LC[l_candidate_num][1] = Midy
		temp = LC[0][0]
		result_warnning = temp - LC[l_candidate_num][0]
		# print result_warnning
		if( 50 == result_warnning ):
			WarningFlag = 1
		else:
			WarningFlag = 0
		l_candidate_num += 1
	
	# if( 1 == WarningFlag ):
	# 	if( 0 == debug_cnt ):
	# 		print " start "
	# 		start_flag = 1
	# 		t1.start()
	# 	debug_cnt += 1	
	# 	# print "!!!!!!!!!! left ---->> right !!!!!!!!!!"				
	# 	# cv2.circle(img2,(50,50), 15, (0,0,255), -1)
	# # else:
	# 	# print " stop "		
	# 	# t1.stop()
	# 	# cv2.circle(img2,(50,50), 15, (0,255,0), -1)
	# if( 17 == debug_cnt ):
	# 	print " stop "
	# 	t1.stop()
	# 	debug_cnt += 1
	# 	end_flag = 1
	# 	start_flag = 0

	# if( 1 == start_flag ):
	# 	print "!!!!!!!!!! left ---->> right !!!!!!!!!!"
	# 	cv2.circle(img2,(50,50), 15, (0,0,255), -1) # Red
	# elif( 1 == end_flag ):
	# 	cv2.circle(img2,(50,50), 15, (0,255,0), -1) # green
	# else:
	# 	cv2.circle(img2,(50,50), 15, (0,255,0), -1) # green

	# cv2.circle(img2,(50,50), 15, (0,255,0), -1) # green
	cv2.circle(img2,(50,30), 15, (0,255,0), -1)
	cv2.putText(img2,'Normal driving',(100,50), font, 2,(0,255,0),5)

	# ticks2 = time.time()	
	# sumi = ticks2 - ticks
	# print sumi

	cv2.imshow('frame',img2)
	VideoEndCnt += 1
	if cv2.waitKey(1) & 0xFF == ord('q'):
		break
# print LC

cap.release()
cv2.destroyAllWindows()