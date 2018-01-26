package SpotifySongToPlaylistSorter.Runnables;

import SpotifySongToPlaylistSorter.Services.*;
import SpotifySongToPlaylistSorter.Models.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiagoRodrigues on 20/04/2017.
 */
public class SongSorter {

    public static void main(String[] args) throws ParseException {

        //SpotifySongToPlaylistSorter.Services Instantiion
        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SongLibraryService songLibraryService = new SongLibraryService();
        PlaylistService playlistService = new PlaylistService();
        SongAnalyzerService songAnalyzerService = new SongAnalyzerService();
        ArtistService artistService = new ArtistService();


        //Helpers
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        /*
        * change this date if you just want to work with songs from a given data onwards
        * */
        Date lastRunDate = df.parse("2018-01-24");

        boolean noMoreSongsToAdd = false;
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
            String UserId = userService.getUser().getId();


            /*
            * Get the playlist Mapping and save it in a hashmap
            * */
            playlistHashMap = playlistService.getPlaylists(UserId);



            /*
            *Start populating the Song Library with items. Executing getSongSet with
            * first request url triggers that
            * */

            songLibraryService.getSongSet(firstRequestURL);


        do{

            /*
            * Define a songLibrary variable, that holds the current set of songs
            * This contents of this variable will be replaced when getting the next set of songs
            * */
            SongLibrary curSongSet = songLibraryService.songLibrary;


            /*
            * Define an array of items. This array is what holds the track and the date
            * that track was added. In my model Item = Track + Added_At
            * */
            Item[] curItemSet = curSongSet.getItems();


                /*
                * Now lets process each track in the Item array
                * */

                for (Item item : curItemSet) {


                   ////comment out this bit to add all library songs
//                    if (!isCurrentDateAfterLastRunDate(lastRunDate, item.getAdded_at())) {
//                        noMoreSongsToAdd = true;
//                        break;
//                    }

                    //get the track from the item
                    Track curTrack = item.getTrack();


                    //get the FIRST artist for the current track

                    /*
                    * A track has an array of artist because some tracks have
                    * more than one artist. For example Pentatonix ft. Lindsey Stirling - Radioactive
                    * is a track with more than 1 artist. I'm always getting the first artist,
                    * because i'm considering that it is the most important for a given track.
                    * */
                    Artist curArtist = item.getTrack().getFirstArtist();

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


                playlistService.addTrackToPlaylist(userService.getUser().getId(), playlistService.getPlaylists(userService.getUser().getId()));

                //comment out this bit to add all library songs to correct playlist
//            if (noMoreSongsToAdd) {
//                System.out.println("All songs after last run date added");
//                return;
//            }

                playlistService.purgeTempSongContainer();

                if (songLibraryService.songLibrary.getNext() == null){
                    System.out.println("All songs processed. Exiting...");
                    return;
                }

                songLibraryService.getSongSet(curSongSet.getNext());

                Thread.sleep(1000);

            } while (songLibraryService.songLibrary.getItems().length != 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Date parseDate(String spotifyDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String[] splitDate = spotifyDate.split("T");

        Date date = dateFormat.parse(splitDate[0]);

        return date;

    }

    private static boolean isCurrentDateAfterLastRunDate(Date lastRunDate, String trackAddedDate) {

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
