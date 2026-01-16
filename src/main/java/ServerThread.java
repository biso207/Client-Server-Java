import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.net.Socket;

public class ServerThread implements Runnable {    
    private final Socket clientSocket;
    
    // costruttore
    public ServerThread(Socket client) {
        this.clientSocket = client;
    }
    
    @Override
    public void run() {
        // legge 1 riga = 1 JSON
        try (
            Socket s = clientSocket;
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)
        ) 
        {
            String receivedLine = in.readLine(); 
            if (receivedLine == null) {
                System.out.println("[SERVER] Client disconnesso senza inviare dati.");
                return;
            }

            System.out.println("[SERVER] JSON ricevuto: " + receivedLine);

            // parsing messaggio ricevuto in JSON
            JsonObject receivedJson = JsonParser.parseString(receivedLine).getAsJsonObject();

            // stampa campi
            System.out.println("[SERVER] nome: " + receivedJson.get("nome").getAsString());
            System.out.println("[SERVER] cognome: " + receivedJson.get("cognome").getAsString());
            System.out.println("[SERVER] data: " + receivedJson.get("data").getAsString());
            System.out.println("[SERVER] numero: " + receivedJson.get("numero").getAsInt());

            // risposta sempre in JSON
            JsonObject response = new JsonObject();
            response.addProperty("nome", "server"); // 'server' essendo il server
            response.addProperty("cognome", "Bisognin"); // cognome studente
            response.addProperty("data", LocalDate.now().toString()); // data odierna
            response.addProperty("numero", 2); // numero nel registro

            // invia 1 riga = 1 JSON
            out.println(response);
            System.out.println("[SERVER] JSON inviato: " + response);
        }
        catch (Exception e) {
            System.err.println("[SERVER] Errore fatale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
