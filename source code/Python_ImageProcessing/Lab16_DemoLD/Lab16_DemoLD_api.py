# -*- coding: UTF-8 -*-
import cv2
import numpy as np
import io
import my_lib
from matplotlib import pyplot as plt
import time
import datetime
import matplotlib.pyplot as plt
from skimage.feature import hog
from skimage import data, color, exposure

def reshape(SizeX,SizeY,Image_data):	
	ImgCnt = Image_data.shape[0]
	data = np.zeros((ImgCnt,SizeX*SizeY),np.float32)
	cnt = 0
	for i in range(0,ImgCnt,1):
		for y in range(0,SizeY,1):
			for x in range(0,SizeX,1):
				# print " (" + str(i) + "," + str(j) + "," + str(k) + ") __Output__ (" + str(i) + "," + str(cnt) + ") " 
				data[i][cnt] = Image_data[i][y][x]
				cnt = cnt + 1
		cnt = 0
	# print data.shape
	return data

def DataToImg_img(SerialMatrix):
	# (64,64)
	Y_len = SerialMatrix.shape[0]
	X_len = SerialMatrix.shape[1]
	OutImg = np.zeros((Y_len,X_len),np.uint8)
	for y in range(0,Y_len,1):
		for x in range(0,X_len,1):
			if(math.isnan(SerialMatrix[y][x])):
				return 1
			else:
				OutImg[y][x] = SerialMatrix[y][x]		
	return OutImg	
# daimg_name = "Da_Photo11.jpg"
daimg_name = "Photo15.jpg"
hwimg_name = "HW_Photo11.jpg"
ntimg_name = "Night_Photo11.jpg"
rainimg_name = "Rain_Photo11.jpg"
test_name = "test_twopass.jpg"
show = 0
using_name = daimg_name

# img = cv2.imread(using_name)
# gray = cv2.imread(using_name,0)
# gx = cv2.Sobel(gray, cv2.CV_8U, 1, 0)
# gy = cv2.Sobel(gray, cv2.CV_8U, 0, 1)
# absX = cv2.convertScaleAbs(gx)   
# absY = cv2.convertScaleAbs(gy)  
  
# dst = cv2.addWeighted(absX,0.8,absY,0.8,0)  

# tv = np.zeros((dst.shape[0],dst.shape[1]),np.uint8)

# for y in range(0,dst.shape[0],1):
# 	for x in range(0,dst.shape[1],1): # 由左往右 掃描	
# 		if dst[y][x] >= 60:		
# 			tv[y][x] = 255
# 		else:
# 			tv[y][x] = 0
# cv2.imwrite("tv2.jpg",tv)
# # cv2.rectangle(tv,(x1,y1),(x2,y2),(0,0,255),1)	
# tv_img = cv2.imread("tv2.jpg")

# cv2.imshow("tv", tv_img)

# # plt.subplot(121), plt.plot(tv_linebuf_index)
# # plt.subplot(122), plt.plot(center_mat)
# # plt.show()
# cv2.waitKey(0)
# cv2.destroyAllWindows()



#ContinueValueF = 0
CVFlag = 0

