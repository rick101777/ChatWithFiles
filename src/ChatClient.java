import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient{
	
	
	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println("There were no arguments passed");
			System.err.println("Client require a Port Number to be passed");
			return;
		}
		
		BufferedReader standardInput = null;
		PrintWriter writer = null;
		Thread t1 = null;
		try {
			int ListeningPort = Integer.valueOf(args[1]);
			int portNumber = Integer.valueOf(args[3]);
			System.out.println(portNumber);
			Socket ServerSocket = new Socket("localhost", portNumber);
			System.out.println("Client Connection Established");
			InputThread Threadin = new InputThread(ServerSocket);
			t1 = new Thread(Threadin);
			t1.start();
			
			standardInput = new BufferedReader(new InputStreamReader(System.in));
			writer = new PrintWriter(ServerSocket.getOutputStream(), true);
			String Name = standardInput.readLine();
			writer.println(Name);
			writer.println(ListeningPort);
			String userMessage;
			while(true) {
				System.out.println("Enter an option ('m', 'f', 'x'):\n"
						+ "(M)essage (send)\n"
						+ "(F)ile (request)\n"
						+ "e(X)it");
				userMessage = standardInput.readLine();
				if (userMessage.equals("m")) {
					writer.println("m");
					System.out.println("Enter your message: ");
					userMessage = standardInput.readLine();
					writer.println(userMessage);
				}
				if (userMessage.equals("f")) {
					writer.println("f");
					System.out.println("Who owns the file?");
					userMessage = standardInput.readLine();
					writer.println(userMessage);
					System.out.println("Which file do you want?");
					userMessage = standardInput.readLine();
					writer.println(userMessage);
					FileRequestHandlerThread temp = new FileRequestHandlerThread(ListeningPort, userMessage);
					Thread request = new Thread(temp);
					request.start();
					try {
						request.join();
					}catch(InterruptedException ie) {
						
					}
				}
				if (userMessage.equals("x")) {
					writer.println("x");
					break;
				}
			}
			
			Threadin.Exiting();
			writer.close();
			standardInput.close();
			try {
				t1.join();
			}catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
			
			
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}finally {
			try {
				standardInput.close();
				writer.close();
				try {
					t1.join();
				}catch(InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}catch(IOException e) {
				System.err.println(e.getMessage());
			}
		}
		
		
		
	}

}



class InputThread implements Runnable{
	
	private Socket ServerSocket;
	private boolean ShouldContinue = true;
	
	public InputThread(Socket socket) {
		this.ServerSocket = socket;
	}

	@Override
	public void run() {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(ServerSocket.getInputStream()));
			String line = input.readLine();
			System.out.println(line);
			while (ShouldContinue) {
				line = input.readLine();
				if (line.equals("f")) {
					String ListeningPort = input.readLine();
					line = input.readLine();
					FileResponseHandlerThread temp = new FileResponseHandlerThread(Integer.parseInt(ListeningPort), line);
					Thread response = new Thread(temp);
					response.start();
					try {
						response.join();
					}catch(InterruptedException ie) {
						
					}
				}
				System.out.println(line);
			}
			input.close();
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				input.close();
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	
	public void Exiting() {
		ShouldContinue = false;
	}
	
}