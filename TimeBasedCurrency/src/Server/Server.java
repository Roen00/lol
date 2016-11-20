package Server;

<<<<<<< HEAD
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
									ServiceUtility.printHashMap(data);
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
						serverObjectOutputStream.reset();
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
								ServiceUtility.printHashSet(temp);
								
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
						serverObjectOutputStream.reset();
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
						Map<String, HashSet<Service>> tempData = new LinkedHashMap<String, HashSet<Service>>();
						
						for (Map.Entry<String, HashSet<Service>> entry : data.entrySet()) {
							String key = entry.getKey();
							if (!actualClient.equals(key)){
								HashSet<Service> value = entry.getValue();
								tempData.put(key, value);
							}
						}
						
						serverObjectOutputStream.reset();
						serverObjectOutputStream.writeObject(tempData);
						
						String serviceToReserve;
						while(true){
							if ((serviceToReserve = (String)serverObjectInputStream.readObject()) == null){
								serverObjectOutputStream.writeObject("error");
								break;
							}
							synchronized(data){
								if (reserveService(serviceToReserve)){
									serverObjectOutputStream.writeObject("accepted");
									break;
								}
								else {
									serverObjectOutputStream.writeObject("error");
								}
							}
						}
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
		
		private boolean reserveService(String serviceToReserve){
			Service serviceToReserveExample = new Service(serviceToReserve);
			
			for (Map.Entry<String, HashSet<Service>> entry : data.entrySet()) {
				//String key = entry.getKey();
				HashSet<Service> value = entry.getValue();
				Iterator<Service> it = value.iterator();
				while(it.hasNext()){
					Service tempService = it.next();
					if(tempService.equals(serviceToReserveExample)){
						tempService.setStatusOfService(ServiceUtility.RESERVED_SERVICE);
						return true;
					}
				}
			}
			return false;
		}
		
	}
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
 
public class Server {
	private final int port = 3000;
	//private int numberOfClientsConnected = 0;
	private PrintWriter serverOutputStream;
	private BufferedReader serverInputStream;
	
	private Map<String, HashSet<Service>> data = new LinkedHashMap<String, HashSet<Service>>();
	private HashSet<Service> serviceSet = new HashSet<Service>();
	
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
		private int decision;
		private String actualClient;
		
		public Connection(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				System.out.println("Nowy klient polaczyl sie z serwerem.");
				serverOutputStream = new PrintWriter(socket.getOutputStream(), true);
				serverInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				
				ObjectOutputStream a = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream b = new ObjectInputStream(socket.getInputStream());
				
				//Getting name from user
				while(true){
					String name;
					if((name = getNameFromClient()) != null){
						setActualClientName(name);
						break;
					}
				}
				
				//Sending to client information about possible options
				while (true){
					if((serverInputStream.readLine().toLowerCase().startsWith("ready"))) { 
						showOptions(serverOutputStream);
						break;
					}
				}
				
				//Get decision and processs it
				while(true){
					decision = Integer.parseInt(serverInputStream.readLine());
					
					if(decision == ServerConstants.CLOSE){
						System.out.println("Client wybral zamkniecie polaczenia");
						break;
					}
					else if (decision == ServerConstants.SUBMIT_NEW_SERVICE){
						System.out.println("Client wybral zglosznie uslugi");
						Service service = getService();
						serviceSet.add(service);
						data.put(actualClient, serviceSet);
						
						for (Map.Entry<String, HashSet<Service>> entry : data.entrySet()) {
						    System.out.println(entry.getKey() + " : ");
						    HashSet<Service> temp = entry.getValue();
						    for (Service s : temp) {
						        System.out.print(s.print());
						    }
						}
						//zapytaj o nazwe uslugi i termin
						//sprawdz czy usluga istnieje
					}
					else if (decision == ServerConstants.WITHDRAW_YOUR_SERVICE){
						System.out.println("Client wybral zamkniecie uslugi");
					}
					else if (decision == ServerConstants.SHOW_ALL_SERVICES){
						System.out.println("Client wybral wyswietlenie wszystkich uslug");
					}
					else if (decision == ServerConstants.RESERVE_SERVICE){
						System.out.println("Client wybral rezerwacje uslugi");
					}
				}
				
				socket.close();
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		private void setActualClientName(String name){
			this.actualClient = name;
		}
		
	}
	
	private void showOptions(PrintWriter serverOutputStream){
		serverOutputStream.println(	"Co chcesz zrobic? \n" +
					"0. Wyjdz i zamknij po³¹czenie \n" +
					"1. Zg³oœ now¹ us³ugê. \n" + 
					"2. Wycofaj swoj¹ us³ugê. \n" + 
					"3. Wyœwietl wszystkie dostêpne us³ugi. \n" +
					"4. Zarezerwuj us³ugê. \n");
	}
	
	private String getNameFromClient() throws IOException{
		serverOutputStream.println("Podaj nazwe uzytkownika");
		String nameOfClient = serverInputStream.readLine();
		if (nameOfClient == null){
			return null;
		}
		synchronized(data){
			if (!data.containsKey(nameOfClient)){
				data.put(nameOfClient, new HashSet<>());
				serverOutputStream.println("accepted");
				return nameOfClient;
			}
			else {
				serverOutputStream.println("error.");
			}
		}
		return null;
	}
	
	private Service getService() throws IOException{
		serverOutputStream.println("nazwa");
		String nameOfService = serverInputStream.readLine();
		if (nameOfService == null){
			return null;
		}
		synchronized(serviceSet){
			if (!serviceSet.contains(nameOfService)){
				System.out.println("Server: " + nameOfService);
				serverOutputStream.println("accepted");
				String termOfService = getTermOfService();
				serverOutputStream.println("accepted");
				return new Service(nameOfService, termOfService);
			}
			else {
				serverOutputStream.println("error.");
				return null;
			}
		}
	}
	
	private String getTermOfService() throws IOException{
		serverOutputStream.println("termin");
		String termOfService = serverInputStream.readLine();
		if (termOfService == null){
			return null;
		}else{
			System.out.println("Server: " + termOfService);
			return termOfService;
		}
	}

>>>>>>> refs/remotes/origin/master
}