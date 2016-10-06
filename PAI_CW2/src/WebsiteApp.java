import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;


public class WebsiteApp {

	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("Nie podano ¿adnych argumentów.");
		}
		else {
			
		}

		
		openWebsite("http://www.google.pl/");
	}
	
	public static void openWebsite(String path){
			try {
		    URL myURL = new URL(path);
		    URLConnection myURLConnection = myURL.openConnection();
		    myURLConnection.connect();
	        BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));

	        String inputLine;
	        while ((inputLine = in.readLine()) != null){
	        	//System.out.println(inputLine);
	        	findEmails(inputLine);
	        }

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
	
	private static void findEmails(String text){
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
			//allMatches.add(matcher.group());
			System.out.println(matcher.group());
		}
		
		
	}
		
	
}