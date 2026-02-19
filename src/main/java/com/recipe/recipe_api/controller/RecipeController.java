package com.recipe.recipe_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.recipe_api.dto.RecipeResponse;
import com.recipe.recipe_api.entity.Recipe;
import com.recipe.recipe_api.repository.RecipeRepository;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    // ✅ API 1: GET all recipes (pagination + sort rating desc)
    @GetMapping
    public Map<String, Object> getAllRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        int pageIndex = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(
                pageIndex,
                limit,
                Sort.by(Sort.Direction.DESC, "rating")
        );

        Page<Recipe> recipePage = recipeRepository.findAll(pageable);

        List<RecipeResponse> data = recipePage.getContent()
                .stream()
                .map(this::toDto)
                .toList();

        Map<String, Object> res = new HashMap<>();
        res.put("page", page);
        res.put("limit", limit);
        res.put("total", recipePage.getTotalElements());
        res.put("data", data);
        return res;
    }

    // ✅ API 2: SEARCH recipes (filters + pagination + sort rating desc)
    @GetMapping("/search")
    public Map<String, Object> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(required = false) Double ratingMax,
            @RequestParam(required = false) Integer totalTimeMin,
            @RequestParam(required = false) Integer totalTimeMax,
            @RequestParam(required = false) Integer caloriesMin,
            @RequestParam(required = false) Integer caloriesMax,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        int pageIndex = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(
                pageIndex,
                limit,
                Sort.by(Sort.Direction.DESC, "rating")
        );

        Page<Recipe> result = recipeRepository.findAll((root, query, cb) -> {
            var predicate = cb.conjunction();

            // title contains (case-insensitive)
            if (title != null && !title.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            // cuisine equals (case-insensitive)
            if (cuisine != null && !cuisine.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("cuisine")), cuisine.toLowerCase()));
            }

            // rating filters (NULL safe)
            if (ratingMin != null) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.isNotNull(root.get("rating")),
                                cb.greaterThanOrEqualTo(root.get("rating"), ratingMin)
                        )
                );
            }

            if (ratingMax != null) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.isNotNull(root.get("rating")),
                                cb.lessThanOrEqualTo(root.get("rating"), ratingMax)
                        )
                );
            }

            // totalTime filters (NULL safe)
            if (totalTimeMin != null) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.isNotNull(root.get("totalTime")),
                                cb.greaterThanOrEqualTo(root.get("totalTime"), totalTimeMin)
                        )
                );
            }

            if (totalTimeMax != null) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.isNotNull(root.get("totalTime")),
                                cb.lessThanOrEqualTo(root.get("totalTime"), totalTimeMax)
                        )
                );
            }

            // calories filters (NULL safe)
            if (caloriesMin != null) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.isNotNull(root.get("calories")),
                                cb.greaterThanOrEqualTo(root.get("calories"), caloriesMin)
                        )
                );
            }

            if (caloriesMax != null) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.isNotNull(root.get("calories")),
                                cb.lessThanOrEqualTo(root.get("calories"), caloriesMax)
                        )
                );
            }

            return predicate;
        }, pageable);

        List<RecipeResponse> data = result.getContent()
                .stream()
                .map(this::toDto)
                .toList();

        Map<String, Object> res = new HashMap<>();
        res.put("page", page);
        res.put("limit", limit);
        res.put("total", result.getTotalElements());
        res.put("data", data);
        return res;
    }

    // ✅ Convert Recipe Entity -> RecipeResponse DTO
    private RecipeResponse toDto(Recipe r) {
        RecipeResponse dto = new RecipeResponse();

        dto.setId(r.getId());
        dto.setCuisine(r.getCuisine());
        dto.setTitle(r.getTitle());
        dto.setRating(r.getRating());
        dto.setPrepTime(r.getPrepTime());
        dto.setCookTime(r.getCookTime());
        dto.setTotalTime(r.getTotalTime());
        dto.setDescription(r.getDescription());
        dto.setServes(r.getServes());
        dto.setCalories(r.getCalories());

        // Convert nutrients String -> JSON object
        try {
            if (r.getNutrients() != null && !r.getNutrients().isBlank()) {
                Map<String, Object> map = objectMapper.readValue(r.getNutrients(), Map.class);
                dto.setNutrients(map);
            } else {
                dto.setNutrients(null);
            }
        } catch (Exception e) {
            dto.setNutrients(null);
        }

        return dto;
    }
}
