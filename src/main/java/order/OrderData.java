package order;


import java.util.List;

public class OrderData {
    protected static final String FIRST_INGREDIENT = "61c0c5a71d1f82001bdaaa6d";
    protected static final String SECOND_INGREDIENT = "61c0c5a71d1f82001bdaaa70";
    protected static final String THIRD_INGREDIENT = "61c0c5a71d1f82001bdaaa73";
    protected static final Order ORDER_WITH_ALL_CORRECT_INGREDIENTS = new Order(List.of(FIRST_INGREDIENT, SECOND_INGREDIENT, THIRD_INGREDIENT));
    protected static final Order ORDER_WITH_ALL_INCORRECT_INGREDIENTS = new Order(List.of(FIRST_INGREDIENT + "test1", SECOND_INGREDIENT + "test2", THIRD_INGREDIENT + "test3"));

}
