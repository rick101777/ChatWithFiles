import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ServerDataSingleton {
	
	private ArrayList<Node> clientList;
	private Queue<Node> Messages;
	
	private static ServerDataSingleton ServerData = new ServerDataSingleton();
	
	private ServerDataSingleton() {
		this.clientList = new ArrayList<Node>();
		this.Messages = new LinkedList<Node>();
	}
	
	public static synchronized ServerDataSingleton getInstance() {
		return ServerData;
	}
	
	public Queue<Node> getQueue(){
		return Messages;
	}
	
	
	public Node getElementAtIndex(int index) {
		return clientList.get(index);
	}
	
	public void addToArray(Node client) {
		clientList.add(client);
	}
	
	public void addToQueue(Node client) {
		Messages.add(client);
	}
	
	public Node removeFromQueue() {
		return Messages.remove();
	}
	
	public boolean isQueueEmpty() {
		return Messages.isEmpty();
	}
	
	public void remove(Node client) {
		clientList.remove(client);
	}
	
	public int getSize() {
		return clientList.size();
	}

	
	
}
