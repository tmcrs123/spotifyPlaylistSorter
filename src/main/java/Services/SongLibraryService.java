package Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fullModel.SongLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tiagoRodrigues on 20/04/2017.
 */
public class SongLibraryService {

    public SongLibrary songLibrary;
    public HttpURLConnection connection = null;



    public void getSongSet(String requestURL) throws IOException {

        URL url = new URL(requestURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + AuthService.accessToken.getAccessToken());
        connection.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;

        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        System.out.println(response.toString()+"\n");


        ObjectMapper objectMapper = new ObjectMapper();
        songLibrary = objectMapper.readValue(response.toString(), SongLibrary.class);

//        System.out.println("Reference to song Library is " + songLibrary.toString());

    }
}
