package ChatServer;


import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;


public class ServerWorker extends Thread {

    private static final String StringUtils = null;
	private final Socket s;
    private final Server server;
    private String login = null;    //will store name if user is online else will store null
    private HashSet<String> topicSet=new HashSet<>();  // to store the current topics/groups

    public ServerWorker(Server server, Socket s) {
        this.server = server;
        this.s= s;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {

        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));        
        String line;
        while ( (line = br.readLine()) != null) {

            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];

                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;

                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(tokens);

                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, 3);
                    handleMessage(tokensMsg);

                } else if ("join".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, 2);
                    handleJoin(tokensMsg);

                }else if ("leave".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, 2);
                    handleLeave(tokensMsg);

                }else {
                    String msg = "unknown " + cmd + "\n";
                    OutputStream os=s.getOutputStream();
                    os.write(msg.getBytes());
                }
            }
        }

        s.close();
    }

      private void handleLogin(String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String user = tokens[1];
            String password = tokens[2];

            if ((user.equals("guest") && password.equals("guest")) || (user.equals("aditya") && password.equals("aditya")) ) {
                String msg = "Ok login\n";
                OutputStream os=s.getOutputStream();
                    os.write(msg.getBytes());                
                this.login = user;
                System.out.println("User logged in succesfully: " + login);

                List<ServerWorker> workerList = server.getWorkerList();

                // send current user, all other online user's status
                for(ServerWorker worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {  //to prevent self messaging
                            String msg2 = "ONLINE " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }

                // send other online users, current user's status
                String onlineMsg = "online " + login + "\n";
                for(ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }

            } else {
                String msg = "error login\n";
                OutputStream os=s.getOutputStream();
                os.write(msg.getBytes());
            }
        }
    }

    private void handleMessage(String[] tokens) throws IOException { // case1- group message, case2 -direct message
        String sendTo = tokens[1];
        String body = tokens[2];
         List<ServerWorker> workerList=server.getWorkerList();    

        if(sendTo.charAt(0)=='#'){  // for group message
            for (ServerWorker worker : workerList) {
               if(worker.isMemberofTopic(sendTo)){
                String outMsg="You have received a message from the topic"+sendTo+"from "+login+" "+body+"/n";        
                    worker.send(outMsg);   
               }

        else {  //direct message
            for (ServerWorker worker1 : workerList) {
                if (sendTo.equalsIgnoreCase(worker1.getLogin())) {
                    String outMsg = "msg from " + login + " = " + body + "\n";
                    worker1.send(outMsg);
                }
            }
        }
       }
    }
  }
    private void handleLogoff() throws IOException {
        server.removeWorker(this);
        List<ServerWorker> workerList = server.getWorkerList();

        // send other online users current user's status
        String offlineMsg = "OFFLINE " + login + "\n";
        for(ServerWorker worker : workerList) {
                worker.send(offlineMsg);
            }
    }

    public String getLogin() {
        return login;
    }

        private void send(String msg) throws IOException {
          OutputStream os=s.getOutputStream();
           os.write(msg.getBytes());
    }
      
      private void handleJoin(String[] tokens){
      String topic=tokens[1];
      topicSet.add(topic);
      try {
		handleMessage(tokens);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      }

      public boolean isMemberofTopic(String topic){
     return topicSet.contains(topic);
     }

     private void handleLeave(String[] tokens){
    String topic=tokens[1];
    topicSet.remove(topic);
    }
}
