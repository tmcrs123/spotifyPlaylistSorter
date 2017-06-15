package SpotifySongToPlaylistSorter.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 18/04/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SongLibrary {

    Item[] items;
    String next;
    public int total;
    public int offset;

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
