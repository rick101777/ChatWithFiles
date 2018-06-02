import java.net.Socket;

public interface NodeInterface {

	public void setSocket(Socket socket);
	
	public Socket getSocket();
	
	public void setName(String name);
	
	public String getName();
	
	public String getMessage();
	
	public void setMessage(String Message);
	
	public void printContents();
	
	public void setListeningPort(int ListeningPort) ;
	
	public int getListeningPort();
}
