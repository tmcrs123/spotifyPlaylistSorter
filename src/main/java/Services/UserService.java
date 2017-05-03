package Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fullModel.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tiagoRodrigues on 20/04/2017.
 */
public class UserService {

    User user;

    public void createUser() throws IOException {

        URL url = new URL("https://api.spotify.com/v1/me");
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + AuthService.accessToken.getAccessToken());

        connection.connect();

        BufferedReader breader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;

        StringBuilder response = new StringBuilder();

        while ((inputLine = breader.readLine()) != null) {
            response.append(inputLine);
        }

        breader.close();

        ObjectMapper objectMapper = new ObjectMapper();


        user =  objectMapper.readValue(response.toString(), User.class);

    }

    public User getUser() {
        return user;
    }
}
