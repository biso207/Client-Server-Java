// classe Server sempre in ascolto che riceve messaggi in formato JSON da un client

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

        System.out.println("[SERVER] Avvio sulla porta " + PORT + " (sempre in ascolto)...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                System.out.println("[SERVER] In attesa di un client...");

                // Accetta una nuova connessione (bloccante)
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter out = new PrintWriter(
                             new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                    System.out.println("[SERVER] Client connesso: " + clientSocket.getInetAddress());

                    // Legge 1 riga = 1 JSON
                    String receivedLine = in.readLine();
                    if (receivedLine == null) {
                        System.out.println("[SERVER] Client disconnesso senza inviare dati.");
                        continue; // torna in ascolto
                    }

                    System.out.println("[SERVER] JSON ricevuto: " + receivedLine);

                    // Parsing JSON
                    JsonObject receivedJson = JsonParser.parseString(receivedLine).getAsJsonObject();

                    // (facoltativo) stampa campi
                    System.out.println("[SERVER] nome: " + receivedJson.get("nome").getAsString());
                    System.out.println("[SERVER] cognome: " + receivedJson.get("cognome").getAsString());
                    System.out.println("[SERVER] data: " + receivedJson.get("data").getAsString());
                    System.out.println("[SERVER] numero: " + receivedJson.get("numero").getAsInt());

                    // Risposta (stesso formato)
                    JsonObject response = new JsonObject();
                    response.addProperty("nome", "server"); // 'server' essendo il server
                    response.addProperty("cognome", "Bisognin"); // cognome studente
                    response.addProperty("data", LocalDate.now().toString()); // data odierna
                    response.addProperty("numero", 2); // numero nel registro

                    // Invia 1 riga = 1 JSON
                    out.println(response);
                    System.out.println("[SERVER] JSON inviato: " + response);

                } catch (Exception e) {
                    // errori solo del singolo client: il server NON si ferma
                    System.err.println("[SERVER] Errore gestione client: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("[SERVER] Errore fatale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
