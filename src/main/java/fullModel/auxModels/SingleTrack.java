package fullModel.auxModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fullModel.Item;

/**
 * Created by tiagoRodrigues on 06/06/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleTrack {

    Item[] items;

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}
