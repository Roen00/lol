package Server;

<<<<<<< HEAD
import java.io.Serializable;

public class Service implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nameOfService;
	private String termOfService;
	private int statusOfService;
	
	@Override
	public int hashCode(){
		return nameOfService.hashCode()+termOfService.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		Service service = (Service)o;
		if (this.nameOfService.equals(service.nameOfService)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public Service(){
		this.nameOfService = ServiceUtility.EMPTY;
		this.termOfService = ServiceUtility.EMPTY;
		this.statusOfService = ServiceUtility.NO_INFO;
	}
	
	public Service(String nameOfService, String termOfService){
		this.nameOfService = nameOfService;
		this.termOfService = termOfService;
		this.statusOfService = ServiceUtility.NEW_SERVICE;
	}
	
	public Service(String nameOfService){
		this.nameOfService = nameOfService;
		this.termOfService = "";
		this.statusOfService = ServiceUtility.NO_INFO;
	}
	
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
	
	public String print(){
		if(statusOfService == 0){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: no info.\n");
		}
		else if(statusOfService == 1){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: new service\n");
		}
		else if(statusOfService == 2){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: reserved.\n");	
		}
		else if(statusOfService == 3){
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: withdrawn.\n");	
		}
		else {
			return ("Service name: " + this.nameOfService + ", service term: " + this.termOfService + ", service status: !!!PROBLEM!!!\n");	
		}
	}
=======
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
	
	
	
>>>>>>> refs/remotes/origin/master
}
