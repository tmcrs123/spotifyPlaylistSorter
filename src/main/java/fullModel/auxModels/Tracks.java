package fullModel.auxModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by tiagoRodrigues on 05/06/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tracks {

    int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