string = "C:\\Python_Video_Photo\\new\\Highway\\Photo"
for k in range(11,11+1,1):
	ticks = time.time()
	# print " tick :", ticks
	localtime = time.asctime( time.localtime(time.time()) )
	# print "time :", localtime

	using_name = string + str(k) + ".jpg"
	analyze_img = cv2.imread(using_name,0)
	img = cv2.imread(using_name)
	img2 = cv2.imread(using_name)
	img3 = cv2.imread(using_name)
	img4 = cv2.imread(using_name)
	img5 = cv2.imread(using_name)
	l_candidate_num = 0
	r_candidate_num = 0
	LC = np.zeros((700,2),np.int16)
	RC = np.zeros((700,2),np.int16)
	pixelY = img.shape[0]
	pixelX = img.shape[1]		

	unit2 = my_lib.FuncOthers()

	svm = cv2.SVM()
	svm.load('HoG_3type_HW_API.dat')
	
	# Image_data = np.zeros((1,32,32),np.float32)


	y_start = img.shape[0]/2
	y_end = img.shape[0]
	ScanX = 32
	ScanY = 32
	y1 = y_start
	y2 = y_start + ScanY

	# 由下至上 掃描
	# y2 = y_end
	# y1 = y_end - ScanY
	x1 = 0
	x2 = 0 + ScanX

	for i in range(0,pixelY,1):
		for j in range(0,pixelX,1):
			# if y1 <= (pixelY/2):# 由下至上 掃描
			# 	continue
			if y2 >= pixelY:
				continue
			elif x2 >= pixelX:
				continue
			else:
				# if(x2 > ((1280/2)+(1280/4))):
				# 	continue
				# else:
				# unit2.ImgSplit(using_name,x1,y1,x2,y2,"svm_predict.jpg")
				img_test = analyze_img[y1:y2,x1:x2]
				
				# gray = cv2.imread("svm_predict.jpg",0)
				# Image_data[0] = gray
				# data = reshape(64,64,gray)
				############################################# SVM testing Func ############################################# 
				# ImgCnt = 1
				# Image_data = np.zeros((ImgCnt,64,64),np.float32)
				# for temp_i in range(0,ImgCnt,1):
				# 	temp_img = cv2.imread("svm_predict.jpg",0)
				# 	Image_data[temp_i] = temp_img
				# data = reshape(64,64,Image_data)
				# result = svm.predict_all(data)
				# print result

				# print Image_data.shape

				
				# cell_size = (8,8,256)
				# block_size = (16,16,128)				
				# data = np.zeros((1,32*32),np.float32)
				
				# for HogLoop in range(0,1,1):
					# image = cv2.imread("svm_predict.jpg",0)					
				fd, hog_image = hog(img_test, orientations=8, pixels_per_cell=(4, 4),
				                    cells_per_block=(8, 8), visualise=True)
				data = np.float32(hog_image).reshape(-1,32*32)
					# data_cnt = 0
					# for z in range(0,hog_image.shape[0],1):
					# 	for y in range(0,hog_image.shape[1],1):		
					# 		data[HogLoop][data_cnt] = hog_image[z][y]
					# 		data_cnt = data_cnt + 1
				# print data
				# print data	
				result = svm.predict_all(data)
				# print result
				############################################# SVM testing Func ############################################# 
				Midx = (x1+x2)/2
				Midy = (y1+y2)/2
				if( 1 == result ): # Left
					cv2.rectangle(img,(x1,y1),(x2,y2),(0,0,255),3)			
					cv2.circle(img,(Midx,Midy), 5, (0,0,255), -1)
					LC[l_candidate_num][0] = Midx
					LC[l_candidate_num][1] = Midy					
					l_candidate_num += 1
				if( 2 == result ):	# Right	
					cv2.rectangle(img2,(x1,y1),(x2,y2),(0,0,255),3)								
					cv2.circle(img2,(Midx,Midy), 5, (0,0,255), -1)					 
					RC[r_candidate_num][0] = Midx
					RC[r_candidate_num][1] = Midy
					r_candidate_num += 1
				# if( 0 == result ):
				# 	cv2.rectangle(img,(x1,y1),(x2,y2),(0,0,255),3)							
				# 	cv2.circle(img,(Midx,Midy), 5, (0,0,255), -1)
				# elif( 1 == result ):
				# 	cv2.rectangle(img,(x1,y1),(x2,y2),(0,255,0),3)			
				# 	cv2.circle(img,(Midx,Midy), 5, (0,255,0), -1)
				# elif( 2 == result ):
				# 	cv2.rectangle(img,(x1,y1),(x2,y2),(255,255,0),3)			
				# 	cv2.circle(img,(Midx,Midy), 5, (255,255,0), -1)

				# cv2.imshow("img", img)
				# cv2.waitKey(0)
				# cv2.destroyAllWindows()					
				x1 = x1 + 32
				x2 = x2 + 32			
		x1 = 0
		x2 = 0 + ScanX		
		y1 = y1 + 32
		y2 = y2 + 32

	# cv2.imwrite(str(k) + "_2HoG_Predict_result.jpg",img2)
	# cv2.imwrite(str(k) +"_img5.jpg",img5)
	# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 右側邊線 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 
	RightLD = my_lib.Filter()
	RightLD.ContinuePointFilter(RC)
	RightLD.ContinuePointPossible("R")

	for draw_i in range(0,RightLD.CPF_size+1,1):
		cv2.circle(img2,(RightLD.Filter_1[draw_i][0],RightLD.Filter_1[draw_i][1]), 5, (0,255,0), -1)
	print RightLD.Filter_1
	cv2.imwrite(str(k)+"R_Predict_Candidate_F1.jpg",img2)	

	# for draw_i in range(0,RightLD.CPF_size+1,1):
	# 	cv2.circle(img2,(RightLD.Filter_2[draw_i][0],RightLD.Filter_2[draw_i][1]), 5, (255,0,0), -1)
	# print RightLD.Filter_2	
	# cv2.imwrite(str(k)+"R_Predict_Candidate_F2.jpg",img2)		

	# for draw_i in range(0,RightLD.CPF_size+1,1):
	# 	cv2.circle(img2,(RightLD.Filter_3[draw_i][0],RightLD.Filter_3[draw_i][1]), 5, (0,0,0), -1)
	# print RightLD.Filter_3	
	# cv2.imwrite(str(k)+"R_Predict_Candidate_F3.jpg",img2)

	for draw_i in range(0,RightLD.CPF_size,1):
		if( RightLD.Filter_3[draw_i+1][0] == 0):
			break
		else:
			cv2.line(img4,(RightLD.Filter_3[draw_i][0],RightLD.Filter_3[draw_i][1]),(RightLD.Filter_3[draw_i+1][0],RightLD.Filter_3[draw_i+1][1]),(0,0,255),5)
			cv2.line(img5,(RightLD.Filter_3[draw_i][0],RightLD.Filter_3[draw_i][1]),(RightLD.Filter_3[draw_i+1][0],RightLD.Filter_3[draw_i+1][1]),(0,0,255),5)
	# cv2.imwrite(str(k)+"R_Predict_Line.jpg",img4)
	# cv2.imwrite(str(k) +"_1img5.jpg",img5)

	# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 右側邊線 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 
	LeftLD = my_lib.Filter()
	LeftLD.ContinuePointFilter(LC)
	LeftLD.ContinuePointPossible("L")

	for draw_i in range(0,LeftLD.CPF_size+1,1):
		cv2.circle(img,(LeftLD.Filter_1[draw_i][0],LeftLD.Filter_1[draw_i][1]), 5, (0,255,0), -1)
	print LeftLD.Filter_1
	cv2.imwrite(str(k)+"L_Predict_Candidate_F1.jpg",img)	

	# for draw_i in range(0,LeftLD.CPF_size+1,1):
	# 	cv2.circle(img,(LeftLD.Filter_2[draw_i][0],LeftLD.Filter_2[draw_i][1]), 5, (255,0,0), -1)
	# print LeftLD.Filter_2
	# cv2.imwrite(str(k)+"L_Predict_Candidate_F2.jpg",img)		

	# for draw_i in range(0,LeftLD.CPF_size+1,1):
	# 	cv2.circle(img,(LeftLD.Filter_3[draw_i][0],LeftLD.Filter_3[draw_i][1]), 5, (0,0,0), -1)
	# print LeftLD.Filter_3
	# cv2.imwrite(str(k)+"L_Predict_Candidate_F3.jpg",img)	

	for draw_i in range(0,LeftLD.CPF_size,1):
		if( LeftLD.Filter_3[draw_i+1][0] == 0):
			break
		else:
			cv2.line(img3,(LeftLD.Filter_3[draw_i][0],LeftLD.Filter_3[draw_i][1]),(LeftLD.Filter_3[draw_i+1][0],LeftLD.Filter_3[draw_i+1][1]),(0,0,255),5)
			cv2.line(img5,(LeftLD.Filter_3[draw_i][0],LeftLD.Filter_3[draw_i][1]),(LeftLD.Filter_3[draw_i+1][0],LeftLD.Filter_3[draw_i+1][1]),(0,0,255),5)
	# cv2.imwrite(str(k)+"L_Predict_Line.jpg",img3)
	cv2.imwrite(str(k)+"_LR_Result.jpg",img5)
	
	# img5 = np.zeros((img5.shape[0],img5.shape[1]),np.uint8)
	print " Finished at loop : " + str(k) 

	ticks2 = time.time()
	# print " tick :", ticks2
	localtime = time.asctime( time.localtime(time.time()) )
	# print "time :", localtime
	sumi = ticks2 - ticks
	print sumi

