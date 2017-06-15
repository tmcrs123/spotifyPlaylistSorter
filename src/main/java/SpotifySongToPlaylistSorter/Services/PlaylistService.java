package SpotifySongToPlaylistSorter.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import SpotifySongToPlaylistSorter.Models.Playlist;
import SpotifySongToPlaylistSorter.Models.PlaylistLibrary;
import SpotifySongToPlaylistSorter.Models.Genres;
import SpotifySongToPlaylistSorter.Models.Track;
import SpotifySongToPlaylistSorter.auxModels.SingleTrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by tiagoRodrigues on 21/04/2017.
 */
public class PlaylistService {

    /*
    * This class has multiple goals:
    * 1 - Get the playlists a user has in spotify
    * 2 - Add tracks to spotify playlists
    * 3 - Retrieve a random track from a playlists
    * 4 - General utilities (add tracks to a temporary container and purging that container)
    * */


   //Declarations
    static HashMap<String, Playlist> playlistHashMap = new HashMap<>();
    private HashMap<Genres, ArrayList<String>> tempSongContainer = new HashMap<>();
    private ArrayList<Integer> selectedRandomTrackIndex = new ArrayList<>();

    //Getters and Setters
    public HashMap<Genres, ArrayList<String>> getTempSongContainer() {
        return tempSongContainer;
    }

    public void setTempSongContainer(HashMap<Genres, ArrayList<String>> tempSongContainer) {
        this.tempSongContainer = tempSongContainer;
    }


    //Methods


    /*
    * Retrive playlists from spotify
    * */
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


        ObjectMapper objectMapper = new ObjectMapper();


        PlaylistLibrary playlistLibrary = objectMapper.readValue(response.toString(), PlaylistLibrary.class);


        for (Playlist playlist : playlistLibrary.getItems()) {

            playlistHashMap.put(playlist.getName(), playlist);
        }

        return playlistHashMap;

    }

    /*
    * Add tracks to a spotify playlists
    *
    * What I'm doing here is building a key value pair that holds as key the genre of the playlist
    * and as value all the spotify track uri's I need to send as a query parameter
    *
    * I'm using the string builder to build the really large string of uri's I need to send back to the
    * spotify API and, since I'm reusing it, I need to flush it after sending the request
    * */
    public void addTrackToPlaylist(String userId, HashMap<String, Playlist> playlistHashMap) throws IOException {


        StringBuilder stringBuilder = new StringBuilder();

        Set<Genres> genresSet = tempSongContainer.keySet();

        for (Genres genre : genresSet) {

            ArrayList<String> iterableGenreArrayList = tempSongContainer.get(genre);

            for (String uri : iterableGenreArrayList) {


                if (iterableGenreArrayList.lastIndexOf(uri) == iterableGenreArrayList.size() - 1) {
                    stringBuilder.append(uri);

                } else {
                    stringBuilder.append(uri);
                    stringBuilder.append(",");
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
            connection.connect();

            System.out.println("Message" + connection.getResponseMessage());
            System.out.println("Code" + connection.getResponseCode());

            //flush the string builder
            stringBuilder.delete(0, stringBuilder.length());

        }
    }


    /*
    * The goal of this class is to add all the randomly picked songs from the Genre playlists to the Offline Genre playlist
    * Ex: What this does is to all the random songs I got from "Rock" to "OfflineRock"
    * */
    public void addTrackToOfflinePlaylist(String userId , String playlistIdToAddTrackTo, String uris) throws IOException {

            URL url = null;


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
    }

    /*
    * Helper method to add songs to the tempSongContainer
    * */
    public void addTrackToContainer(Genres genres, Track track) {

        if (!tempSongContainer.containsKey(genres)) {
            tempSongContainer.put(genres, new ArrayList<String>());
            System.out.println("created a new array list named " + genres.toString());
        }

        tempSongContainer.get(genres).add(track.getUri());
        System.out.println("added a track named " + track.getName() + " to array list");

    }


    /*
    * Helper method to purge the tempSongContainer
    * */
    public void purgeTempSongContainer() {

        for (ArrayList<String> song : tempSongContainer.values()) {
            song.clear();
        }
    }

    /*
    * The goal of this class is to pick a random track from the genre playlists to the
    * offline genre playlists, without repetition
    * */
    public Track getRandomPlaylistTrack(Playlist playlist, String userId) throws IOException {

        System.out.println("playlist is " + playlist.getName());
        System.out.println("playlist id is " + playlist.getId());
        System.out.println("playlist has " + playlist.getTracks().getTotal() + " tracks");
        System.out.println("playlists owner is " + playlist.getOwner().getId());

        int randomTrackIndex = ((int)Math.floor(Math.random()*playlist.getTracks().getTotal()));

        while (selectedRandomTrackIndex.contains(randomTrackIndex)){
            randomTrackIndex = ((int)Math.floor(Math.random()*playlist.getTracks().getTotal()));
        }

        selectedRandomTrackIndex.add(randomTrackIndex);


        System.out.println("picking up song number " + randomTrackIndex);

        String  playlistOwner;

        if (playlist.getOwner().getId().equals("spotify")){
          playlistOwner = "spotify";
        } else{
            playlistOwner = userId;
        }

        Track randomTrackToReturn;

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
    }

    public void purgeSelectedRandomTrackIndex() {
            selectedRandomTrackIndex.clear();
    }
}
