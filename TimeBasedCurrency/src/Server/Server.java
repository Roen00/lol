package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
 
public class Server {
	private final int port = 3000;
	//private int numberOfClientsConnected = 0;
	
	private Map<String, List<Service>> data = new LinkedHashMap<String, List<Service>>();
	
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
		private PrintWriter serverOutputStream;
		private BufferedReader serverInputStream;
		private int decision;
		private String nameOfClient;
		
		public Connection(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				System.out.println("Klient polaczyl sie z serverem.");
				
				serverOutputStream = new PrintWriter(socket.getOutputStream(), true);
				serverInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//String inputLine;
				
				
				//Getting name from user
				while(true){
					if(getNameFromClient(serverOutputStream, serverInputStream, nameOfClient)){
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
				
				
				while(true){
					decision = Integer.parseInt(serverInputStream.readLine());
					
					if(decision == ServerConstants.CLOSE){
						System.out.println("Client wybral zamkniecie polaczenia");
						//out.println("Zamykam polaczenie!");
						return;
					}
					else if (decision == ServerConstants.SUBMIT_NEW_SERVICE){
						System.out.println("Client wybral zgloszneie uslugi");
						break;
					}
					else if (decision == ServerConstants.WITHDRAW_YOUR_SERVICE){
						System.out.println("Client wybral zamkniecie uslugi");
						break;
					}
					else if (decision == ServerConstants.SHOW_ALL_SERVICES){
						System.out.println("Client wybral wyswietlenie wszystkich uslug");
						break;
					}
					else if (decision == ServerConstants.RESERVE_SERVICE){
						System.out.println("Client wybral rezerwacje uslugi");
						break;
					}
					
					System.out.println("Przyjalem decyzje i robie cos");
					
				}
				
				
				
				
				socket.close();
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
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
	
	private boolean getNameFromClient(PrintWriter serverOutputStream, BufferedReader serverInputStream, String nameOfClient) throws IOException{
		serverOutputStream.println("Podaj nazwe uzytkownika");
		nameOfClient = serverInputStream.readLine();
		if (nameOfClient == null){
			return false;
		}
		synchronized(data){
			if (!data.containsKey(nameOfClient)){
				data.put(nameOfClient, new LinkedList<>());
				serverOutputStream.println("accepted");
				return true;
			}
			else {
				serverOutputStream.println("error.");
			}
		}
		return false;
	}
}