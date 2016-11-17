package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
 
public class Client{
	private int port;
	private String hostName;
	
	//Constructors
	public Client(){	
	}
	
	public Client(int port){
		this.port = port;
		this.hostName = "localhost";
	}
	
	public Client(String hostName){
		this.port = 3000;
		this.hostName = hostName;
	}
	
	public Client(int port, String hostName){
		this.port = port;
		this.hostName = hostName;
	}
	
	public static void main(String[] args) throws IOException{
		Client client = new Client(3000, "localhost");
		Socket clientSocket = new Socket(client.hostName, client.port);
		
		
		PrintWriter clientOutputStream = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader clientInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		
		//Sending name of client
		while(true){
			String line = clientInputStream.readLine();
			if (line.startsWith("Podaj")){
				clientOutputStream.println(client.getClientName());
			}
			else if (line.toLowerCase().startsWith("error")){
				System.out.println("Blad, jeszcze raz");
			}
			else if (line.toLowerCase().startsWith("accepted")){
				System.out.println("Zaakceptowano");
				break;
			}
		}
		
		
		//Decision from client
		while(true){
			//Sending information to server that connection is ready
			clientOutputStream.println("ready");
			for (int i = 0; i < 6; i++){
				System.out.println(clientInputStream.readLine());
			}
			String userDecision = stdIn.readLine();
			clientOutputStream.println(userDecision);
			break;
		}
		
		
//		while ((userInput = stdIn.readLine()) != null) {
//			out.println(userInput);
//			System.out.println("Server: " + in.readLine());
//		}
			
		clientSocket.close();
	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	public String getHostName(){
		return this.hostName;
	}
	
	public String getClientName() throws IOException{
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Podaj nazwe uzytkownika: ");
		return stdIn.readLine();
	}
	
}
