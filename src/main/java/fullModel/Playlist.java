package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.HashMap;

/**
 * Created by tiagoRodrigues on 21/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Playlist {

    String id;
    String name;


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
}
