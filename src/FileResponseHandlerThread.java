import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileResponseHandlerThread implements Runnable {
	
	private ServerSocket Server;
	private Socket Client;
	private String Filename;
	private DataInputStream input;
	private DataOutputStream output;
	private static final int size = 2056;
	
	
	public FileResponseHandlerThread(int FilePort, String Filename) throws IOException {
		Server = new ServerSocket(FilePort);
		System.out.println("Listening for connections on Port: " + FilePort);
		Client = Server.accept();
		System.out.println("File Response Connection Established");
		Server.close();
		this.Filename = Filename;
		input = new DataInputStream(Client.getInputStream());
		output = new DataOutputStream(Client.getOutputStream());
	}
	

	@Override
	public void run() {
		try {
		File file = new File(this.Filename);
		
		if (file.exists() && file.canRead()) {
			
			FileInputStream FileStream = new FileInputStream(file);
			System.out.println("Transmitting File: " + this.Filename);
			byte[] buffer = new byte[size];
			int numberRead;
			while ((numberRead = FileStream.read(buffer)) != -1) {
				output.write(buffer, 0, numberRead);
				System.out.println(numberRead);
			}
			FileStream.close();
		
		}
		
		}catch(IOException ioe) {
			
		}finally {
			try {
				Client.close();
				input.close();
				output.close();
			}catch(IOException ioe) {
				
			}
		}
		
	}

}
