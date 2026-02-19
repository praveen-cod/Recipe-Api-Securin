package com.recipe.recipe_api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.recipe_api.entity.Recipe;
import com.recipe.recipe_api.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class RecipeImportService {

    @Autowired
    private RecipeRepository recipeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void importRecipes() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/data/recipes.json");
            if (inputStream == null) {
                throw new RuntimeException("recipes.json not found at /src/main/resources/data/recipes.json");
            }

            // Read root as JsonNode to detect ARRAY vs OBJECT
            var root = objectMapper.readTree(inputStream);

            List<Map<String, Object>> items = new ArrayList<>();

            if (root.isArray()) {
                // ✅ JSON is like: [ {...}, {...} ]
                items = objectMapper.convertValue(root, new TypeReference<>() {});
            } else if (root.isObject()) {
                // ✅ JSON is like: { "0": {...}, "1": {...} }
                Map<String, Map<String, Object>> data =
                        objectMapper.convertValue(root, new TypeReference<>() {});
                items.addAll(data.values());
            } else {
                throw new RuntimeException("Unsupported JSON format. Root must be array or object.");
            }

            List<Recipe> recipeList = new ArrayList<>();

            for (Map<String, Object> r : items) {

                Recipe recipe = new Recipe();

                recipe.setCuisine((String) r.get("cuisine"));
                recipe.setTitle((String) r.get("title"));

                recipe.setRating(safeDouble(r.get("rating")));
                recipe.setPrepTime(safeInt(r.get("prep_time")));
                recipe.setCookTime(safeInt(r.get("cook_time")));
                recipe.setTotalTime(safeInt(r.get("total_time")));

                recipe.setDescription((String) r.get("description"));

                // nutrients -> JSON string (stored in MySQL JSON column)
                Map<String, Object> nutrientsMap = (Map<String, Object>) r.get("nutrients");
                String nutrientsJson = objectMapper.writeValueAsString(nutrientsMap);
                recipe.setNutrients(nutrientsJson);

                recipe.setServes((String) r.get("serves"));

                recipe.setCalories(extractCalories(nutrientsMap));

                recipeList.add(recipe);
            }

            recipeRepository.saveAll(recipeList);
            System.out.println("Recipes imported successfully! Total: " + recipeList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Double safeDouble(Object value) {
        try {
            if (value == null || value.toString().equalsIgnoreCase("NaN")) return null;
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Integer safeInt(Object value) {
        try {
            if (value == null || value.toString().equalsIgnoreCase("NaN")) return null;
            return (int) Double.parseDouble(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Integer extractCalories(Map<String, Object> nutrients) {
        try {
            if (nutrients == null) return null;
            String calStr = (String) nutrients.get("calories");
            if (calStr == null) return null;
            String num = calStr.replaceAll("[^0-9]", "");
            return Integer.parseInt(num);
        } catch (Exception e) {
            return null;
        }
    }
}
