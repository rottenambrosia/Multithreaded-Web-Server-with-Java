import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;

public class Client {

    private Runnable getRunnable() {
        return new Runnable()  {
            @Override
            public void run ()  {
                int port = 3098;
                try  {
                    InetAddress address = InetAddress.getByName("localhost");
                    Socket socket = new Socket(address, port);
                    socket.setReuseAddress(true);
                    PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toServer.println("Hello from Client." + socket.getLocalSocketAddress());
                    String message = fromServer.readLine();
                    System.out.println(message);
                    fromServer.close();
                    toServer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static void main (String[] args) {
        Client client = new Client();

        for (int i = 0; i <100; i++) {
            try {
                Thread thread = new Thread(client.getRunnable());
                                thread.start();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}
