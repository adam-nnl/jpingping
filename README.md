***jpingping***
=========

***jPingPing utility***

Small java utility applet that makes use of the JNI (Java Native Interface) to make native OS (Operating System) calls 
for the purpose of gathering network information and statistics from a client-machine to a host without any interaction 
from the end-user (aside from installing Java™).

***Purpose***

It allows for the assurance of client side communication fidelity and latency to a remote server, very handy in many cases 
of network or website diagnostics.

***Demo***

Demo of applet in action can be found here:
http://nnovationlabs.com/projects/jPingping/jpingping_project.html

***usage***

The jPingPing package consists of two classes, jPingping.java which contains the main method and GUI code. and jPingpingThread
which is the background processing thread where the magic happens. All of the settings for which host to ping and email settings
for the results are in the jPingpingThread.java file and should be changed prior to deployment
