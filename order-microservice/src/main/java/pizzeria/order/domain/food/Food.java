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

    @Getter
    private long orderId;

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

    /**
     * Food constructor for testing purposes
     *
     * @param id the food id
     * @param recipeId the id of the recipe this food is based on
     * @param orderId the id of the order this food belongs to
     * @param base the list of base ingredient ids
     * @param extra the list of extra ingredient ids
     */
    public Food(long id, long recipeId, long orderId, List<Long> base, List<Long> extra){
        this.id = id;
        this.recipeId = recipeId;
        this.orderId = orderId;
        this.baseIngredients = base;
        this.extraIngredients = extra;
    }

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
