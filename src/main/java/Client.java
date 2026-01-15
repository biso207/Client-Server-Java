// classe Client per inviare messaggi in formato JSON al server

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Client {

    public static void main(String[] args) {
        final String HOST = "127.0.0.1";
        final int PORT = 5003;

        System.out.println("[CLIENT] Connessione a " + HOST + ":" + PORT + "...");

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            // Costruisce JSON (formato richiesto)
            JsonObject request = new JsonObject();
            request.addProperty("nome", "client"); // 'client' essendo il client
            request.addProperty("cognome", "Bisognin"); // cognome studente
            request.addProperty("data", LocalDate.now().toString()); // data odierna
            request.addProperty("numero", 2); // numero nel registro

            // Invia 1 riga = 1 JSON
            out.println(request.toString());
            System.out.println("[CLIENT] JSON inviato: " + request);

            // Legge risposta (1 riga = 1 JSON)
            String responseLine = in.readLine();
            if (responseLine == null) {
                System.out.println("[CLIENT] Nessuna risposta dal server.");
                return;
            }

            System.out.println("[CLIENT] JSON ricevuto: " + responseLine);

            // Parsing risposta
            JsonObject receivedJson = JsonParser.parseString(responseLine).getAsJsonObject();
            System.out.println("[CLIENT] nome: " + receivedJson.get("nome").getAsString());
            System.out.println("[CLIENT] cognome: " + receivedJson.get("cognome").getAsString());
            System.out.println("[CLIENT] data: " + receivedJson.get("data").getAsString());
            System.out.println("[CLIENT] numero: " + receivedJson.get("numero").getAsInt());

        } catch (Exception e) {
            System.err.println("[CLIENT] Errore: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("[CLIENT] Chiuso.");
    }
}
