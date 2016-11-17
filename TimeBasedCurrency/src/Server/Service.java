package Server;

public class Service {
	
	private String nameOfService;
	private String termOfService;
	private int statusOfService;
	
	
	public void setNameOfService(String nameOfService){
		this.nameOfService = nameOfService;
	}
	
	public String getNameOfService(){
		return this.nameOfService;
	}
	
	public void setTermOfService(String termOfService){
		this.termOfService = termOfService;
	}
	
	public String getTermOfService(){
		return this.termOfService;
	}
	
	public void setStatusOfService(int status){
		this.statusOfService = status;
	}
	
	public int getStatusOfService(){
		return this.statusOfService;
	}
	
	public Service(){
		this.nameOfService = ServiceConstants.EMPTY;
		this.termOfService = ServiceConstants.EMPTY;
		this.statusOfService = ServiceConstants.NO_INFO;
	}
	
	public Service(String nameOfService, String termOfService){
		this.nameOfService = nameOfService;
		this.termOfService = termOfService;
		this.statusOfService = ServiceConstants.NEW_SERVICE;
	}
	
	public String print(){
		if(statusOfService == 0){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: no info.");
		}
		else if(statusOfService == 1){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: new service");
		}
		else if(statusOfService == 2){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: reserved.");	
		}
		else if(statusOfService == 3){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: withdrawn.");	
		}
		else {
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: !!!PROBLEM!!!");	
		}
	}
	
	
	
}