# !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Orig Code !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 右側邊線 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 
# # 第一次濾波 濾除非連續點
# print RC
# RC_F1 = np.zeros((120,2),np.int16)
# num = 0
# loop_i = 1
# while( loop_i < 120) :
# 	if( 0 == (RC[loop_i][1]-RC[loop_i-1][1]) ):
# 		if( 32 == (RC[loop_i][0]-RC[loop_i-1][0]) ):			
# 			if( (RC[loop_i-1][0] == RC_F1[num][0])and(RC[loop_i-1][1] == RC_F1[num][1]) ):
# 				if( (0 == RC_F1[num][0])and(0 == RC_F1[num][1]) ):
# 					RC_F1[num][0] = RC[loop_i-1][0]
# 					RC_F1[num][1] = RC[loop_i-1][1]
# 					RC_F1[num+1][0] = RC[loop_i][0]
# 					RC_F1[num+1][1] = RC[loop_i][1]
# 					num += 1
# 				else:
# 					RC_F1[num+1][0] = RC[loop_i][0]
# 					RC_F1[num+1][1] = RC[loop_i][1]
# 					num += 1						
# 			elif( (0 != RC_F1[num][0])and(0 != RC_F1[num][1]) ):
# 				num += 1
# 				RC_F1[num][0] = RC[loop_i-1][0]
# 				RC_F1[num][1] = RC[loop_i-1][1]
# 				RC_F1[num+1][0] = RC[loop_i][0]
# 				RC_F1[num+1][1] = RC[loop_i][1]
# 				num += 1
# 			else:
# 				RC_F1[num][0] = RC[loop_i-1][0]
# 				RC_F1[num][1] = RC[loop_i-1][1]
# 				RC_F1[num+1][0] = RC[loop_i][0]
# 				RC_F1[num+1][1] = RC[loop_i][1]
# 				num += 1
# 			loop_i += 1	
# 		else:	
# 			loop_i += 1
# 	else:
# 		loop_i += 1


