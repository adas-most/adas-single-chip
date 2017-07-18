# -*- coding: UTF-8 -*-
import time
import serial
import sys
import threading    #導入 threading 模組
import thread
import commands
import sys

class MyThread(threading.Thread):
    def __init__(self,flag,ser,fd,event_id):
        threading.Thread.__init__(self)
        self.a = True
        self.flag = flag
        self.ser = ser
        self.fd = fd
	self.event_id = event_id
    def run(self):
        self.Warnning_Beep(self.flag)

    def Warnning_Beep(self,flag):
        while self.a:
            if( 1 == flag ):
                out = ''
                # let's wait one second before reading output (let's give device time to answer)
                time.sleep(0.5)
                while self.ser.inWaiting() > 0:
                    out += self.ser.read(1)
                if out != '':
                    print " output >> \r\n" + out
                    buf_len =  len(out)
                    if ((29 == buf_len) and (1 == self.fd)):
                        print "================== Info =================="
                        # print " Temperature: " + str(out[1:4])
                        # print " RPM        : " + str(out[6:10])
                        # print " Speed      : " + str(out[11:13])
                        # print " O2-1       : " + str(out[15:17])
                        # print " O2-2       : " + str(out[18:20])
                        # print " MAF        : " + str(out[22:28])
                        print " Temperature: %03s" %(out[1:4])
                        print " RPM        : %05s" %(out[5:10])
                        print " Speed      : %03s" %(out[11:14])
                        print " O2-1       : %03s" %(out[15:18])
                        print " O2-2       : %03s" %(out[18:21])
                        print " MAF        : %05s" %(out[22:28])
                        print "================== Info =================="
			f = file("OBD2.txt","w")
			f.write("radiatortemperature:%03s\r\n" %(out[1:4]))
			f.write("enginerpm:%05s\r\n" %(out[5:10]))
			f.write("speed:%03s\r\n" %(out[11:14]))
			f.write("o2sensor:%06s\r\n" %(out[15:21]))
			f.write("airflowmeter:%05s\r\n" %(out[22:28]))
			f.close()
                        self.fd = 0
			print self.event_id			
			a,b = commands.getstatusoutput('java -jar updateData.jar ' + str(self.event_id))			
			self.event_id = self.event_id + 1
			print a
			print b
                        

    def stop(self):
        self.a = False

# $064,00961,250,012000,11470
#================== Info ==================
# Temperature: 080
# RPM        : 09297
# Speed      : 140
# O2-1       : 068
# O2-2       : 000
# MAF        : 31980
#================== Info ==================
# ser = serial.Serial('COM4',38400,timeout=1)

para1 = int(sys.argv[1])

fd = 1
event_id = para1

ser = serial.Serial('/dev/ttyUSB0',38400,timeout=1)

print "Setup OBD-II , Please waitting."
ser.write('@OBD' + '\r\n')
time.sleep(3)

OneThreadFlag = 1
t1 = MyThread(OneThreadFlag,ser,fd,event_id)
t1.start()
time.sleep(2)
ser.write('@STOP' + '\r\n')
time.sleep(2)
ser.write('@OFF' + '\r\n')
time.sleep(2)
t1.stop()

# configure the serial connections (the parameters differs on the device you are connecting to)
#ser = serial.Serial(
#    port='COM4',
#    baudrate=38400,
#    parity=serial.PARITY_ODD,
#    stopbits=serial.STOPBITS_TWO,
#    bytesize=serial.SEVENBITS
#)



# while 1 :
#     # get keyboard input
#     input = raw_input(" input >> ")
#         # Python 3 users
#         # input = input(">> ")
#     if input == 'exit':
#         ser.close()
#         exit()
#     else:
#         # send the character to the device
#         # (note that I happend a \r\n carriage return and line feed to the characters - this is requested by my device)
#         ser.write(input + '\r\n')
#         out = ''
#         # let's wait one second before reading output (let's give device time to answer)
#         time.sleep(1)
#         while ser.inWaiting() > 0:
#             out += ser.read(1)

#         if out != '':
#             print " output >> " + out