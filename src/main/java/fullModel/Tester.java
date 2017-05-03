package fullModel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiagoRodrigues on 06/04/2017.
 */
public class Tester {


    public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
        // Replace these with your client id and secret
        final String clientId = "fd2300e87f114d69a48e2d013e942a4f";
        final String clientSecret = "d66504db87a144b7bfa4e1959c2fe7f6";
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId).apiSecret(clientSecret)
                .scope("user-read-private") // replace with desired scope
                .callback("http://localhost:8080/")
                .state("some_params")
                .build(SpotifyApi20.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== SPOTIFY " + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();


        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");


        System.out.println("this is my auth code");

        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");


        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();


        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        //Make a cURL for the profile

        URL url = new URL("https://api.spotify.com/v1/me");
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken.getAccessToken());

        connection.connect();

        BufferedReader breader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;

        StringBuilder response = new StringBuilder();

        while ((inputLine = breader.readLine()) != null) {
            response.append(inputLine);
        }

        breader.close();

        ObjectMapper objectMapper = new ObjectMapper();
        User tiago = objectMapper.readValue(response.toString(), User.class);

        System.out.println(tiago.getDisplay_name());

        SongLibrary songLibrary = null;


        try {
            URL url2 = new URL("https://api.spotify.com/v1/me/tracks");
            HttpURLConnection connection2 = null;
            connection2 = (HttpURLConnection) url2.openConnection();
            connection2.setRequestMethod("GET");
            connection2.setRequestProperty("Accept", "application/json");
            connection2.setRequestProperty("limit", "10");
            connection2.setRequestProperty("Authorization", "Bearer " + accessToken.getAccessToken());

            connection2.connect();

            BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));

            String inputLine2;

            StringBuilder response2 = new StringBuilder();

            while ((inputLine2 = in2.readLine()) != null) {
                response2.append(inputLine2);
            }

            in2.close();

            System.out.println(response2.toString());


            ObjectMapper objectMapper2 = new ObjectMapper();
            songLibrary = objectMapper2.readValue(response2.toString(), SongLibrary.class);



            /*
            * Get the artist genres
            * */

            for (int i = 0 ; i < songLibrary.items.length ; i++) {

                System.out.println("IN FOR");

                String curId = songLibrary.items[i].track.artists[0].id;
                Artist curArtist = songLibrary.items[i].track.artists[0];

                URL url3 = new URL("https://api.spotify.com/v1/artists/"+curId);
                HttpURLConnection connection3 = null;
                connection3 = (HttpURLConnection) url3.openConnection();
                connection3.setRequestMethod("GET");
                connection3.setRequestProperty("Accept", "application/json");
                connection3.setRequestProperty("Authorization", "Bearer " + accessToken.getAccessToken());

                connection3.connect();

                BufferedReader in3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()));

                String inputLine3;

                StringBuilder response3 = new StringBuilder();

                while ((inputLine3 = in3.readLine()) != null) {
                    response3.append(inputLine3);
                }

                in3.close();


                ObjectMapper objectMapper3 = new ObjectMapper();

                curArtist = objectMapper3.readValue(response3.toString(), Artist.class);

                System.out.println("Artist is " + curArtist.getName());
                System.out.println("Artist ID is " + curArtist.getId());
                System.out.println("Artist Genres are "+ curArtist.printGenres());






            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

