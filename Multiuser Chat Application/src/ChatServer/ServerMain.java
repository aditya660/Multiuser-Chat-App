package ChatServer;

public class ServerMain {
    public static void main(String[] args) {
        int port = 8818;
        Server server = new Server(port); //making an object of the class Server which we will make as a thread
        server.start(); //kickstarting the thread
    }
}
