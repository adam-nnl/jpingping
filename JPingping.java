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

package jpingping;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*; 
import javax.mail.*;
import javax.mail.internet.*;


public class JPingping extends Applet {
 
     StringBuffer buffer = new StringBuffer();
	 
       public void init() {
 
       resize(320,400);
       add(new Label("initializing..."));
   	   repaint();
   	   
       }//init
       
       public void start() {   

       runPing();


       }//start
 
       public void paint(Graphics g) {
 
 
       }//paint
       
       public void runPing() {
       	   add(new Label("Now running ping test..."));
       	   repaint();
    	   try {	 
    	        String cmd = "";
    	   		String host = "127.0.0.1";
    	   		String[] results = new String[20];
    	   		String fullResults = "";
    	   	    String eol = System.getProperty("line.separator");  
    	   	    
    	   		int loopCount = 0;

    	               if(System.getProperty("os.name").startsWith("Windows")) {
    	                           // For Windows
    	                           cmd = "ping -n 10 " + host;
    	               } else {
    	                           // For Linux and OSX
    	                           cmd = "ping -c 10 " + host;
    	               }

                        Process runPing = Runtime.getRuntime().exec(cmd);
    	   			BufferedReader reader = new BufferedReader(new InputStreamReader(runPing.getInputStream()));
    	   			String lineRead = null;
    	   			while((lineRead = reader.readLine()) != null)
    	   			{
    	   			   results[loopCount]=lineRead;
    	   			   loopCount++;
    	   			   //System.out.println(lineRead); //You can send output from here to a GUI display if needed
    	   			}
    	   			
    	               runPing.waitFor();

    	               if(runPing.exitValue() == 0) {
                            //System.out.println("Test successful!");
    	                    //return true;
    	               } else {
                            //System.out.println("Test was not successful, problems encountered.");
    	                    //return false;
    	               }		
    	               
    	               Properties props = System.getProperties();
    	               props.put("mail.smtp.host","MAIL_HOST_HERE");
    	               props.put("mail.smtp.port","111");
    	               props.put("mail.smtp.auth","true"); //true if smtp authentication required

    	               Authenticator auth = new Authenticator() {
    	                 public PasswordAuthentication getPasswordAuthentication() {
    	                   return new PasswordAuthentication("emailOrLogin","password");
    	                 }
    	               };

    	               int size = results.length;
    	               for (int i=0; i<size; i++)
    	               {
    	               	if(results[i]!=null){
    	               	fullResults = fullResults + eol + results[i];
    	               	}
    	               }             
    	               
    	               sendMail(props,auth,new InternetAddress("ENTER_SENDER_EMAIL"),"ENTER_RECIPIENT@EMAIL.com","ENTER_SUBJECT_LINE",fullResults,"text/plain");            
    	                    
    	   		 
    	         }catch(Exception e)
    	         {
    	            e.printStackTrace();
    	         }     	   
       }
       
   	   public void sendMail(Properties props, 
            Authenticator authenticator, 
            InternetAddress fromAddress, 
            String recipients, 
            String subject, 
            String content, 
            String contentType) {
          	add(new Label("Test completed, results have been emailed. "));
           	repaint();
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
