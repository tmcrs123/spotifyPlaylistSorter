package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 21/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Playlist {

    String id;
    String name;
    int numberOfsongs;


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

    public int getNumberOfSongs(){
        return numberOfsongs;
    }

    public void setNumberOfsongs(int nSongs){
        numberOfsongs = nSongs;
    }
}
