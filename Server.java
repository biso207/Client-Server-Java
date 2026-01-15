import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Server {

    public static void main(String[] args) {
        final int PORT = 5003;

        System.out.println("[SERVER] Avvio sulla porta " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("[SERVER] Client connesso: " + clientSocket.getInetAddress());

            // Legge 1 riga = 1 JSON
            String receivedLine = in.readLine();
            if (receivedLine == null) {
                System.out.println("[SERVER] Nessun dato ricevuto.");
                return;
            }

            System.out.println("[SERVER] JSON ricevuto: " + receivedLine);

            // Parsing JSON
            JsonObject receivedJson = JsonParser.parseString(receivedLine).getAsJsonObject();
            System.out.println("[SERVER] nome: " + receivedJson.get("nome").getAsString());
            System.out.println("[SERVER] cognome: " + receivedJson.get("cognome").getAsString());
            System.out.println("[SERVER] data: " + receivedJson.get("data").getAsString());
            System.out.println("[SERVER] numero: " + receivedJson.get("numero").getAsInt());

            // Costruisce risposta (stesso formato)
            JsonObject response = new JsonObject();
            response.addProperty("nome", "server");
            response.addProperty("cognome", "TUO_COGNOME");
            response.addProperty("data", LocalDate.now().toString());
            response.addProperty("numero", 1); // metti il tuo numero nel registro

            // Invia 1 riga = 1 JSON
            out.println(response.toString());
            System.out.println("[SERVER] JSON inviato: " + response);

        } catch (Exception e) {
            System.err.println("[SERVER] Errore: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("[SERVER] Chiuso.");
    }
}