# RC_Filter_1 = np.zeros((num+1,2),np.int16)
# RC_Filter_2 = np.zeros((num+1,2),np.int16)

# for draw_i in range(0,num+1,1):
# 	RC_Filter_1[draw_i][0] = RC_F1[draw_i][0]
# 	RC_Filter_1[draw_i][1] = RC_F1[draw_i][1]
# 	cv2.circle(img2,(RC_F1[draw_i][0],RC_F1[draw_i][1]), 5, (0,255,0), -1)

# print RC_Filter_1

# cv2.imwrite(str(k) + "R_HoG_Predict_result.jpg",img2)		

# # 第二次濾波 濾除連續點中，不可能之點
# RC_Filter_cnt = 0
# temp_RC_cnt = 0
# array_x = []
# array_y = []
# temp_cnt = 0
# temp_x = np.zeros(1, np.int16)
# temp_y = np.zeros(1, np.int16)
# temp_x2 = np.zeros(1, np.int16)
# temp_y2 = np.zeros(1, np.int16)

# for i in range(0, num+1, 1):
# 	if( i == num ):
# 		array_x.append(RC_Filter_1[i][0])
# 		array_y.append(RC_Filter_1[i][1])
# 		temp_RC_cnt += 1
# 		for k in range(0, temp_RC_cnt, 1):
# 			temp_x = array_x.pop()	
# 			temp_y = array_y.pop()	
# 			if (k < 3):	
# 				temp_x2 += temp_x
# 				temp_y2 += temp_y
# 			else:	
# 				continue
# 				# print array_x.pop()
# 				# print array_y.pop()
# 				# print " ( " + str(array_x.pop()) + "," + str(array_y.pop()) + " ) "
# 		if (2 == temp_RC_cnt):
# 			temp_x2 = (temp_x2 / 2)
# 			temp_y2 = (temp_y2 / 2)
# 		else:
# 			temp_x2 = (temp_x2 / 3)
# 			temp_y2 = (temp_y2 / 3)
# 		print " ( " + str(temp_x2) + "," + str(temp_y2) + " ) "
# 		if( temp_x2 > 640 ):
# 			RC_Filter_2[RC_Filter_cnt][0] = temp_x2
# 			RC_Filter_2[RC_Filter_cnt][1] = temp_y2
# 			RC_Filter_cnt += 1

