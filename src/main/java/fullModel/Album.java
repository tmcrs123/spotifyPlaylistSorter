package fullModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import oracle.jrockit.jfr.StringConstantPool;

/**
 * Created by tiagoRodrigues on 18/04/2017.
 */
@JsonIgnoreProperties({"album_type","artists","available_markets","copyrights","external_ids","external_urls","href","images","label","popularity","release_date","release_date_\n" +
        "precision","tracks","type","uri","genres"})
public class Album {

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
