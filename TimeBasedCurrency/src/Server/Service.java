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
	
	
	
}
