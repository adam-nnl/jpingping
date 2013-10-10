/*
    Copyright 2013 Adam Lara

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import java.applet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;
 
 public class jPingping extends Applet implements ActionListener{
 
	 StringBuffer buffer = new StringBuffer();
	 String ip;
	 int port;
	 JLabel dirLabel = new JLabel("<html>Enter your information below and the click the 'Run Test' button to begin.<br>You will see the output of the test in the console box below. Once the<br> test is complete the results will be emailed.</html>");
	 Label nameLabel = new Label("Enter Your Name");
	 TextField nameField = new TextField("your name", 36);
	 Label emailLabel = new Label("Enter your email");
	 TextField emailField = new TextField("your email", 36);
	 JButton goButton = new JButton("Run Test"); 
	 TextArea out_console = new TextArea(22,56);
	 jPingpingThread pt = new jPingpingThread(this);


     public void init() {
       FlowLayout experimentLayout = new FlowLayout();
       setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
       setLayout(experimentLayout);
       resize(425,494);
   	   repaint();
   	   add(dirLabel);
   	   add(nameLabel);
   	   add(nameField);
   	   add(emailLabel);
   	   add(emailField);
   	   add(goButton);
       add(out_console);
       goButton.addActionListener(this);
       out_console.append("Initializing applet.....\n");
       repaint();
       out_console.append("Applet intialized.....\n");
 	   try{
 	       	Socket s = new Socket("nnovationlabs.com", 80);
 	       	InetAddress ipraw = s.getLocalAddress().getLocalHost();
 	       	ip = ipraw.toString();
 	       	s.close();
 	        }catch(IOException io){
 	        	out_console.append("Problem fetching client IP. Error: " + io.getMessage() + "\n");
 		    }      
       


     }//init
       
     public void start() {   

     }//start


	public void actionPerformed(ActionEvent e) {
	     if (e.getSource() == goButton) {
	    	 String nameValue = nameField.getText();
	    	 String emailValue = emailField.getText();
	    	 if(nameValue.equals(" ") || nameValue.equals("your name")) {
	    		 out_console.append("Enter a valid value in the 'name' field please.\n\n");
	    	 }
	    	 else {//if name checks out
	    	 	if(emailValue.equals(" ") || emailValue.equals("your email")) {//nested if to check email
	    	 		out_console.append("Enter a valid value in the 'email' field please.\n\n");
	    	 	}
	    	 	else {//if email and name checks out
	    	 		out_console.append("Begining ping tests.....\n");
	    	 		out_console.append("Client(your) IP address: " + ip + "\n");
	    	 		pt.start();
	    	 	}	
	    	 }	
	    	 	
	       }//if button action
		
	}
 
      
   }//jPingping class
