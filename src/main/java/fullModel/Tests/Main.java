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

    public static void main(String[] args) {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();

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


            /*
            * Get the first songs in the library
            * */
            songLibraryService.getSongSet(firstRequestURL);

            SongLibrary firstSetofSongs = songLibraryService.songLibrary;

            Track firstTrack = firstSetofSongs.getItems()[0].getTrack();

            System.out.println(firstTrack.getName());

            System.out.println("track id : " + firstTrack.getId());

            firstTrack.setGenre(songAnalyzerService.trackGenreAnalyzer(firstTrack));

            System.out.println(firstTrack.getGenre());

            playlistService.addTrackToContainer(firstTrack.getGenre() , firstTrack);

            ArrayList<String> unspecifiedURI = playlistService.getTempSongContainer().get(Genres.UNSPECIFIED);


            System.out.println("Size is " + unspecifiedURI.size());

            for (String uri : unspecifiedURI){

                System.out.println(uri);
            }

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