# 		temp_RC_cnt = 0
# 		temp_cnt = 0
# 		temp_x = 0
# 		temp_y = 0
# 		temp_x2 = 0
# 		temp_y2 = 0
# 		print " =========== "
# 	elif (RC_Filter_1[i + 1][1] == RC_Filter_1[i][1]):
# 		array_x.append(RC_Filter_1[i][0])
# 		array_y.append(RC_Filter_1[i][1])
# 		temp_RC_cnt += 1
# 	else:
# 		array_x.append(RC_Filter_1[i][0])
# 		array_y.append(RC_Filter_1[i][1])
# 		temp_RC_cnt += 1
# 		for k in range(0, temp_RC_cnt, 1):
# 			temp_x = array_x.pop()	
# 			temp_y = array_y.pop()	
# 			if (k < 3):	
# 				temp_x2 += temp_x
# 				temp_y2 += temp_y
# 			else:	
# 				continue
# 				# print array_x.pop()
# 				# print array_y.pop()
# 				# print " ( " + str(array_x.pop()) + "," + str(array_y.pop()) + " ) "
# 		if (2 == temp_RC_cnt):
# 			temp_x2 = (temp_x2 / 2)
# 			temp_y2 = (temp_y2 / 2)
# 		else:
# 			temp_x2 = (temp_x2 / 3)
# 			temp_y2 = (temp_y2 / 3)
# 		print " ( " + str(temp_x2) + "," + str(temp_y2) + " ) "
# 		if( temp_x2 > 640 ):
# 			RC_Filter_2[RC_Filter_cnt][0] = temp_x2
# 			RC_Filter_2[RC_Filter_cnt][1] = temp_y2
# 			RC_Filter_cnt += 1

# 		temp_RC_cnt = 0
# 		temp_cnt = 0
# 		temp_x = 0
# 		temp_y = 0
# 		temp_x2 = 0
# 		temp_y2 = 0
# 		print " =========== "

# for draw_i in range(0,num+1,1):
# 	cv2.circle(img2,(RC_Filter_2[draw_i][0],RC_Filter_2[draw_i][1]), 5, (255,0,0), -1)

# print RC_Filter_2

# cv2.imwrite("RFin_HoG_Predict_result.jpg",img2)		
# # plt.subplot(111), plt.plot(RC_Filter_2)	
# # plt.show()

