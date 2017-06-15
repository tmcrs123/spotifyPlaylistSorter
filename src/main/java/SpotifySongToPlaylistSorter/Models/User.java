package SpotifySongToPlaylistSorter.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 12/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String display_name;
    private String id;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
