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

print "File Name                                   : %s"%(sys.argv[0])
print "Feature Parameter                        P1 : %s"%(sys.argv[1])
print "Machine Learnning Parameter              P2 : %s"%(sys.argv[2])
print "Feature Source Parameter                 P3 : %s"%(sys.argv[3])
print "Weather Condition Parameter              P4 : %s"%(sys.argv[4])
print "Daylight/Night Condition Parameter       P5 : %s"%(sys.argv[5])
print "Car Intensive Rate Condition Parameter   P6 : %s"%(sys.argv[6])
print "Position Condition Parameter             P7 : %s"%(sys.argv[7])
aaa = raw_input("starting")

# print 'parameter num : ', len(sys.argv)
# print 'parameter : ', str(sys.argv)




# case_rain()	
# print "========="
# case_rain_no()
# print "========="
# case_sky()
# print "========="
# case_sky_no()