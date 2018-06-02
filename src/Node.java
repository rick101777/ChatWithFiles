import java.net.Socket;

public class Node implements NodeInterface {
	
	private Socket socket;
	private String name;
	private int ListeningPort;
	private String Message = null;
	
	public Node() {
		
	}
	
	public Node(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	public Node(Socket socket, String name, int ListeningPort) {
		this.socket = socket;
		this.name = name;
		this.ListeningPort = ListeningPort;
	}
	

	@Override
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void printContents() {
		String Output = "" +this.socket + ", " + this.name;
		System.out.println(Output);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (!(object instanceof Node)) {
			return false;
		}
		Node node = (Node) object;
		return node.socket.equals(this.socket) && node.name.equals(this.name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.socket.hashCode();
		result = 31 * result + this.name.hashCode();
		
		return result;
	}

	@Override
	public String getMessage() {
		return Message;
	}

	@Override
	public void setMessage(String Message) {
		this.Message = Message;
	}

	@Override
	public void setListeningPort(int ListeningPort) {
		this.ListeningPort = ListeningPort;
	}

	@Override
	public int getListeningPort() {
		return this.ListeningPort;
	}

		
}
