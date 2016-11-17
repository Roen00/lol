package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Uruchomiono serwer.");
        ServerSocket serverSocket = new ServerSocket(9890);
        
        try {
            while (true) {
            	Connection connection = new Connection(serverSocket.accept());
            	connection.start();
            }
        } 
		catch(Exception e){
			System.out.println(e.getMessage());
        }
        serverSocket.close();
    }
 
    static class Connection extends Thread {
        private Socket socket;
        
        public Connection(Socket socket) {
        	System.out.println("Klient polaczyl sie z serverem.");
            this.socket = socket;
        }
 
        public void run() {
            try {
            	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

  
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                   out.println(inputLine);
                }
                
                socket.close();
            } 
			catch (IOException e) {
				System.out.println(e.getMessage());
            } 
        }
    }
}