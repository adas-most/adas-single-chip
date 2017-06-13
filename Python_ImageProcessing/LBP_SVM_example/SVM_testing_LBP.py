# -*- coding: UTF-8 -*-
import cv2
import numpy as np
import io
import my_lib
from matplotlib import pyplot as plt

ImgCnt = 10
SampleSize = (32,32)
Image_data = np.zeros((ImgCnt,SampleSize[0],SampleSize[1]),np.float32)

for temp_i in range(0,ImgCnt,1):
	img = cv2.imread("test_" + str(temp_i) + ".jpeg",0)
	Image_data[temp_i] = img

cv2.imwrite("Image_data[0].jpg",Image_data[0])	
# cv2.imwrite("Image_data[1].jpg",Image_data[1])
print Image_data.shape


feature_len = Image_data.shape[1]*Image_data.shape[2]
data = np.zeros((ImgCnt,feature_len),np.float32)
his = np.zeros((ImgCnt,8),np.float32)
unitLBP = my_lib.ImgOperations("test.jpg","test.jpg")

for LBPLoop in range(0,ImgCnt,1):
	NanCheck = unitLBP.DataToImg(Image_data[LBPLoop],"transferData.jpg")
	if( 1 == NanCheck ):
		print 0
	unitLBP.LBP_Transform("transferData.jpg","LBP[0].jpg",0)
	data_cnt = 0
	for y in range(0,unitLBP.LBP_Matrix.shape[0],1):
		for x in range(0,unitLBP.LBP_Matrix.shape[1],1):			
			data[LBPLoop][data_cnt] = unitLBP.LBP_Matrix[y][x]
			data_cnt = data_cnt + 1

	# for y in range(0,unitLBP.LBP_Matrix.shape[0],1):
	# 	for x in range(0,unitLBP.LBP_Matrix.shape[1],1):
	# 		if( 1 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][0] += 1				
	# 		elif( 2 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][1] += 1
	# 		elif( 4 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][2] += 1
	# 		elif( 8 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][3] += 1
	# 		elif( 16 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][4] += 1
	# 		elif( 32 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][5] += 1
	# 		elif( 64 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][6] += 1
	# 		elif( 128 == unitLBP.LBP_Matrix[y][x] ):
	# 			his[LBPLoop][7] += 1
print data	

svm = cv2.SVM()
svm.load('LBP0_3type_HW.dat')
result = svm.predict_all(data)
print result