# for draw_i in range(0,num,1):
# 	if( RC_Filter_2[draw_i+1][0] == 0):
# 		break
# 	else:
# 		cv2.line(img4,(RC_Filter_2[draw_i][0],RC_Filter_2[draw_i][1]),(RC_Filter_2[draw_i+1][0],RC_Filter_2[draw_i+1][1]),(0,0,255),5)
# 		cv2.line(img5,(RC_Filter_2[draw_i][0],RC_Filter_2[draw_i][1]),(RC_Filter_2[draw_i+1][0],RC_Filter_2[draw_i+1][1]),(0,0,255),5)
# cv2.imwrite("RFinal_left_result.jpg",img4)


# # @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 左側邊線 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 
# # 第一次濾波 濾除非連續點

# print LC
# LC_F1 = np.zeros((120,2),np.int16)
# num = 0
# loop_i = 1
# while( loop_i < 120) :
# 	if( 0 == (LC[loop_i][1]-LC[loop_i-1][1]) ):
# 		if( 32 == (LC[loop_i][0]-LC[loop_i-1][0]) ):			
# 			if( (LC[loop_i-1][0] == LC_F1[num][0])and(LC[loop_i-1][1] == LC_F1[num][1]) ):
# 				if( (0 == LC_F1[num][0])and(0 == LC_F1[num][1]) ):
# 					LC_F1[num][0] = LC[loop_i-1][0]
# 					LC_F1[num][1] = LC[loop_i-1][1]
# 					LC_F1[num+1][0] = LC[loop_i][0]
# 					LC_F1[num+1][1] = LC[loop_i][1]
# 					num += 1
# 				else:
# 					LC_F1[num+1][0] = LC[loop_i][0]
# 					LC_F1[num+1][1] = LC[loop_i][1]
# 					num += 1						
# 			elif( (0 != LC_F1[num][0])and(0 != LC_F1[num][1]) ):
# 				num += 1
# 				LC_F1[num][0] = LC[loop_i-1][0]
# 				LC_F1[num][1] = LC[loop_i-1][1]
# 				LC_F1[num+1][0] = LC[loop_i][0]
# 				LC_F1[num+1][1] = LC[loop_i][1]
# 				num += 1
# 			else:
# 				LC_F1[num][0] = LC[loop_i-1][0]
# 				LC_F1[num][1] = LC[loop_i-1][1]
# 				LC_F1[num+1][0] = LC[loop_i][0]
# 				LC_F1[num+1][1] = LC[loop_i][1]
# 				num += 1
# 			loop_i += 1	
# 		else:	
# 			loop_i += 1
# 	else:
# 		loop_i += 1


# LC_Filter_1 = np.zeros((num+1,2),np.int16)
# LC_Filter_2 = np.zeros((num+1,2),np.int16)

# for draw_i in range(0,num+1,1):
# 	LC_Filter_1[draw_i][0] = LC_F1[draw_i][0]
# 	LC_Filter_1[draw_i][1] = LC_F1[draw_i][1]
# 	cv2.circle(img,(LC_F1[draw_i][0],LC_F1[draw_i][1]), 5, (0,0,255), -1)

# print LC_Filter_1

# cv2.imwrite(str(k) + "_HoG_Predict_result.jpg",img)		
# # plt.subplot(111), plt.plot(LC_Filter_1)	
# # plt.show()

# # 第二次濾波 濾除連續點中，不可能之點
# LC_Filter_cnt = 0
# temp_LC_cnt = 0
# array_x = []
# array_y = []
# temp_cnt = 0
# temp_x = np.zeros(1, np.int16)
# temp_y = np.zeros(1, np.int16)
# temp_x2 = np.zeros(1, np.int16)
# temp_y2 = np.zeros(1, np.int16)

