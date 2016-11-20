package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
			
			@SuppressWarnings("unchecked")
			HashSet<Service> services = (HashSet<Service>)objectInputStream.readObject();
			
			if (!services.isEmpty()){
				System.out.println("Twoje us³ugi: ");
				ServiceUtility.printHashSet(services);
				
				System.out.println("Podaj nazwe zamykanej uslugi: ");
				String nameOfClosingService = stdIn.readLine();
				objectOutputStream.writeObject(nameOfClosingService);
			}
			else {
				System.out.println("Brak dostêpnych us³ug.");
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
		else if (decision == ServerConstants.SHOW_ALL_SERVICES){
			System.out.println("Client wybral wyswietlenie wszystkich uslug");
			objectOutputStream.writeObject("show");
			System.out.println("Dostêpne us³ugi: ");
			
			@SuppressWarnings("unchecked")
			Map<String, HashSet<Service>> data = (LinkedHashMap<String, HashSet<Service>>)objectInputStream.readObject();
			
			if (!data.isEmpty()){
				ServiceUtility.printHashMap(data);
				
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
			objectOutputStream.writeObject("reserve");
			System.out.println("Client wybral rezerwacje uslugi");
			
			@SuppressWarnings("unchecked")
			Map<String, HashSet<Service>> data = (LinkedHashMap<String, HashSet<Service>>)objectInputStream.readObject();
			
			if (!data.isEmpty()){
				System.out.println("Twoje us³ugi: ");
				ServiceUtility.printHashMap(data);
				System.out.println("Podaj nazwe rezerwowanej us³ugi: ");
				String nameOfServiceToReserve = stdIn.readLine();
				objectOutputStream.writeObject(nameOfServiceToReserve);
			}
			else {
				System.out.println("Brak dostêpnych us³ug.");
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
	
//	public void printHashSet(HashSet<Service> services){
//		for (Service s : services) {
//			System.out.print(s.print());
//		}
//	}
}
