package fullModel.Tests;

import Services.*;
import fullModel.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;

import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public static void main(String[] args) throws ParseException {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();
        ArtistService artistService = new ArtistService();
        int counter = 0;

        String firstRequestURL = "https://api.spotify.com/v1/me/tracks";


        HashMap<String, Playlist> playlistHashMap;

        //Helpers
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date lastRunDate = df.parse("2017-05-29");
        boolean noMoreSongsToAdd = false;


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
            *Start populating the Song Library with items. Executing getSongSet with
            * first request url triggers that
            * */

            songLibraryService.getSongSet(firstRequestURL);




            /*
            * start doing stuff until there aren't no more references to the next song set
            * */
            while (songLibraryService.songLibrary.getNext() != null && counter != songLibraryService.songLibrary.total) {


                SongLibrary curSongSet = songLibraryService.songLibrary;


                Item[] curItemSet = curSongSet.getItems();


                List<Track> tracks = new ArrayList<>();

                /*
                * For each track in the array of items, do stuff
                * */

                for (Item item : curItemSet) {

                    counter++;

                    //comment out this bit to add all library songs
//                    if (!isCurrentDateAfterLastRunDate(lastRunDate, item.getAdded_at())) {
//                        noMoreSongsToAdd = true;
//                        break;
//                    }

                    //get the track from the item
                    Track curTrack = item.getTrack();


//                    try {
//                        //get the date the track was added from the item
//                        Date curTrackAddedDate = parseDate(item.getAdded_at());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                    //get the FIRST artist for the current track
                    Artist curArtist = item.getTrack().getArtists()[0];

                    //get the genres for that artist
                    artistService.getGenresforArtist(curArtist);

                    //set the genre of the current track
                    curTrack.setGenre(songAnalyzerService.trackGenreAnalyzer(curTrack));


                    //added the current track to the temp container of tracks

                    playlistService.addTrackToContainer(curTrack.getGenre(), curTrack);

                    /*
                    * Now set the reference of songLibrary to the next song library set
                    * */
                }
/*
* Can I run this method in the end when all tracks have a defined genre???
* */

                playlistService.addTrackToPlaylist(userService.getUser().getId(), playlistService.getPlaylists(userService.getUser().getId()));

                //comment out this bit to add all library songs to correct playlist
//                if (noMoreSongsToAdd) {
//                    System.out.println("All songs after last run date added");
//                    return;
//                }

                playlistService.purgeTempSongContainer();

                songLibraryService.getSongSet(curSongSet.getNext());

                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Date parseDate(String spotifyDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String[] splitDate = spotifyDate.split("T");

        Date date = dateFormat.parse(splitDate[0]);

        return date;

    }

    static boolean isCurrentDateAfterLastRunDate(Date lastRunDate, String trackAddedDate) {

        boolean isAfterRunDate = false;

        try {
            Date dateAdded = parseDate(trackAddedDate);

            if (dateAdded.after(lastRunDate)) {
                isAfterRunDate = true;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isAfterRunDate;
    }
}