# for i in range(0, num+1, 1):
# 	if( i == num ):
# 		array_x.append(LC_Filter_1[i][0])
# 		array_y.append(LC_Filter_1[i][1])
# 		temp_LC_cnt += 1
# 		for k in range(0, temp_LC_cnt, 1):
# 			temp_x = array_x.pop()	
# 			temp_y = array_y.pop()	
# 			if (k < 3):	
# 				temp_x2 += temp_x
# 				temp_y2 += temp_y
# 			else:	
# 				continue
# 				# print array_x.pop()
# 				# print array_y.pop()
# 				# print " ( " + str(array_x.pop()) + "," + str(array_y.pop()) + " ) "
# 		if (2 == temp_LC_cnt):
# 			temp_x2 = (temp_x2 / 2)
# 			temp_y2 = (temp_y2 / 2)
# 		else:
# 			temp_x2 = (temp_x2 / 3)
# 			temp_y2 = (temp_y2 / 3)
# 		print " ( " + str(temp_x2) + "," + str(temp_y2) + " ) "
# 		if( temp_x2 < 640 ):
# 			LC_Filter_2[LC_Filter_cnt][0] = temp_x2
# 			LC_Filter_2[LC_Filter_cnt][1] = temp_y2
# 			LC_Filter_cnt += 1

# 		temp_LC_cnt = 0
# 		temp_cnt = 0
# 		temp_x = 0
# 		temp_y = 0
# 		temp_x2 = 0
# 		temp_y2 = 0
# 		print " =========== "
# 	elif (LC_Filter_1[i + 1][1] == LC_Filter_1[i][1]):
# 		array_x.append(LC_Filter_1[i][0])
# 		array_y.append(LC_Filter_1[i][1])
# 		temp_LC_cnt += 1
# 	else:
# 		array_x.append(LC_Filter_1[i][0])
# 		array_y.append(LC_Filter_1[i][1])
# 		temp_LC_cnt += 1
# 		for k in range(0, temp_LC_cnt, 1):
# 			temp_x = array_x.pop()	
# 			temp_y = array_y.pop()	
# 			if (k < 3):	
# 				temp_x2 += temp_x
# 				temp_y2 += temp_y
# 			else:	
# 				continue
# 				# print array_x.pop()
# 				# print array_y.pop()
# 				# print " ( " + str(array_x.pop()) + "," + str(array_y.pop()) + " ) "
# 		if (2 == temp_LC_cnt):
# 			temp_x2 = (temp_x2 / 2)
# 			temp_y2 = (temp_y2 / 2)
# 		else:
# 			temp_x2 = (temp_x2 / 3)
# 			temp_y2 = (temp_y2 / 3)
# 		print " ( " + str(temp_x2) + "," + str(temp_y2) + " ) "
# 		if( temp_x2 < 640 ):
# 			LC_Filter_2[LC_Filter_cnt][0] = temp_x2
# 			LC_Filter_2[LC_Filter_cnt][1] = temp_y2
# 			LC_Filter_cnt += 1

# 		temp_LC_cnt = 0
# 		temp_cnt = 0
# 		temp_x = 0
# 		temp_y = 0
# 		temp_x2 = 0
# 		temp_y2 = 0
# 		print " =========== "



# for draw_i in range(0,num+1,1):
# 	cv2.circle(img,(LC_Filter_2[draw_i][0],LC_Filter_2[draw_i][1]), 5, (255,0,0), -1)

# print LC_Filter_2

# cv2.imwrite("Fin_HoG_Predict_result.jpg",img)		
# # plt.subplot(111), plt.plot(LC_Filter_2)	
# # plt.show()

# for draw_i in range(0,num,1):
# 	if( LC_Filter_2[draw_i+1][0] == 0):
# 		break
# 	else:
# 		cv2.line(img3,(LC_Filter_2[draw_i][0],LC_Filter_2[draw_i][1]),(LC_Filter_2[draw_i+1][0],LC_Filter_2[draw_i+1][1]),(0,0,255),5)
# 		cv2.line(img5,(LC_Filter_2[draw_i][0],LC_Filter_2[draw_i][1]),(LC_Filter_2[draw_i+1][0],LC_Filter_2[draw_i+1][1]),(0,0,255),5)
# cv2.imwrite("Final_left_result.jpg",img3)	

# cv2.imwrite("result.jpg",img5)	
		
	

