package Server;

import java.util.HashSet;
import java.util.Map;

public final class ServiceUtility {
	
	public ServiceUtility() {
	
	}
	
	public final static int NO_INFO = 0;
	public final static int NEW_SERVICE = 1;
	public final static int RESERVED_SERVICE = 2;
	public final static int WITHDRAWN_SERVICE = 3;
	
	public final static String EMPTY = "";
	
	
	public static void printHashSet(HashSet<Service> services){
		for (Service s : services) {
			System.out.print(s.print());
		}
	}
	
	public static void printHashMap(Map<String, HashSet<Service>> data){
		for (Map.Entry<String, HashSet<Service>> entry : data.entrySet()) {
			System.out.println(entry.getKey() + " : ");
			HashSet<Service> temp = entry.getValue();
			for (Service s : temp) {
				System.out.print(s.print());
			}
		}
	}
	
}
