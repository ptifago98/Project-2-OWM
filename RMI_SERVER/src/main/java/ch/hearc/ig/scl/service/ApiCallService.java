package ch.hearc.ig.scl.service;

import ch.hearc.ig.scl.tools.EnvProperties;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCallService {
    private final static String APIKEY = EnvProperties.get("APIKEY");

    public ApiCallService() {}

    public static HttpResponse<String> callAPI(Double lat, Double lon){
        HttpResponse<String> response = null;
        // Créer un client HTTP
        HttpClient client = HttpClient.newHttpClient();
        // Construire une requête HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + APIKEY))
                .build();
        // Envoyer la requête et obtenir la réponse
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
