package fullModel.Tests;

import Services.*;
import fullModel.Playlist;
import fullModel.Track;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * Created by tiagoRodrigues on 02/06/2017.
 */
public class TempPlaylistBuilder {

    public static void main(String[] args) {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();

        int numberOfSongsInPlaylist = 20;
        HashMap<String, Playlist> playlistHashMap;


        String firstRequestURL = "https://api.spotify.com/v1/me/tracks";

        try {

            /*
            * Do oauth process and get token with required scopes
            * */
            authService.doAuthentication();

            /*
            * Create a user, the entity where the user_id is saved
            * */
            userService.createUser();


            /*
            * Get the playlist Mapping and save it in a hashmap
            * */
            playlistHashMap = playlistService.getPlaylists(userService.getUser().getId());


            for (Playlist playlist : playlistHashMap.values()) {

                //A neat way of checking if a playlists matches a genre!

                boolean isPersonalPlaylist = Arrays.stream(Genres.values()).filter(x -> x.toString().equals(playlist.getName())).count() > 0;

                if (!isPersonalPlaylist) {
                    continue;
                }

                //get the name of the playlist, need to added it to the offline playlists
                String playlistName = playlist.getName();


                //get the id of the playlists

                String playlistId = playlist.getId();


                for (int i = 0; i < numberOfSongsInPlaylist; i++) {
                    Track curTrack = playlistService.getRandomPlaylistTrack(playlist, userService.getUser().getId());
                    //add this song to some sort of map
                }

                //call the method below to add all songs to playlist at once


//                playlistService.addTrackToPlaylist(userService.getUser().getId(), playlistService.getPlaylists(userService.getUser().getId()));

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
