package recipes;

import java.util.Objects;

public class RecipeValidator {
  public static boolean validateRecipe(Recipe recipe) {
    if (Objects.isNull(recipe.getName())
        || Objects.isNull(recipe.getDescription())
        || Objects.isNull(recipe.getIngredients())
        || Objects.isNull(recipe.getDirections())
        || Objects.isNull(recipe.getCategory())) {
      return false;
    }
    // If name or description are empty strings, return false.
    if (recipe.getName().isBlank()
        || recipe.getDescription().isBlank()
        || recipe.getCategory().isBlank()) {
      return false;
    }
    // If size of ingredients or directions is 0, return false.
      return !recipe.getIngredients().isEmpty() && !recipe.getDirections().isEmpty();
  }
}
