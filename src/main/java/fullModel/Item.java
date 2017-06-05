package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 18/04/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class Item {

    String added_at;
    Track track;

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }


    @Override
    public String toString(){


        return "Artist is: " + track.artists +"\n"+
                "Song name is :" + track.name;
    }
}
