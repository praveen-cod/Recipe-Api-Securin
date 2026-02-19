package com.recipe.recipe_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuisine;
    private String title;
    private Double rating;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "cook_time")
    private Integer cookTime;

    @Column(name = "total_time")
    private Integer totalTime;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "JSON")
    private String nutrients;

    private String serves;
    private Integer calories;

    // getters and setters
    public Long getId() { return id; }

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

    public String getNutrients() { return nutrients; }
    public void setNutrients(String nutrients) { this.nutrients = nutrients; }

    public String getServes() { return serves; }
    public void setServes(String serves) { this.serves = serves; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
}
