package ch.hearc.ig.scl.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCall {
    public ApiCall() {}

    public static HttpResponse<String> callAPI(Double lat, Double lon) {
        HttpResponse<String> response = null;
        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();
        // Construire une requête HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=97df5997cc0276ef6380b0772555044d"))
                .build();
        // Envoyer la requête et obtenir la réponse
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
