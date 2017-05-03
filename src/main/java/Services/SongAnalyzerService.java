package Services;

import fullModel.Tests.Genres;
import fullModel.Track;

/**
 * Created by tiagoRodrigues on 26/04/2017.
 */
public class SongAnalyzerService {

    public Genres trackGenreAnalyzer(Track track){


        //check if track is live

        if (liveTrack(track)){
            return (Genres.LIVE);
        }


        //check if track belongs to a specific country playlist

        //analyze the genres of the song

        return Genres.UNSPECIFIED;

    }


    public boolean liveTrack(Track track){

        if (track.getName().contains(" live ") || track.getName().contains(" Live ") || track.getName().contains(" Ao Vivo ")){
            return true;
        }

        return false;

    }


}
