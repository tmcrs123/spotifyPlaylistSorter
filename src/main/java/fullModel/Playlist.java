package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fullModel.auxModels.Owner;
import fullModel.auxModels.Tracks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiagoRodrigues on 21/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Playlist {

    String id;
    String name;
    Tracks tracks;
    Owner owner;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
