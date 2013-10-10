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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class jPingpingThread extends Thread {

	jPingping gui;

	public jPingpingThread(jPingping in) {
	    gui = in;
	}
	
    public void run() {
        runPing();
    	
    }
    public void runPing() {
 	   try {	 
 	        String cmd = "";
 	   		String host = "98.130.148.213";  //hostname or IP to ping
 	   		String[] results = new String[60];
 	   		String[] secondaryResults = new String[120];  //***NEW
 	   		String fullResults = "";
 	   	    String eol = System.getProperty("line.separator");
 	   	    
 	   	    fullResults = fullResults + "Name: " + gui.nameField.getText() + eol;
 	   	    fullResults = fullResults + "Email: " + gui.emailField.getText() + eol;
 	   	    fullResults = fullResults + "IP Address: " + gui.ip + eol;
 	   	    
 	   		int loopCount = 0;
 	   		int timedoutCount = 0; //***NEW
 	   		
 	               if(System.getProperty("os.name").startsWith("Windows")) {
 	                           // For Windows
 	                           cmd = "ping -n 10 " + host;
 	                           gui.out_console.append("Windows OS detected, beginning ping test to remote host." + eol);	
 	                           gui.out_console.append("''" + cmd + "''" + eol);
 	               } else {
 	                           // For Linux and OSX
 	                           cmd = "ping -c 10 " + host;
 	                          gui.out_console.append("Mac OSX or Linux OS detected, beginning ping test to remote host." + eol);
 	                           gui.out_console.append(cmd + eol);
 	               }

 	            Process runPing = Runtime.getRuntime().exec(cmd);
 	   			BufferedReader reader = new BufferedReader(new InputStreamReader(runPing.getInputStream()));
 	   			String lineRead = null;
 	   			while((lineRead = reader.readLine()) != null)
 	   			{
 	   			   results[loopCount]=lineRead;
 	   			   if (lineRead.contains("timed out")) {
 	   				   timedoutCount++;
 	   			   }
 	   			   loopCount++;
 	   			   gui.out_console.append(lineRead + "\n");
 	   			}
 	   			
 	   			//***NEW
 	   			if (timedoutCount >= 3) {
 	   				loopCount = 0;
 	   				gui.out_console.append("Abnormal time out errors detected, running secondary test suite...\n");
  	                if(System.getProperty("os.name").startsWith("Windows")) {
                        // For Windows
                        cmd = "ipconfig /all ";
                        gui.out_console.append(eol + "Checking network interfaces." + eol);	
                        gui.out_console.append("''" + cmd + "''" + eol);
  	                } else {
                        // For Linux and OSX
                        cmd = "ifconfig -a ";
                        gui.out_console.append(eol + "Checking network interfaces." + eol);
                        gui.out_console.append(cmd + eol);
  	                }
  	 	            runPing = Runtime.getRuntime().exec(cmd);
  	 	   			BufferedReader reader2 = new BufferedReader(new InputStreamReader(runPing.getInputStream()));
  	 	   			String lineRead2 = null;
  	 	   			while((lineRead2 = reader2.readLine()) != null)
  	 	   			{
  	 	   			   secondaryResults[loopCount]=lineRead2;
  	 	   			   loopCount++;
  	 	   			   gui.out_console.append(lineRead2 + "\n");
  	 	   			}

  	                if(System.getProperty("os.name").startsWith("Windows")) {
                        // For Windows
                        cmd = "tracert " + host;
                        gui.out_console.append(eol + "Running trace route to remote host." + eol);	
                        gui.out_console.append("''" + cmd + "''" + eol);
  	                } else {
                        // For Linux and OSX
                        cmd = "traceroute " + host;
                        gui.out_console.append(eol + "Running trace route to remote host." + eol);
                        gui.out_console.append(cmd + eol);
  	                }
  	 	            runPing = Runtime.getRuntime().exec(cmd);
  	 	   			BufferedReader reader3 = new BufferedReader(new InputStreamReader(runPing.getInputStream()));
  	 	   			String lineRead3 = null;
  	 	   			while((lineRead3 = reader3.readLine()) != null)
  	 	   			{
  	 	   			   secondaryResults[loopCount]=lineRead3;
  	 	   			   loopCount++;
  	 	   			   gui.out_console.append(lineRead3 + "\n");
  	 	   			}  	 	   			
  	               
  	               
 	   				
 	   			}//****NEW end.
 	   			
 	   			runPing.waitFor();

 	               if(runPing.exitValue() == 0) {

 	            	   		gui.out_console.append("Test successful!" + "\n");
 	               } else {

 	            	   		gui.out_console.append("Test was not successful, problems encountered." + "\n");
 	               }		
 	               
 	               Properties props = System.getProperties();
 	               props.put("mail.smtp.host","SMTP-MAIL-HOST");
 	               props.put("mail.smtp.port","SMTP-PORT");
 	               props.put("mail.smtp.auth","true-false");  //smtp authentication

 	               Authenticator auth = new Authenticator() {
 	                 public PasswordAuthentication getPasswordAuthentication() {
 	                   return new PasswordAuthentication("email@address.com","password");  //smtp login & password
 	                 }
 	               };

 	               int size = results.length;
 	               int size2 = secondaryResults.length;
 	               for (int i=0; i<size; i++)
 	               {
 	               	if(results[i]!=null){
 	               	fullResults = fullResults + eol + results[i];
 	               	}
 	               } 
 	               
 	               for (int u=0; u<size2; u++)
 	               {
 	               	if(secondaryResults[u]!=null){
 	               	fullResults = fullResults + eol + secondaryResults[u];
 	               	}
 	               }
 	               
 	               sendMail(props,auth,new InternetAddress("test@nnovationlabs.com"),"recipient@email.com","Ping Results",fullResults,"text/plain");            
 	                    
 	   		 
 	         }catch(Exception e)
 	         {
 	            e.printStackTrace();
 	           gui.out_console.append(e + "\n");
 	         }     	   
    }
    
	   public void sendMail(Properties props, 
         Authenticator authenticator, 
         InternetAddress fromAddress, 
         String recipients, 
         String subject, 
         String content, 
         String contentType) {
		 gui.out_console.append("\nTest completed, results have been emailed. \n");
		try {

			Session session = Session.getDefaultInstance(props, authenticator);

			Message message = new MimeMessage(session);

			message.setFrom(fromAddress);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients, false));

			message.setSubject(subject);
			message.setContent(content,contentType);
			message.setSentDate(new Date());

			Transport.send(message);


				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}

	}//sendmail
	
	
}
