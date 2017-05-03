package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by tiagoRodrigues on 21/04/2017.
 */
@JsonIgnoreProperties({"href","limit","next","offset","previous","total"})
public class PlaylistLibrary {

    Playlist[] items;

    public Playlist[] getItems() {
        return items;
    }

    public void setItems(Playlist[] items) {
        this.items = items;
    }


}
