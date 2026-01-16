// classe Server sempre in ascolto che riceve messaggi in formato JSON da un client
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        final int PORT = 5003; // porta
            
        System.out.println("[SERVER] Avvio sulla porta " + PORT + " (sempre in ascolto)...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {                   
            // loop infinito per il server sempre in ascolto                
            while (true) {
                System.out.println("[SERVER] In attesa di un client...");
                Socket clientSocket = serverSocket.accept(); // accept del messaggio client
                System.out.println("[SERVER] Client connesso: " + clientSocket.getInetAddress());

                // creazione di un thread
                Thread t = new Thread(new ServerThread(clientSocket));
                t.start(); // avvio del thread
            }
        } catch (Exception e) {
            System.err.println("[SERVER] Errore fatale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
