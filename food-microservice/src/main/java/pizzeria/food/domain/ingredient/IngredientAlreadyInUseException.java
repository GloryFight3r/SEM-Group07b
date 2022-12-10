package pizzeria.food.domain.ingredient;


public class IngredientAlreadyInUseException extends Exception{
    static final long serialVersionUID = -3437516993124229568L;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public IngredientAlreadyInUseException() {
        super("The ingredient is already stored in the database");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IngredientAlreadyInUseException(String message, String message1) {
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }


}
