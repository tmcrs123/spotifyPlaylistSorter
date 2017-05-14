package fullModel.Tests;

import Services.*;
import fullModel.Item;
import fullModel.Playlist;
import fullModel.SongLibrary;
import fullModel.Track;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiagoRodrigues on 20/04/2017.
 */
public class Main {

    /*
    * todo: Features
    * Check for duplicates
    * Create playlist for artist based on the songs I have from them on my library
    * Limit the number of URI's that can go in the ulr query
    * Draw 20 feature
    *
    * */

    public static void main(String[] args) {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();
        ArtistService artistService = new ArtistService();

        String firstRequestURL = "https://api.spotify.com/v1/me/tracks";

        HashMap<String, Playlist> playlistHashMap;

        ZoneId zoneId = ZoneId.of("UTC");

        String teste = ZonedDateTime.of(2017, 4, 1, 0, 0, 0, 0, zoneId).format(DateTimeFormatter.ISO_INSTANT);

        //todo: pode haver aqui um check para ver se a data é válida


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



            //WHILE STARTS HERE

            /*
            * Get the first songs in the library
            * */
            songLibraryService.getSongSet(firstRequestURL);


            SongLibrary firstSetofSongs = songLibraryService.songLibrary;


            Track firstTrack = firstSetofSongs.getItems()[0].getTrack();

            System.out.println("Track name is: " + firstTrack.getName());

            System.out.println("track id : " + firstTrack.getId());

            artistService.getGenresforArtist(firstTrack.getArtists()[0]);



            /**
             * Set the genre of the track
             * */
            firstTrack.setGenre(songAnalyzerService.trackGenreAnalyzer(firstTrack));

            System.out.println("I've set my genre definition to " + firstTrack.getGenre());

            /*
            * Add it to the temp song container
            * */

            playlistService.addTrackToContainer(firstTrack.getGenre() , firstTrack);


            /*
            * Get all the tracks from a given genre
            * */
//            for (int i = 0; i < genres.length; i++){
//
//
//                ArrayList<String> genresURI = playlistService.getTempSongContainer().get(genres[i]);
//
//
//            }



            /*
            * Add it to the corresponding playlist
            * */

            playlistService.addTrackToPlaylist(userService.getUser().getId(),playlistService.getPlaylists(userService.getUser().getId()));













            //todo:I got the first song set here but need to process it




            /*
            * while there are more songs get them*/
//            while (songLibraryService.songLibrary.getNext() != null) {
//                songLibraryService.getSongSet(songLibraryService.songLibrary.getNext());
//
//                for (Item item : songLibraryService.songLibrary.getItems()) {
//
//                    item.getTrack().setGenre(songAnalyzerService.trackGenreAnalyzer(item.getTrack()));
//                    playlistService.addTrackToContainer(item.getTrack().getGenre(),item.getTrack());
//
//                    playlistService.addTrackToPlaylist(userService.getUser().getId(),playlistHashMap);
//
//
//
//                /*
//                * <0 a musica foi adicionada ANTES da data definida
//                * >0 a musica foi adicionada DEPOIS da data
//                * */
////                    if (item.getAdded_at().compareTo(teste) > 0) {
////                        System.out.println("Processing songs...");
////
////                    } else {
////                        System.out.println("No more songs to process");
////                        return;
////                    }
//                }
//            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
