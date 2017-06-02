package fullModel.Tests;

import Services.*;
import fullModel.Playlist;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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


                //get the name of the playlist, need to added it to the offline playlists
                String playlistName = playlist.getName();

                //get the id of the playlists

                String  playlistId = playlist.getId();

                //get the number of songs in playlist


                //get the id of the track at random position x
                //append it to a string builder

                StringBuilder stringBuilder = new StringBuilder();

                //add it to the corresponding playlist


                playlistService.addTrackToPlaylist(userService.getUser().getId(), playlistService.getPlaylists(userService.getUser().getId()));

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
