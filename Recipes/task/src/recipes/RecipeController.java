package recipes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

  private final RecipeManager recipeManager;
  UserService userService;

  public RecipeController(RecipeManager recipeManager, UserService userService) {
    this.recipeManager = recipeManager;
    this.userService = userService;
  }

  // A mapper for POST /api/recipe receives a recipe as a JSON object and overrides the current
  // recipe.
  @PostMapping("/new")
  public ResponseEntity<Map<String, Long>> addRecipe(@AuthenticationPrincipal User authentication, @RequestBody Recipe recipe) {
    // All fields of Recipe should be present in the JSON object.
    if (!RecipeValidator.validateRecipe(recipe)) {
      return ResponseEntity.badRequest().build();
    }
    User user = userService.findByEmail(authentication.getEmail());
    long id = recipeManager.newRecipe(user.getId(), recipe);
    // Send back a JSON object with an id field containing the id of the new recipe.
    Map<String, Long> map = Map.of("id", id);
    return ResponseEntity.ok(map);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Long>> updateRecipe(
      @AuthenticationPrincipal User authentication,
      @PathVariable long id,
      @RequestBody Recipe recipe) {

    try {
      User user = userService.findByEmail(authentication.getEmail());
      // Check if id exists in the recipe map
      if (Objects.isNull(recipe)) {
        return ResponseEntity.notFound().build();
      }
      // All fields of Recipe should be present in the JSON object.
      if (!RecipeValidator.validateRecipe(recipe)) {
        return ResponseEntity.badRequest().build();
      }
      if (!recipeManager.updateRecipe(user.getId(), id, recipe)) {
        return ResponseEntity.notFound().build();
      }
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // A mapper for GET /api/recipe returns the current recipe as a JSON object.
  @GetMapping("/{id}")
  public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
    // Check if id exists in the recipe map
    Recipe recipe = recipeManager.getRecipe(id);
    if (Objects.isNull(recipe)) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(recipe);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Recipe> deleteRecipe(
      @AuthenticationPrincipal User authentication, @PathVariable long id) {
    // Get the Recipe by ID
    Recipe recipe = recipeManager.getRecipe(id);
    if(Objects.isNull(recipe))
        return ResponseEntity.notFound().build();
    User user = userService.findByEmail(authentication.getEmail());
    if (recipe.getAuthorId().equals(user.getId())) {
      // Check if id exists in the recipe map
      if (!recipeManager.deleteRecipe(id)) {
        return ResponseEntity.notFound().build();
      }
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<Recipe>> searchRecipe(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String name) {
    if (Objects.nonNull(category) && Objects.isNull(name)) {
      return ResponseEntity.ok(recipeManager.searchByCategory(category));
    }
    if (Objects.isNull(category) && Objects.nonNull(name)) {
      return ResponseEntity.ok(recipeManager.searchByName(name));
    }
    return ResponseEntity.badRequest().build();
  }
}
