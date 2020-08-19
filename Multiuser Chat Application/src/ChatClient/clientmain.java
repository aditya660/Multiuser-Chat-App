package ChatClient;

public class clientmain {
	   public static void main(String[] args) {
	        int port = 8818;
	        String servername = "localhost";
	        clientWorker client = new clientWorker(servername, port);
	        client.start();
	    }


}
