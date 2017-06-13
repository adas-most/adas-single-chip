# -*- coding: UTF-8 -*-
import cv2
import numpy as np
import io
import my_lib
from matplotlib import pyplot as plt
import math
# x=float('nan')
# print math.isnan(x)

SampleNum = 100
SampleType = 3
SampleSize = (32,32)

ImgCnt = (SampleNum*SampleType)
Image_data = np.zeros((ImgCnt,SampleSize[0],SampleSize[1]),np.float32)

for i in range(0,SampleNum,1):
	if( i < 10 ):
		img = cv2.imread( "Highway_Trainning/others/sample_0" + str(i) + ".jpeg",0)
		# print "trt1"
	else:
		img = cv2.imread( "Highway_Trainning/others/sample_" + str(i) + ".jpeg",0)
	Image_data[i] = img
for i in range(0,SampleNum,1):
	if( i < 10 ):
		img = cv2.imread( "Highway_Trainning/left/sample_0" + str(i) + ".jpeg",0)
		# print "trt2"
	else:
		img = cv2.imread( "Highway_Trainning/left/sample_" + str(i) + ".jpeg",0)
	Image_data[(SampleNum*(SampleType-2)) + i] = img
for i in range(0,SampleNum,1):
	if( i < 10 ):
		img = cv2.imread( "Highway_Trainning/right/sample_0" + str(i) + ".jpeg",0)
		# print "trt3" + str(i)
	else:
		img = cv2.imread( "Highway_Trainning/right/sample_" + str(i) + ".jpeg",0)
	Image_data[(SampleNum*(SampleType-1)) + i] = img

cv2.imwrite("Image_data[50].jpg",Image_data[50])	
cv2.imwrite("Image_data[150].jpg",Image_data[150])
cv2.imwrite("Image_data[200].jpg",Image_data[250])
print Image_data.shape

print " Image 3 type is already. "


feature_len = Image_data.shape[1]*Image_data.shape[2]
data = np.zeros((ImgCnt,feature_len),np.float32)
his = np.zeros((ImgCnt,8),np.float32)

unitLBP = my_lib.ImgOperations("test.jpg","test.jpg")


for LBPLoop in range(0,ImgCnt,1):
# for LBPLoop in range(0,1,1):
	
	NanCheck = unitLBP.DataToImg(Image_data[LBPLoop],"transferData.jpg")
	if( 1 == NanCheck ):
		print 0
	unitLBP.LBP_Transform("transferData.jpg","LBP[0].jpg",0)
	# print unitLBP.LBP_Matrix

	data_cnt = 0
	for y in range(0,unitLBP.LBP_Matrix.shape[0],1):
		for x in range(0,unitLBP.LBP_Matrix.shape[1],1):			
			data[LBPLoop][data_cnt] = unitLBP.LBP_Matrix[y][x]
			data_cnt = data_cnt + 1
	# print data[LBPLoop]
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
	# data_cnt = 0
	# for z in range(0,his.shape[0],1):
	# 	for y in range(0,his.shape[1],1):			
	# 		data[HogLoop][data_cnt] = unit.Block_MagMatrix[z][y]
	# 		data_cnt = data_cnt + 1
	# print data

print data		
# plt.subplot(121), plt.imshow(unitLBP.LBP_Matrix,'gray')	
# plt.subplot(122), plt.plot(his)
# plt.show()	

responses = np.float32(np.repeat(np.arange(SampleType),SampleNum)[:,np.newaxis])
svm_params = dict( kernel_type = cv2.SVM_LINEAR,svm_type = cv2.SVM_C_SVC,C=2.67, gamma=5.383 )
svm = cv2.SVM()
svm.train(data,responses, params=svm_params)
svm.save('LBP0_3type_HW.dat')
print " SVM is done . "



