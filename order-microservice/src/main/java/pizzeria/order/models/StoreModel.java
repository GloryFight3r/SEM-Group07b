package pizzeria.order.models;

import lombok.Data;
import pizzeria.order.domain.store.Store;

@Data
public class StoreModel {
    String location;
    String contact;
    public Store parseToStore() {
        return new Store(location, contact);
    }
}
