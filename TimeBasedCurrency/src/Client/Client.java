package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Server.ServiceProtocol;

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
	
	public static void main(String[] args) throws IOException, NumberFormatException, ClassNotFoundException{
		Client client = new Client(3000, "localhost");
		Socket clientSocket = new Socket(client.hostName, client.port);

		ObjectOutputStream clientObjectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream clientObjectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		//Sending name of client
		while(true){
			String line = (String)clientObjectInputStream.readObject();
			if (line.toLowerCase().startsWith("podaj")){
				clientObjectOutputStream.writeObject(client.getClientName());
			}
			else if (line.toLowerCase().startsWith("error")){
				System.out.println("Uzytkownik o takiej nazwie jest juz podlaczony.");
			}
			else if (line.toLowerCase().startsWith("accepted")){
				System.out.println("Zaakceptowano nazwe uzytkownika.");
				break;
			}
		}

		//Showing options to client
		ServiceProtocol serviceProtocol = new ServiceProtocol(clientObjectOutputStream, clientObjectInputStream);

		while(true){
			//Processing decision from client
			serviceProtocol.showOptions();
			String decision = stdIn.readLine();
			if(serviceProtocol.processDecision(Integer.parseInt(decision))){
				break;
			}
		}

		
		clientSocket.close();
	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
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
