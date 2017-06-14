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

    /*
    * This is the class that connects to spotify to get the songLibrary (Your music -> songs) for a given user
    *
    * The reason why the getSongSet requires a dynamic requestUrl is because you don't get all your songs in
    * one go. You get a set of 20 songs at a time...and the URL for the next set of songs.
    *
    * So my idea here is to have only reference to a SongLibrary at the time. I don't need to keep
    * the past set of songs (aka some sort of container of SongLibrary's).
    *
    * Therefore, each time I get another set of songs, I'm pointing to the same SongLibrary var
    * and letting the garbage collector deal with the unused references
    *
    * */

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

        ObjectMapper objectMapper = new ObjectMapper();
        songLibrary = objectMapper.readValue(response.toString(), SongLibrary.class);

    }
}
