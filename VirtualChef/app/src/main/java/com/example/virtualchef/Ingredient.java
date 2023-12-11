package com.example.virtualchef;

public class Ingredient {
    private String foodName;
    private double calories;
    private double totalFat;
    private double saturatedFat;
    private double cholesterol;
    private double sodium;
    private double totalCarbohydrate;
    private double dietaryFiber;
    private double sugars;
    private double protein;
    private double potassium;
    private double phosphorus;

    // Constructor
    public Ingredient(String foodName,
                        double calories,
                        double totalFat,
                        double saturatedFat,
                        double cholesterol,
                        double sodium,
                        double totalCarbohydrate,
                        double dietaryFiber,
                        double sugars,
                        double protein,
                        double potassium,
                        double phosphorus) {
        this.foodName = foodName;
        this.calories = calories;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.totalCarbohydrate = totalCarbohydrate;
        this.dietaryFiber = dietaryFiber;
        this.sugars = sugars;
        this.protein = protein;
        this.potassium = potassium;
        this.phosphorus = phosphorus;
    }
    public String getAllNutrients() {
        return "Item: " + foodName +
                "\nCalories: " + calories +
                "\nTotal Fat: " + totalFat +
                "\nSaturated Fat: " + saturatedFat +
                "\nCholesterol: " + cholesterol +
                "\nSodium: " + sodium +
                "\nTotal Carbohydrate: " + totalCarbohydrate +
                "\nDietary Fiber: " + dietaryFiber +
                "\nSugars: " + sugars +
                "\nProtein: " + protein +
                "\nPotassium: " + potassium +
                "\nPhosphorus: " + phosphorus;
    }

    // Getter and Setter methods
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(double totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public double getDietaryFiber() {
        return dietaryFiber;
    }

    public void setDietaryFiber(double dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public double getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(double phosphorus) {
        this.phosphorus = phosphorus;
    }
}

