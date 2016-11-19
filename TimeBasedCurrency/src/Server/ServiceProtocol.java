package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ServiceProtocol {
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private BufferedReader stdIn;

	public ServiceProtocol(ObjectOutputStream outputStream, ObjectInputStream inputStream){
		this.objectOutputStream = outputStream;
		this.objectInputStream = inputStream;
	}
	
	public boolean processDecision(int decision) throws IOException, ClassNotFoundException{
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		if(decision == ServerConstants.CLOSE){
			System.out.println("Client wybral zamkniecie polaczenia");
			objectOutputStream.writeObject("close connection");
			return true;
		}
		else if (decision == ServerConstants.SUBMIT_NEW_SERVICE){
			objectOutputStream.writeObject("new");
			System.out.println("Client wybral zglosznie uslugi");
			System.out.println("Podaj nazwe uslugi: ");
			String nameOfService = stdIn.readLine();
			System.out.println("Podaj termin uslugi: ");
			String termOfService = stdIn.readLine();
			
			//Creating and sending object to server
			Service service = new Service(nameOfService, termOfService);
			objectOutputStream.writeObject(service);
			
			
			//Getting validation status from server
			String line = (String)objectInputStream.readObject();

			if (line.equals("error")){
				System.out.println("Bledne dane");
				return true;
			}
			else if (line.equals("accepted")){
				System.out.println("Poprawne dane");
				return false;
			}
			else{
				return true;
			}
		}
		else if (decision == ServerConstants.WITHDRAW_YOUR_SERVICE){
			objectOutputStream.writeObject("withdrawn");
			System.out.println("Client wybral zamkniecie uslugi");
			System.out.println("Twoje us³ugi: ");
			@SuppressWarnings("unchecked")
			HashSet<Service> services = (HashSet<Service>)objectInputStream.readObject();
			for (Service s : services) {
		        System.out.print(s.print());
		    }
			System.out.println("Podaj nazwe zamykanej uslugi: ");
			String nameOfClosingService = stdIn.readLine();
			objectOutputStream.writeObject(nameOfClosingService);
			
			String line = (String)objectInputStream.readObject();
			if (line.equals("error")){
				System.out.println("Bledne dane");
				return true;
			}
			else if (line.equals("accepted")){
				System.out.println("Poprawne dane");
				return false;
			}
			else{
				return true;
			}
		}
		else if (decision == ServerConstants.SHOW_ALL_SERVICES){
			System.out.println("Client wybral wyswietlenie wszystkich uslug");
			objectOutputStream.writeObject("show");
			System.out.println("Dostêpne us³ugi: ");
			
			Map<String, HashSet<Service>> data = (HashMap<String, HashSet<Service>>)objectInputStream.readObject();
			
			if (!data.isEmpty()){
				for (Map.Entry<String, HashSet<Service>> entry : data.entrySet()) {
				    System.out.println(entry.getKey() + " : ");
				    HashSet<Service> temp = entry.getValue();
				    for (Service s : temp) {
				        System.out.print(s.print());
				    }
				}
				
				objectOutputStream.writeObject("ok");
			}
		
			
			String line = (String)objectInputStream.readObject();
			if (line.equals("error")){
				System.out.println("Bledne dane");
				return true;
			}
			else if (line.equals("accepted")){
				System.out.println("Poprawne dane");
				return false;
			}
			else{
				return true;
			}
		}
		else if (decision == ServerConstants.RESERVE_SERVICE){
			System.out.println("Client wybral rezerwacje uslugi");
			return false;
		}
		else{
			return true;
		}
		
	}
	
	public void showOptions(){
		System.out.println(	"Co chcesz zrobic? \n" +
					"0. Wyjdz i zamknij po³¹czenie \n" +
					"1. Zg³oœ now¹ us³ugê. \n" + 
					"2. Wycofaj swoj¹ us³ugê. \n" + 
					"3. Wyœwietl wszystkie dostêpne us³ugi. \n" +
					"4. Zarezerwuj us³ugê.");
	}
}
