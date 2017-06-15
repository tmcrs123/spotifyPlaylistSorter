package SpotifySongToPlaylistSorter.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
