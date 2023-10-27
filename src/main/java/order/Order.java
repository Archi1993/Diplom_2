package order;

public class Order { //оставил этот класс на всякий случай, в реальном проекте, возможно в будущем он пригодится

    private String ingredients;

    public Order(String ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
