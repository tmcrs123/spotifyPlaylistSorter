package Services;

import com.sun.tools.javac.jvm.Gen;
import fullModel.AnalyzerCounter;
import fullModel.Tests.Genres;
import fullModel.Track;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by tiagoRodrigues on 26/04/2017.
 */
public class SongAnalyzerService {

    /*
    * The goal of this class is to define the genre of a track.
    *
    * The way I'm doing this is by defining the genre of the track as the most
    * frequent genre for an artist that matches wity my spotify playlists.
    *
    * For example Nina Simone has the following genres:
    * [ "adult standards", "christmas", "jazz blues", "jazz christmas", "soul", "soul jazz", "vocal jazz"]
    *
    * I have two playlist that match the genre Nina Simone has: Blues and Jazz
    *
    * So what I'm doing is counting the amount of times the strings "Blues" and "Jazz" show up in the array of genres.
    *
    * Since Jazz = 4 and Blues = 1, every song by Nina Simone will have its genre set to Jazz
    *
    * Advantages of this method: it's easy to do!
    *
    * Disadvantages: since I'm not actually dealing with the parameters of the track (danceability, loudness, tempo,etc)
    * I will not get the definition of all songs 100% accurate.
    *
    * For example, although Metallica is Metal, I wouldn't consider Nothing Else Matters a metal song.
    *
    * I'm given the following order of importance to a track (from most important to less important)
    *
    * 1 - Live track -> if a track is live, then it should go to Live playlists, regardless of the genre
    * 2 - Country Specific -> If the track is country specific, then it should go to a playlist of that country
    * (For example, any portuguese song should go to the Portuguese playlists, regardless of the genre)
    * 3 - Acoustic tracks -> if the name of the track contains the word "Acoustic", it should go to the Acoustic playlist
    * 4-  Genre playlist -> as explained above
    * */

    public Genres trackGenreAnalyzer(Track track) {

        System.out.println("\n" + "------ GENRE ANALYZER-----");

        System.out.println("This track has " + track.getArtists().length + " artists");
        System.out.println("Main artist is : " + track.getArtists()[0].getName());

        if (track.getArtists().length > 1) {
            System.out.println("Second artist is : " + track.getArtists()[1].getName());
        }

        System.out.println("Main artist genres are " + track.getArtists()[0].printGenres());
        System.out.println("-----END GENRE ANALYZER----" + "\n");


        //check if track is live

        if (liveTrack(track)) {
            return (Genres.LIVE);
        }

        return genreSorter(track);

    }


    public boolean liveTrack(Track track) {

        boolean liveTrack = false;

        System.out.println("Track name is : " + track.getName());

        if (track.getName().matches("(?i)(.*)- live(.*)") ||
                track.getName().matches("(?i)(.*)live from(.*)") ||
                track.getName().matches("(?i)(.*)live in(.*)") ||
                track.getName().matches("(?i)(.*)[(]live[)](.*)") ||
                track.getName().matches("(?i)(.*)live at(.*)") ||
                track.getName().matches("(?i)(.*)ao vivo(.*)")) liveTrack = true;

        return liveTrack;
    }

    public Genres genreSorter(Track track) {

        /*
        * todo: if genres are empty or no match return unspecified
        * todo: what happens if two genres have the same counter (tie) ?
        * */

        //Country specific genres
        if (track.getArtists()[0].printGenres().matches("(?i)(.*)portuguese(.*)")) {
            System.out.println("Returning Portuguese in country analyzer");
            return Genres.PORTUGUESE;
        } else if (track.getArtists()[0].printGenres().matches("(?i)(.*)german(.*)")) {
            return Genres.GERMAN;
        }

        if (track.getName().matches("(?i)(.*)acoustic(.*)")) return Genres.ACOUSTIC;


        /*
        * Declarations:
        *
        * */

        AnalyzerCounter maxValueCounter = new AnalyzerCounter("NoGenre");

        /*
        * First step is to get the names of my spotify playlists because I need to define the counters
        * */

        Set<String> playlistNames = PlaylistService.playlistHashMap.keySet();
        ArrayList<AnalyzerCounter> counters = new ArrayList<>();

        /*
        * Secondly, I need to instantiate the counters with a given playlist name to identify them
        * */

        for (String playlistName : playlistNames) {

            counters.add(new AnalyzerCounter(playlistName));

        }

        /*
        * check if genres are empty, then set the genre of the song as unspecified
        * the goal is to minimize the number of unspecified songs
        * */

        if (track.getFirstArtist().getGenres() == null) {

            return Genres.UNSPECIFIED;
        }

        String[] mainArtistGenres = track.getFirstArtist().getGenres();

        /*
        * Next, I check if the genres for a given artist match any
        * of the counters I've defined.
        * If it matches then ++ the counter
        * */

        for (String genre : mainArtistGenres) {

            for (AnalyzerCounter counter : counters) {


                if (genre.contains(counter.getGenreName().toLowerCase())) {
                    counter.incrementCounter();
                    System.out.println("incremented counter");
                }

            }

        }

        /*
        * Now, from all the counters check which one has the max value
        * and return the genre that matches that counter
        * */

        for (AnalyzerCounter counter : counters) {

            if (counter.getCounter() > maxValueCounter.getCounter()) {

                maxValueCounter.setGenreName(counter.getGenreName());
                maxValueCounter.setCounter(counter.getCounter());

            }

        }


        System.out.println("returning genre : " + maxValueCounter.getGenreName());

        /*
        * If the genre is hip hop I need to return it differently because of spaces in enum
        * */

        if (maxValueCounter.getGenreName().equals("Hip Hop")) {

            return Genres.HIPHOP;

        }

        /*
        * Otherwise return the genre defined in the maxValueCounter
        * */
        return Genres.valueOf(Genres.class, maxValueCounter.getGenreName().toUpperCase());

    }


}
