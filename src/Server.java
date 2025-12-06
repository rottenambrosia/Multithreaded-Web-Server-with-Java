import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public Consumer<Socket> getConsumer() {
        return  new Consumer<Socket>() {
            @Override
            public void accept(Socket socket) {
                try {
                    PrintWriter toClient = new PrintWriter(socket.getOutputStream());
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String acceptedMessage = fromClient.readLine();
                    System.out.println(acceptedMessage);
                    toClient.println("Hello from the other side");
                    toClient.flush();
                    toClient.close();
                    socket.close();
                    fromClient.close();
                } catch (IOException e) {
                   e.printStackTrace();
               }
            }
        };
//        return  (clientSocket) -> {
//            try {
//                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
//                toClient.println("Hello from server!");
//                toClient.flush();
//                toClient.close();
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
    }

    public static void main (String[] args) throws IOException {
        int port = 3098;
        Server server = new Server() ;
        Consumer<Socket> consumer = server.getConsumer() ;
            try {
                ServerSocket socket = new ServerSocket(port) ;
                System.out.println("Server started at port " + port);
                socket.setSoTimeout(10000);
                Thread thread = new Thread() ;
                while (true) {
                    Socket clientSocket = socket.accept() ;
                    thread = new Thread( () -> consumer.accept(clientSocket) ) ;
                    thread.start();
                }

            }
            catch (IOException exception) {
                exception.printStackTrace();
            }

    }
}
