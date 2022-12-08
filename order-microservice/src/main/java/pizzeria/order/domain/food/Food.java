package pizzeria.order.domain.food;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name="id")
    @Getter
    private long id;

    @Getter
    private long recipeId;

    @ElementCollection
    @CollectionTable(name = "baseIngredients",
            joinColumns = @JoinColumn(name = "id"))
    @Column(name = "baseIngredients")
    @Getter
    private List<Long> baseIngredients;

    @ElementCollection
    @CollectionTable(name = "extraIngredients",
            joinColumns = @JoinColumn(name = "id"))
    @Column(name = "extraIngredients")
    @Getter
    private List<Long> extraIngredients;

    @Getter
    private enum foodType {PIZZA};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return id == food.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
