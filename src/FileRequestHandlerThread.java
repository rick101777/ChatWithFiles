import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileRequestHandlerThread implements Runnable {
	
	private Socket Client;
	private String Filename;
	private DataInputStream input;
	private DataOutputStream output;
	private final static int size = 2056;
	
	public FileRequestHandlerThread(int FilePort, String Filename) throws IOException{
		Client = new Socket("localhost", FilePort);
		this.Filename = Filename;
		input = new DataInputStream(Client.getInputStream());
		output = new DataOutputStream(Client.getOutputStream());
	}
	

	@Override
	public void run() {
		try {

			FileOutputStream FileStream = new FileOutputStream(this.Filename);
			byte[] buffer = new byte[size];
			int numberRead;
			while((numberRead = input.read(buffer)) != -1) {
				FileStream.write(buffer, 0, numberRead);
			}
			FileStream.close();

			
			
			
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
