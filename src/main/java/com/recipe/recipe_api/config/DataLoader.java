package com.recipe.recipe_api.config;

import com.recipe.recipe_api.repository.RecipeRepository;
import com.recipe.recipe_api.service.RecipeImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeImportService importService;
    private final RecipeRepository recipeRepository;

    public DataLoader(RecipeImportService importService, RecipeRepository recipeRepository) {
        this.importService = importService;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void run(String... args) {
        // delete old data
        recipeRepository.deleteAll();

        // import fresh data
        importService.importRecipes();
    }

}
