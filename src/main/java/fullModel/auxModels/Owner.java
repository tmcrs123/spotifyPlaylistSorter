package fullModel.auxModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 06/06/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
