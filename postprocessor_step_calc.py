__author__ = 'andersonpaac'
import numpy as np

filename="/Users/andersonpaac/Downloads/question_2.csv"   #PLEASE INSERT FULL PATH TO FILENAME HERE


timestamp=[]
acc_data=[]
gyro_data=[]
mag_data=[]
light_data=[]
running = []
threshold=1.4
timediff=1000
ooind=10

def parser():
    fd = open(filename)
    data = fd.read()
    raw_data = data.split(",")
    raw_data = raw_data[10:-1]
    for i in xrange(len(raw_data)):
        a = raw_data[i].split("\n")
        if len(a)>1:
            raw_data[i] = a[1]
        else:
            raw_data[i] = a[0]

    for i in range(len(raw_data)):
        #print raw_data[i]
        raw_data[i] = float(raw_data[i])

    ts_ind =0
    a_ind = 1
    notdone=1
    while(notdone):
        acc_data.append(DataClass(raw_data[ts_ind],raw_data[a_ind],raw_data[a_ind+1],raw_data[a_ind+2]))
        ts_ind = ts_ind + ooind
        a_ind = a_ind + ooind
        if(ts_ind>=len(raw_data)-1):
            notdone=0
    calc()




def calc():
    #isRunning=0
    sum=0
    unset=1
    idx=0
    somearr=[]

    print acc_data[2].get()
    t_lval = acc_data[0].gett()+timediff
    while unset:
        if(acc_data[idx].gett()<=t_lval):
            somearr.append(acc_data[idx].gety())

        if(acc_data[idx].gett()>=t_lval):
            distance=max(somearr)-min(somearr)
            #print distance

            if (distance>threshold):# && (somearr[26] >  ):
                #if(isRunning==0):
                running.append(t_lval-timediff)#acc_data[idx_lval-timediff].gett())
                running.append(t_lval)#acc_data[idx_lval].gett())

            somearr=[]
            t_lval += timediff

        idx=idx+1
        if(idx >= len(acc_data)):
            unset=0


    if len(running) % 2 != 0:
        running.append(acc_data[len(acc_data)-1].gett())


    print len(running)
    print running

    notdone=1
    idx=0
    while notdone:
        sum=sum+(running[idx+1]-running[idx])
        idx=idx+2
        if(idx>=len(running)):
            notdone=0

    print sum
    print sum/1000
    steps = (sum*1.667)/1000
    print "MOVED :"+str(steps)+" steps"
   


class DataClass:
    def __init__(self, t , x , y, z ):
        self.sensor_x = x
        self.sensor_y = y
        self.sensor_z = z
        self.sensor_t = t
    def getx(self):
        return self.sensor_x
    def gety(self):
        return self.sensor_y
    def getz(self):
        return self.sensor_z
    def gett(self):
        return self.sensor_t
    def get(self):
        return self.sensor_t,self.sensor_x,self.sensor_y,self.sensor_z


parser()
