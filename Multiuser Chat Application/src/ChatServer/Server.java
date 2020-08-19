package ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server extends Thread {
    private final int serverPort;

    private ArrayList<ServerWorker> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    // a method to return the concurrent users
    public List<ServerWorker> getWorkerList() {
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(serverPort);
            while(true) {
                System.out.println("About to accept client connection...");
                Socket s = ss.accept();
                System.out.println("Connection established" );
                ServerWorker worker = new ServerWorker(this, s);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  //method to remove a user from the list of online users who wishes to go offline
    public void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }
}