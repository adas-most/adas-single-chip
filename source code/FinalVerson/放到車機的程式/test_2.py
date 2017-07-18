import commands
test = 3 
a,b = commands.getstatusoutput('echo '+str(test))
test = test + 1
#print a
print b

a,b = commands.getstatusoutput('echo '+str(test))
test = test + 1
#print a
print b