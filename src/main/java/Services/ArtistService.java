package Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fullModel.Artist;
import fullModel.SongLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tiagoRodrigues on 07/05/2017.
 */
public class ArtistService {


    public void getGenresforArtist(Artist artist) throws IOException {


        URL url = new URL("https://api.spotify.com/v1/artists/" + artist.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

        System.out.println("n" + response.toString()+"\n");


        ObjectMapper objectMapper = new ObjectMapper();

        Artist tempArtist = objectMapper.readValue(response.toString(),Artist.class);

        System.out.println(tempArtist.getName());

        artist.setGenres(tempArtist.getGenres());


        artist.printGenres();


    }


}
