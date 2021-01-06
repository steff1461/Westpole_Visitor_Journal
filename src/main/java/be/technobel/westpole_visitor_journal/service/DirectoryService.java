package be.technobel.westpole_visitor_journal.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DirectoryService {


    public Optional<JsonArray> infosFromDirectory() {

        Optional<String> token =
                authenticateToDirectory()
                        .map(stringBuilder -> new JsonObject(stringBuilder.toString()).getString("access_token"));

        if (token.isPresent()) {
            Optional<StringBuilder> response = retrieveData(token.get());
            return response.map(sb -> new JsonObject(sb.toString()).getJsonArray("value"));
        }

        return Optional.empty();
    }


    private Optional<StringBuilder> retrieveData(String token) {

        HttpURLConnection conn;
        String route = "https://graph.microsoft.com/v1.0/users?$select=displayName,givenName,mail";
        try {
            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String input;
            StringBuilder response = new StringBuilder();

            while ((input = in.readLine()) != null) {

                response.append(input);
            }

            in.close();

            return Optional.of(response);

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private Optional<StringBuilder> authenticateToDirectory() {

        HttpURLConnection conn;
        String route = "https://login.microsoftonline.com/acddd01c-116e-4298-a357-8844d217cf57/oauth2/v2.0/token";
        String parameters = "client_id=bc66dda2-1435-4e24-88dc-4b6269048f4d&scope=https%3A%2F%2Fgraph.microsoft" +
                ".com%2F.default&client_secret=om748~vcI99NEZ_Mvj~CsHL62~vofN0-~2&grant_type=client_credentials";

        byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);


        try {

            URL url = new URL(route);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            wr.write(postData);
            wr.close();

            StringBuilder content = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = br.readLine()) != null) {

                content.append(line);
                content.append(System.lineSeparator());
            }

            br.close();
            return Optional.of(content);

        } catch (IOException e) {

            return Optional.empty();
        }

    }

}
