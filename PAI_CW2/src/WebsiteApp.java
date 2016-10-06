import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;


public class WebsiteApp {

	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("Nie podano ¿adnych argumentów.");
		}
		else if (args.length == 1){
			openWebsite(args[0]);
		}
		else {
			System.out.println("Podano za du¿o argumentów.");
		}
	}
	
	public static void openWebsite(String path){
		try {
		    URL myURL = new URL(path);
		    URLConnection myURLConnection = myURL.openConnection();
		    myURLConnection.connect();
	        BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));

	        String inputLine;
	        while ((inputLine = in.readLine()) != null){
	        	findSitesAndEmails(inputLine);
	        }
	      
	        findAndSaveToFile(myURL);

	        in.close();
		} 
		catch (MalformedURLException e) { 
		    // new URL() failed
		    // ...
			System.out.println("Problem z polaczeniem: " + e);
		} 
		catch (IOException e) {   
		    // openConnection() failed
		    // ...
			System.out.println("Problem z polaczeniem: " + e);
		}
	}
	
	private static void findSitesAndEmails(String text){
		String emailPattern1 = ("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
		String emailPattern2 = ("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(mailto:)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)\"?");
		String httpPattern = ("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)\"?");

		List<String> tokens = new ArrayList<String>();
		tokens.add(httpPattern);
		tokens.add(emailPattern1);
		tokens.add(emailPattern2);
		
		String patternString = "\\b(" + StringUtils.join(tokens, "|") + ")\\b";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(text);
		 
		while (matcher.find()) {
			System.out.println(matcher.group());
		}	
	}
	
	private static void findAndSaveToFile(URL myURL) throws IOException{
		URLConnection myURLConnection = myURL.openConnection();
	    myURLConnection.connect();
	    
	    PrintWriter writer = new PrintWriter("information.txt", "UTF-8");
	    
	    /*Getting content of <head>*/
	    Map<String, List<String>> map = myURLConnection.getHeaderFields();

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			//System.out.println("Key : " + entry.getKey() + " , Value : " + entry.getValue());
			writer.println("Key : " + entry.getKey() + " , Value : " + entry.getValue());
		}
		
		/*Getting IP address*/
		InetAddress address = InetAddress.getByName(myURL.getHost());
		String ip = address.getHostAddress();
		//System.out.println("Adres ip servera: " + ip);
		writer.println("Adres ip servera: " + ip);
	    
		/*Getting parameters of connection*/
		String[] key = {"Protocol", "Authority", "Host", "Port", "Path", "Query", "Filename", "Ref"};
		String[] value = {myURL.getProtocol(), myURL.getAuthority(), myURL.getHost(), Integer.toString(myURL.getPort()), myURL.getPath(), myURL.getQuery(), myURL.getFile(), myURL.getRef()};
		HashMap<String, String> params = new HashMap<String, String>();
		
		for (int i = 0; i < key.length; i++){
			params.put(key[i], value[i]);
		}
		
		//System.out.println(Arrays.asList(params));
		writer.print(Arrays.asList(params));
		writer.close();
		
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "information.txt");
		pb.start();
	}
}