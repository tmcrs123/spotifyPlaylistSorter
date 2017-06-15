package SpotifySongToPlaylistSorter.Runnables;

import SpotifySongToPlaylistSorter.Services.*;
import SpotifySongToPlaylistSorter.Models.Genres;
import SpotifySongToPlaylistSorter.Models.Playlist;
import SpotifySongToPlaylistSorter.Models.Track;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiagoRodrigues on 02/06/2017.
 */
public class OfflinePlaylistBuilder {

    static HashMap<String, Playlist> playlistHashMap;

    public static void main(String[] args) {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();

        int numberOfSongsInPlaylist = 15;



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



            //stringBuider to save track URI's
            StringBuilder stringBuilder = new StringBuilder();


            for (Playlist playlist : playlistHashMap.values()) {

                //A neat way of checking if a playlists matches a genre!

                boolean isPersonalPlaylist = Arrays.stream(Genres.values()).filter(x -> x.toString().equals(playlist.getName())).count() > 0;

                System.out.println(playlist.getName() + " is personal? " + isPersonalPlaylist);

                if (!isPersonalPlaylist) {
                    continue;
                }

                //get the name of the playlist, need to added it to the offline playlists
                String playlistName = playlist.getName();


                //get the id of the playlists

                String playlistId = playlist.getId();


                for (int i = 0; i < numberOfSongsInPlaylist; i++) {

                    if (i >= playlist.getTracks().getTotal()){
                        continue;
                    }

                    Track curTrack = playlistService.getRandomPlaylistTrack(playlist, userService.getUser().getId());

                    Thread.sleep(1000);

                    if (i == numberOfSongsInPlaylist - 1) {
                        System.out.println("here");
                        stringBuilder.append("spotify:track:" + curTrack.getId());
                        String offlinePlaylistId = offlinePlaylistId(playlist.getName());
                        playlistService.addTrackToOfflinePlaylist(userService.getUser().getId(), offlinePlaylistId, stringBuilder.toString());
                        stringBuilder.delete(0, stringBuilder.length());
                        playlistService.purgeSelectedRandomTrackIndex();
                    } else {
                        stringBuilder.append("spotify:track:" + curTrack.getId() + ",");
                    }


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

    private static String offlinePlaylistId(String playlistName){

        String offlinePlaylistName = "Offline"+playlistName;
        return playlistHashMap.get(offlinePlaylistName).getId();

    }

}
