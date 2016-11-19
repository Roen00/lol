package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 
public class Server {
	private final int port = 3000;

	private Map<String, HashSet<Service>> data = new LinkedHashMap<String, HashSet<Service>>();
	private List<String> listOfConnectedClients = new ArrayList<String>();
	
	
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		ServerSocket serverSocket = new ServerSocket(server.port);
		
		try {
			while (true) {
				Connection connection = server.new Connection(serverSocket.accept());
				connection.start();
			}
		} 
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		serverSocket.close();
	}
	
	public Server(){
		System.out.println("Uruchomiono serwer.");
	}
		
	
	private class Connection extends Thread {
		private Socket socket;
		private String actualClient;
		private ObjectOutputStream serverObjectOutputStream;
		private ObjectInputStream serverObjectInputStream;
		
		public Connection(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				System.out.println("Nowy klient polaczyl sie z serwerem.");
				serverObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				serverObjectInputStream = new ObjectInputStream(socket.getInputStream());
				
				//Getting name from user
				while(true){
					serverObjectOutputStream.writeObject("Podaj nazwe uzytkownika");
					if(getNameFromClient()){
						break;
					}
				}
				
				
				//Waiting for client's input to server
				while(true){
					String inputLine = (String)serverObjectInputStream.readObject();
					
					if (inputLine.startsWith("close")){
						System.out.println("Zamykam polaczenie z klientem " + actualClient);
						listOfConnectedClients.remove(actualClient);
						break;
					}
					else if(inputLine.startsWith("new")) {
						while(true){
							Service service;
							if ((service = (Service)serverObjectInputStream.readObject()) == null){
								serverObjectOutputStream.writeObject("error");
								break;
							}
							synchronized(data){
								if (!data.get(actualClient).contains(service)){
									data.get(actualClient).add(service);
					
									for (Map.Entry<String, HashSet<Service>> entry : data.entrySet()) {
									    System.out.println("\n" + entry.getKey() + " : ");
									    HashSet<Service> temp = entry.getValue();
									    for (Service s : temp) {
									        System.out.print(s.print());
									    }
									}
									serverObjectOutputStream.writeObject("accepted");
									break;
								}
								else {
									serverObjectOutputStream.writeObject("error");
								}
							}
						}
					}
					else if(inputLine.startsWith("withdrawn")) {
						serverObjectOutputStream.writeObject(data.get(actualClient));
						String serviceToWithdraw;
						while(true){
							if ((serviceToWithdraw = (String)serverObjectInputStream.readObject()) == null){
								serverObjectOutputStream.writeObject("error");
								break;
							}
							synchronized(data){
								deleteFromHashSet(serviceToWithdraw);
							    HashSet<Service> temp = data.get(actualClient);
							    for (Service s : temp) {
							        System.out.print(s.print());
							    }
								
								if (!data.get(actualClient).contains(serviceToWithdraw)){
									serverObjectOutputStream.writeObject("accepted");
									break;
								}
								else {
									serverObjectOutputStream.writeObject("error");
								}
							}
						}
					}
					else if(inputLine.startsWith("show")) {
						serverObjectOutputStream.writeObject(data);
						while(true){
							if (((String)serverObjectInputStream.readObject()).equals("ok")){
								serverObjectOutputStream.writeObject("accepted");
								break;
							}
							else {
								serverObjectOutputStream.writeObject("error");
							}
						}
					}
					else if(inputLine.startsWith("reserve")) {
						
					}
					else {
						
					}
				}
				
				
				socket.close();
			}
			catch (IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		
		private void setActualClientName(String name){
			this.actualClient = name;
		}
		
		private boolean getNameFromClient() throws IOException, ClassNotFoundException{
			String nameOfClient = (String)serverObjectInputStream.readObject();
			if (nameOfClient == null){
				serverObjectOutputStream.writeObject("error");
				return false;
			}
			synchronized(data){
				if (!listOfConnectedClients.contains(nameOfClient)){
					setActualClientName(nameOfClient);
					listOfConnectedClients.add(nameOfClient);
					if(!data.containsKey(nameOfClient)){
						data.put(actualClient, new HashSet<Service>());
					}
					
					serverObjectOutputStream.writeObject("accepted");
					return true;
				}
				else {
					serverObjectOutputStream.writeObject("error");
					return false;
				}
			}
		}
		
		private void deleteFromHashSet(String serviceToDelete){
			Service tempService = new Service(serviceToDelete);
			Iterator<Service> it = data.get(actualClient).iterator();
			while(it.hasNext()){
				if(it.next().equals(tempService)){
					it.remove();
				}
			}
		}
		
	}
}