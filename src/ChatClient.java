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
			int portNumber = Integer.valueOf(args[0]);
			System.out.println(portNumber);
			Socket ServerSocket = new Socket("localhost", portNumber);
			System.out.println("Client Connection Established");
			InputThread Threadin = new InputThread(ServerSocket);
			t1 = new Thread(Threadin);
			t1.start();
			
			standardInput = new BufferedReader(new InputStreamReader(System.in));
			writer = new PrintWriter(ServerSocket.getOutputStream(), true);
			String userMessage;
			while((userMessage = standardInput.readLine()) != null) {
				writer.println(userMessage);
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
			while (ShouldContinue) {
				System.out.println(line);
				line = input.readLine();
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