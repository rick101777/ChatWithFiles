import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class ChatServer {
	
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("There were no arguments passed");
			System.err.println("Server Takes a Port Number");
			return;
		}
		ServerDataSingleton Data = ServerDataSingleton.getInstance();
		Thread inputThread = null;
		outputThread out = null;
		Thread outputThread = null;
		try {
			out = new outputThread();
			outputThread = new Thread(out);
			outputThread.start();
			
			
			int portNumber = Integer.valueOf(args[0]);
			System.out.println(portNumber);
			ServerSocket serverSocket = new ServerSocket(portNumber);
			
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Connection Established");
				BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
				writer.println("Enter your Username: ");
				String name = null;
				do {
					name = input.readLine();
				}while(name == null);
				System.out.println("Got name");
				System.out.println(name);
				Node client = new Node(clientSocket, name);
				Data.addToArray(client);
				inputThread Threadin = new inputThread(client);
				inputThread = new Thread(Threadin);
				inputThread.start();
			}
			
			
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}finally {
			try {
				inputThread.join();
				outputThread.join();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}


class inputThread implements Runnable{
	ServerDataSingleton Data = ServerDataSingleton.getInstance();
	
	
	Node client;
	
	public inputThread(Node client) {
		this.client = client;
	}

	@Override
	public void run() {
		BufferedReader input = null;
		try {
			PrintWriter writer = new PrintWriter(client.getSocket().getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				client.setMessage(line);
				Data.addToQueue(client);
				
			}
			Data.remove(client);
		}catch(IOException e) {
			System.err.println(e.getMessage());
		}finally {
			try {
				input.close();
			}catch(IOException e) {
				System.err.println(e.getMessage());
			}
			
		}
	}
	
	
	
}


class outputThread implements Runnable{
	ServerDataSingleton Data = ServerDataSingleton.getInstance();
	
	public outputThread() {
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}
			if (!Data.isQueueEmpty()) {
				Node From = Data.removeFromQueue();
				int n = Data.getSize();
				for (int i = 0; i < n; i++) {
					try {
						Node Sender = Data.getElementAtIndex(i);
						if (!From.equals(Sender)) {
							PrintWriter writer = new PrintWriter(Sender.getSocket().getOutputStream(),true);
							writer.println(From.getName() + ": " +From.getMessage());
						}
					}catch(IOException e) {
						System.err.println(e.getMessage());
					}
				}
			}
		}
	}
	
}








