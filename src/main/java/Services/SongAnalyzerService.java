package Services;

import com.sun.tools.javac.jvm.Gen;
import fullModel.AnalyzerCounter;
import fullModel.Tests.Genres;
import fullModel.Track;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by tiagoRodrigues on 26/04/2017.
 */
public class SongAnalyzerService {

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

        /**
         * todo
         * Can't have spaces. " LIVE " != "Live "
         * */

        if (track.getName().contains(" live ") || track.getName().contains("Live ") || track.getName().contains(" Ao Vivo ")
                || track.getName().contains(" En Vivo ")) {
            return true;
        }

        return false;

    }

    public Genres genreSorter(Track track) {

        /*
        * todo: if genres are empty or no match return unspecified
        * todo: what happens if two genres have the same counter (tie) ?
        * */


        /*
        * Declarations:
        *
        * */

        AnalyzerCounter maxValueCounter = new AnalyzerCounter("NoGenre");

        /*
        * First step is to get tha names of the playlists because I need to define the counters
        * */

        Set<String> playlistNames = PlaylistService.playlistHashMap.keySet();
        ArrayList<AnalyzerCounter> counters = new ArrayList<>();

        /*
        * Secondly, I need to instantiate the counters with a given playlist name to identify them
        * */

        for (String playlistName : playlistNames) {

            counters.add(new AnalyzerCounter(playlistName));

        }

        //assuming that if the track has more than 1 artist, the first one is the one that defines the genre

        /*
        * check if genres are empty
        * */

        if (track.getArtists()[0].getGenres() == null){

            return Genres.UNSPECIFIED;
        }

        String[] mainArtistGenres = track.getArtists()[0].getGenres();

        /*
        * Next, I check if the genres for a given artist match any
        * of the counters I've defined.
        * If it matches then ++ the counter
        * */

        /*
        * REMARK:
        *
        * this won't conflic with spotify playlists because they are Camel Cased
        * And also you can't add songs to them
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
        * This returs n genre bue I need to pass a string that identifies it exactly as it is declared
        * */


        /*
        * If the genre is hip hop I need to return it differently because of spaces in enum
        * */

        //regex here %hip hop% ?

        if (maxValueCounter.getGenreName().equals("Hip Hop")) {

            System.out.println("Got here");

            return Genres.HIPHOP;

        } else if (maxValueCounter.getGenreName().equals("NoGenre")){

            return Genres.UNSPECIFIED;
        }


        return Genres.valueOf(Genres.class, maxValueCounter.getGenreName().toUpperCase());

    }


}
