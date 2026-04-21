package ch.hearc.ig.scl.service;

import ch.hearc.ig.scl.business.Pays;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCallPaysService {

    public static void callApiName(Pays pays) {
        if (pays.getCode() == null) return;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://db.ig.he-arc.ch/ens/scl/ws/country/" + pays.getCode()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();

                // Désérialiser directement le JSON en Pays partiel (seulement name)
                Pays p = mapper.readValue(response.body(), Pays.class);

                // Mettre à jour le nom du pays
                pays.setName(p.getName());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}