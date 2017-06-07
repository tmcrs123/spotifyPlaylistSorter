package Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fullModel.Playlist;
import fullModel.PlaylistLibrary;
import fullModel.Tests.Genres;
import fullModel.Track;
import fullModel.auxModels.SingleTrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by tiagoRodrigues on 21/04/2017.
 */
public class PlaylistService {


    static HashMap<String, Playlist> playlistHashMap = new HashMap<>();
    HashMap<Genres, ArrayList<String>> tempSongContainer = new HashMap<>();


    public HashMap<String, Playlist> getPlaylists(String userId) throws IOException {


        URL url = new URL("https://api.spotify.com/v1/users/" + userId + "/playlists?limit=50");
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

//        System.out.println(response.toString());


        ObjectMapper objectMapper = new ObjectMapper();


        PlaylistLibrary playlistLibrary = objectMapper.readValue(response.toString(), PlaylistLibrary.class);

//        System.out.println(playlistLibrary.getItems().length);


        for (Playlist playlist : playlistLibrary.getItems()) {

            playlistHashMap.put(playlist.getName(), playlist);
        }

        return playlistHashMap;

    }

    public void addTrackToPlaylist(String userId, HashMap<String, Playlist> playlistHashMap) throws IOException {


        //do all the stuff related to building the add to playlist request
        StringBuilder stringBuilder = new StringBuilder();

//        String jsonURIArray = "\"uris\":[\"";
//        stringBuilder.append(jsonURIArray);

        Set<Genres> genresSet = tempSongContainer.keySet();

        for (Genres genre : genresSet) {

            ArrayList<String> iteratableGenreArrayList = tempSongContainer.get(genre);

            for (String uri : iteratableGenreArrayList) {

                //if the current uri is the last, then add it, close the array and end the JSON
//                System.out.println("last index is : " + iteratableGenreArrayList.lastIndexOf(uri));
//                System.out.println("size of the array list is: " + iteratableGenreArrayList.size());


                if (iteratableGenreArrayList.lastIndexOf(uri) == iteratableGenreArrayList.size() - 1) {
                    stringBuilder.append(uri);

                } else {
                    stringBuilder.append(uri + ",");
                }


            }

            if (stringBuilder.toString().equals("")) {
                continue;
            }
//            System.out.println("This is how the String builder looks like: " + stringBuilder.toString());

            String playlist_id = playlistHashMap.get(genre.toString()).getId();
//            System.out.println("Playlist id is: " + playlist_id);

            URL url = new URL("https://api.spotify.com/v1/users/" + userId + "/playlists/" + playlist_id + "/tracks?uris=" + stringBuilder.toString());
            HttpURLConnection connection;
            System.out.println("url is :" + url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "api.spotify.com");
            connection.setRequestProperty("Content-Length", "0");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, compress");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestProperty("Authorization", "Bearer " + AuthService.accessToken.getAccessToken());
//            connection.addRequestProperty("uris", stringBuilder.toString());
            connection.connect();


            System.out.println(connection.getResponseMessage());
            System.out.println(connection.getResponseCode());

            System.out.println(connection.getHeaderField("Retry-After"));
            //flush the string builder
            stringBuilder.delete(0, stringBuilder.length());

        }


    }


    public void addTrackToOfflinePlaylist(String userId , String playlistIdToAddTrackTo, String uris) {

            URL url = null;
            try {
//                POST https://api.spotify.com/v1/users/{user_id}/playlists/{playlist_id}/tracks

                url = new URL("https://api.spotify.com/v1/users/" + userId +"/playlists/" + playlistIdToAddTrackTo + "/tracks?uris=" + uris);
                System.out.println("adding track to offline playlist " + url.toString());
                HttpURLConnection connection;
                System.out.println("url is :" + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Host", "api.spotify.com");
                connection.setRequestProperty("Content-Length", "0");
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate, compress");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + AuthService.accessToken.getAccessToken());
                connection.connect();

                System.out.println(connection.getResponseCode());
                System.out.println(connection.getResponseMessage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        



    }

    public void addTrackToContainer(Genres genres, Track track) {

        if (!tempSongContainer.containsKey(genres)) {
            tempSongContainer.put(genres, new ArrayList<String>());
            System.out.println("created a new array list named " + genres.toString());
        }

        tempSongContainer.get(genres).add(track.getUri());
        System.out.println("added a track named " + track.getName() + " to array list");

    }

    public HashMap<Genres, ArrayList<String>> getTempSongContainer() {
        return tempSongContainer;
    }

    public void setTempSongContainer(HashMap<Genres, ArrayList<String>> tempSongContainer) {
        this.tempSongContainer = tempSongContainer;
    }

    public void purgeTempSongContainer() {

        for (ArrayList<String> song : tempSongContainer.values()) {

            song.clear();

        }

    }

    public Track getRandomPlaylistTrack(Playlist playlist, String userId) {

//        GET https://api.spotify.com/v1/users/{user_id}/playlists/{playlist_id}/tracks

        System.out.println("playlist is " + playlist.getName());
        System.out.println("playlist id is " + playlist.getId());
        System.out.println("playlist has " + playlist.getTracks().getTotal() + " tracks");
        System.out.println("playlists owner is " + playlist.getOwner().getId());
        int randomTrackIndex = ((int)Math.floor(Math.random()*playlist.getTracks().getTotal()));
        System.out.println("picking up song number " + randomTrackIndex);

        String  playlistOwner;

        if (playlist.getOwner().getId().equals("spotify")){
          playlistOwner = "spotify";
        } else{
            playlistOwner = userId;
        }

        Track randomTrackToReturn = new Track();

        try {
            URL url = new URL("https://api.spotify.com/v1/users/" + playlistOwner+ "/playlists/" + playlist.getId() + "/tracks?limit=1&offset=" + randomTrackIndex);
            HttpURLConnection connection;
            System.out.println(url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Host", "api.spotify.com");
            connection.setRequestProperty("Content-Length", "0");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Spotify API Console v0.1");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestProperty("Authorization", "Bearer " + AuthService.accessToken.getAccessToken());
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;

            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            SingleTrack curRandomTrack = objectMapper.readValue(response.toString(), SingleTrack.class);

            System.out.println("got a random track named " + curRandomTrack.getItems()[0].getTrack().getName());

            randomTrackToReturn = curRandomTrack.getItems()[0].getTrack();


            return randomTrackToReturn;



        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return randomTrackToReturn;
    }
}
