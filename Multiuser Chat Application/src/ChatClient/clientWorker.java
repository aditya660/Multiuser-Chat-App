package ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class clientWorker extends  Thread{

    private final int serverPort;
    private final String servername;
    private Socket s;
  


    public clientWorker(String servername,int serverPort) {
        this.serverPort = serverPort;
        this.servername = servername;
    }

    @Override
    public void run() {
        try {
            this.s = new Socket(servername, serverPort);
            System.out.println("Client port is " + s.getLocalPort());
            //login("guest" , "guest");
            Scanner sc=new Scanner(System.in);
            String cmd=sc.nextLine();  //input by user/client
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(String cmd)  throws IOException {

      // the correct syntax of command would be (login <username> <password>)
        OutputStream os=s.getOutputStream();
        os.write(cmd.getBytes());
      // now we will get response from the server whether login is successful or not
         BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
         String response = br.readLine();
        System.out.println("Server Response " + response);

        if ("Ok login".equalsIgnoreCase(response)) {
            System.out.println("login successful");
            startMessageReader();

        } else {
            System.out.println(" unable to login");
        }
    }

    private void startMessageReader() throws IOException {

     Thread t = new Thread()
     {
        @Override
        public void run(){
        readMessageLoop(); // to print responses from server
        }
    }; 
        t.start();

        while(true){
        
		Scanner sc= new Scanner(System.in);  // whatever the client inputs, we outstream it towards the server side
        String clientinput = sc.nextLine();

        try {

    OutputStream os=s.getOutputStream();
     os.write(clientinput.getBytes());           // sending it to server

        } catch (IOException e) {
            e.printStackTrace();
        }
     }
  }

    private void readMessageLoop() {  //will read server side responses and simply print it
        try {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            while ((line = br.readLine()) != null) {
            System.out.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
