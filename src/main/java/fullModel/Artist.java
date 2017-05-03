package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 18/04/2017.
 */
@JsonIgnoreProperties({"external_urls","followers","href","images","popularity","type","uri"})
public class Artist {

    String id;
    String name;
    String genres[];


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

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String printGenres(){

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < getGenres().length; i++) {

            stringBuilder.append(genres[i]+", ");

        }

        return stringBuilder.toString();
    }
}
