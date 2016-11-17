package Server;

import java.net.Socket;

public class ServiceProtocol {
	//private Socket socket;
	
	public ServiceProtocol(Socket socket){
		//this.socket = socket;
	}
	
	public boolean processDecision(int decision){
		if(decision == ServerConstants.CLOSE){
			System.out.println("Client wybral zamkniecie polaczenia");
			//out.println("Zamykam polaczenie!");
			return true;
		}
		else if (decision == ServerConstants.SUBMIT_NEW_SERVICE){
			System.out.println("Client wybral zglosznie uslugi");
			//zapytaj o nazwe uslugi i termin
			//sprawdz czy usluga istnieje
			
			return false;
		}
		else if (decision == ServerConstants.WITHDRAW_YOUR_SERVICE){
			System.out.println("Client wybral zamkniecie uslugi");
			return false;
		}
		else if (decision == ServerConstants.SHOW_ALL_SERVICES){
			System.out.println("Client wybral wyswietlenie wszystkich uslug");
			return false;
		}
		else if (decision == ServerConstants.RESERVE_SERVICE){
			System.out.println("Client wybral rezerwacje uslugi");
			return false;
		}
		
		return false;
	}
}
