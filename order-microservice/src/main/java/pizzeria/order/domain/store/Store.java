package pizzeria.order.domain.store;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="stores")
public class Store {

    @Id
    @Column(name = "id")
    @NotNull
    @Getter
    @Setter
    private long id;

    @Column(name = "location")
    @Getter
    @Setter
    private String location;

    @Column(name = "contact")
    @Getter
    @Setter
    private String contact;

    public Store() {}

    public Store(int id, String location, String contact) {
        this.id = id;
        this.location = location;
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return id == store.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
