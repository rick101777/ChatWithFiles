import java.net.Socket;

public interface NodeInterface {

	public void setSocket(Socket socket);
	
	public Socket getSocket();
	
	public void setName(String name);
	
	public String getName();
	
	public String getMessage();
	
	public void setMessage(String Message);
	
	public void printContents();
	
	public boolean equals(Object object);
	
	public int hashcode();
	
	
}
