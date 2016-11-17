package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
 
public class Client{
 
	public static void main(String[] args) throws IOException{
		String hostName = "localhost";
		Socket echoSocket = new Socket(hostName, 9890);
		
		PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Polaczono z serverem: " + hostName);
	
		 String userInput;
	     while ((userInput = stdIn.readLine()) != null) {
	         out.println(userInput);
	         System.out.println("echo: " + in.readLine());
	     }
			
	     echoSocket.close();
	
	     try {
	    	 Thread.sleep(1000);
	     } catch (InterruptedException e) {
	    	 // TODO Auto-generated catch block
	    	 e.printStackTrace();
	     }
	}
}
