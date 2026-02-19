package com.recipe.recipe_api.dto;

import java.util.Map;

public class RecipeResponse {

    private Long id;
    private String cuisine;
    private String title;
    private Double rating;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private String description;
    private Map<String, Object> nutrients;
    private String serves;
    private Integer calories;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }

    public Integer getCookTime() { return cookTime; }
    public void setCookTime(Integer cookTime) { this.cookTime = cookTime; }

    public Integer getTotalTime() { return totalTime; }
    public void setTotalTime(Integer totalTime) { this.totalTime = totalTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> getNutrients() { return nutrients; }
    public void setNutrients(Map<String, Object> nutrients) { this.nutrients = nutrients; }

    public String getServes() { return serves; }
    public void setServes(String serves) { this.serves = serves; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
}
