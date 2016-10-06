import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {

	public static void main(String[] args) throws Exception {
		Scanner reader = new Scanner(System.in);
		ArrayList<String> listOfFiles = new ArrayList<String>();

		System.out.println("Podaj liste plików: (gdy chcesz zakoñczyæ podawanie wpisz stop)");
		
		while(!reader.hasNext("stop")) {
			listOfFiles.add(reader.nextLine());
		}
		
		reader.nextLine();
		
		System.out.println("Uruchomiæ:\n1) równoczeœnie\n2) sekwencyjnie");
		
		String input = reader.nextLine();
		
		if (Integer.parseInt(input) == 1){
			runParallel(listOfFiles);
		} 
		else if (Integer.parseInt(input) == 2){
			runSequentially(listOfFiles);
		}
		else {
			System.out.println("Nie wybrano sposobu uruchomienia.");
		}
		
		reader.close();
	}
	
	
	private static void runParallel(ArrayList<String> listOfFiles) throws Exception{
		Runnable[] runners = new Runnable[listOfFiles.size()];
		Thread[] threads = new Thread[listOfFiles.size()];
		
		for (int i = 0; i < listOfFiles.size(); i++){
			runners[i] = new CountNumberOfLinesTask(listOfFiles.get(i));
			threads[i] = new Thread(runners[i]);
			threads[i].start();
		}
	}
	
	private static void runSequentially(ArrayList<String> listOfFiles) throws Exception{
		Runnable[] runners = new Runnable[listOfFiles.size()];
		Thread[] threads = new Thread[listOfFiles.size()];
		
		for (int i = 0; i < listOfFiles.size(); i++){
			runners[i] = new CountNumberOfLinesTask(listOfFiles.get(i));
			threads[i] = new Thread(runners[i]);
			threads[i].start();
			threads[i].join(); //sekwencyjnie
		}
	}
}

class CountNumberOfLinesTask implements Runnable {
	private final String path;
	 
	public CountNumberOfLinesTask(String path){
		this.path = path;
	}
	
	public static int numberOfLines(String path) throws IOException{
		FileReader fileReader = new FileReader(path+".txt");
		BufferedReader readFile = new BufferedReader(fileReader);
		int numberOfLines = 0;
		while((readFile.readLine()) != null)
			numberOfLines++;
		
		readFile.close();
		return numberOfLines;
	}

	@Override
	public void run() {
		try{
			Long actualTime = System.currentTimeMillis();
			System.out.println(numberOfLines(path));
			System.out.println("Time of running: " + (System.currentTimeMillis()-actualTime));
		}catch (IOException exception){
			System.out.println("Blad! " + exception);
		}
	}
}