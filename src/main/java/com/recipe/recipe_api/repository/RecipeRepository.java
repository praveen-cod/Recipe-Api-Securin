package com.recipe.recipe_api.repository;

import com.recipe.recipe_api.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeRepository
        extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
}
