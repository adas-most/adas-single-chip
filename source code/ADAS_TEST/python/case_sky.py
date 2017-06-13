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
import sys
import winsound
import threading    #導入 threading 模組
import thread

# print 'parameter num : ', len(sys.argv)
# print 'parameter : ', str(sys.argv)

# para1 = int(sys.argv[1])

# print para1

# if( 100 == para1):
# 	print "int"


cap = cv2.VideoCapture('sample_fix_no_rain.mp4')
l_candidate_num = 0
LC = np.zeros((30,2),np.int16)
VideoEndCnt = 0
WarningFlag = 0

stepNum = 50
CVFlag = 0
Pos_Flag = 0
Neg_Flag = 0
warnning = 0
threshold_warnning = 0
BigFlag = 0

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


while(VideoEndCnt < 95):
# while(cap.isOpened()):
	# print VideoEndCnt
	# ticks = time.time()

	ret, frame = cap.read()
	ret, frame2 = cap.read()
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

	
	analyze_img = gray
	img = frame2
	# img2 = cv2.imread(using_name)
	# img3 = cv2.imread(using_name)
	# img4 = cv2.imread(using_name)
	# img5 = cv2.imread(using_name)
	
	pixelY = img.shape[0]
	pixelX = img.shape[1]		


	svm = cv2.SVM()
	svm.load('HoG_sky_HW_API_64x64.dat')
	
	# Image_data = np.zeros((1,32,32),np.float32)
	temp_LC_cnt = 0
	temp_LC = np.zeros((30,2),np.int16)

	# y_start = 0
	y_start = img.shape[0]/2
	# y_end = img.shape[0]/2
	# y_end = img.shape[0]-140
	y_end = img.shape[0]-70

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
	Midx = 0
	Midy = 0
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
				img_test = analyze_img[y1:y2,x1:x2]
								
				fd, hog_image = hog(img_test, orientations=8, pixels_per_cell=(8, 8),
				                    cells_per_block=(8, 8), visualise=True)
				data = np.float32(hog_image).reshape(-1,64*64)
					
				result = svm.predict_all(data)
				
				############################################# SVM testing Func ############################################# 
				Midx = (x1+x2)/2
				Midy = (y1+y2)/2
				if( 1 == result ): # Left
					# cv2.rectangle(img,(x1,y1),(x2,y2),(0,0,255),3)			
					# cv2.circle(img,(Midx,Midy), 5, (0,0,255), -1)					
					temp_LC[temp_LC_cnt][0] = Midx
					temp_LC[temp_LC_cnt][1] = Midy
					temp_LC_cnt += 1
					# print " ( " + str(Midx) + "," + str(Midy) + " ) "
									
				x1 = x1 + stepNum
				x2 = x2 + stepNum			
		x1 = 0
		x2 = 0 + ScanX		
		y1 = y1 + stepNum
		y2 = y2 + stepNum

	# if( k%10 == 0 ):
	# 	print " "
	# 	print " Loop : " + str(k)
	# 	print " "
	# cv2.imwrite("result_v2/result_" + str(k) + ".jpg",img)	

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
		# print " ( " + str(Midx) + "," + str(Midy) + " ) "
		# cv2.imwrite("result_v3/result_" + str(k) + ".jpg",img)	
		# cv2.imshow('frame',img)
		# cv2.waitKey(0)
	elif( LC[l_candidate_num-1][0] != Midx):
		if( 0 == Midx ):			
			continue
		LC[l_candidate_num][0] = Midx
		LC[l_candidate_num][1] = Midy		
		temp = LC[l_candidate_num-1][0]
		result_warnning = temp - LC[l_candidate_num][0]
		# print " ( " + str(Midx) + "," + str(Midy) + " ) ===> "  + str(temp) + " , " + str(LC[l_candidate_num][0]) 

		# print result_warnning
		if( result_warnning >= 25 ):
			# print "!!!!!!!!!! left ---->> right !!!!!!!!!!"
			Pos_Flag = 1	
			warnning = 1		
		elif( result_warnning < 0 ):
			# print " not left ---->> right "			
			Neg_Flag = 1
		

		if( Pos_Flag and Neg_Flag ):		
			# print " =========================================== "	
			threshold_warnning += 1
			Pos_Flag = 0
			Neg_Flag = 0

		l_candidate_num += 1

	
	if( 3 == threshold_warnning and 1 == warnning ):
		# print " stop "
		threshold_warnning = 0
		warnning = 0
		BigFlag = 1	
		t1.stop()
	elif( 1 == warnning and 0 == BigFlag):
		# print " start "
		if( 1 == OneThreadFlag ):
			t1.start()	
			OneThreadFlag = 0
		# winsound.Beep(600,100)		
		print "!!!!!!!!!! left ---->> right !!!!!!!!!!"
		cv2.circle(img,(50,30), 15, (0,0,255), -1) # Red		
		cv2.putText(img,'Abnormal driving',(100,50), font, 2,(0,0,255),5)
	else:
		cv2.circle(img,(50,30), 15, (0,255,0), -1)
		cv2.putText(img,'Normal driving',(100,50), font, 2,(0,255,0),5)
	# ticks2 = time.time()	
	# sumi = ticks2 - ticks
	# print sumi

	cv2.imshow('frame',img)
	VideoEndCnt += 1
	if cv2.waitKey(1) & 0xFF == ord('q'):
		break

cap.release()
cv2.destroyAllWindows()