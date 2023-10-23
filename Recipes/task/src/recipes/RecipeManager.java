package recipes;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecipeManager {

  private final RecipeRepository recipeRepository;

  public RecipeManager(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  // update the recipe
  public long newRecipe(Long author, Recipe recipe) {
    recipe.setDate(LocalDateTime.now());
    recipe.setAuthorId(author);
    Recipe recipeSaved = recipeRepository.save(recipe);
    return recipeSaved.getId();
  }

  // get the recipe
  public Recipe getRecipe(long id) {
    return recipeRepository.findById(id).orElse(null);
  }

  // delete the recipe
  public boolean deleteRecipe(long id) {
    if (recipeRepository.existsById(id)) {
      recipeRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  public boolean updateRecipe(long authorId, long id, Recipe recipe) {
    // First get the recipe from the database
    if (recipeRepository.existsById(id)) {
      Long oldRecipeRepositoryAuthorId = recipeRepository.findById(id).get().getAuthorId();
      if (!oldRecipeRepositoryAuthorId.equals(authorId)) {
        throw new RuntimeException("You are not the author of this recipe");
      }
      // Then update the recipe from the database with the new recipe
      recipe.setId(id);
      recipe.setDate(LocalDateTime.now());
      recipe.setAuthorId(oldRecipeRepositoryAuthorId);
      recipeRepository.save(recipe);
      return true;
    }
    return false;
  }

  public List<Recipe> searchByCategory(String category) {
    return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
  }

  public List<Recipe> searchByName(String name) {
    return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
  }
}
