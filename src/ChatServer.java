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
				int ListeningPort = 0;
				do {
					name = input.readLine();
					ListeningPort = Integer.parseInt(input.readLine());
				}while(name == null && ListeningPort == 0);
				System.out.println("Got name");
				System.out.println(name);
				System.out.println("Got Listening Port: " + ListeningPort);
				Node client = new Node(clientSocket, name, ListeningPort);
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
			while (true) {
				line = input.readLine();
				if (line.equals("m")) {
					line = input.readLine();
					client.setMessage(line);
					Data.addToQueue(client);
				}
				if (line.equals("f")) {
					line = input.readLine();
					int DataSize = Data.getSize();
					for (int i = 0; i < DataSize; i++) {
						if (Data.getElementAtIndex(i).getName().equals(line)) {
							System.out.println(Data.getElementAtIndex(i).getName());
							PrintWriter temp = new PrintWriter(Data.getElementAtIndex(i).getSocket().getOutputStream(), true);
							temp.println("f");
							temp.println(this.client.getListeningPort());
							line = input.readLine();
							temp.println(line);
						}
					}
					
				}
				if (line.equals("x")) {
					break;
				}			
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
			}catch(InterruptedException ie) {
				
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








