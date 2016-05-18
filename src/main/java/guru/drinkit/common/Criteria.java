package guru.drinkit.common;

import java.util.Set;

@SuppressWarnings("unused")
public class Criteria {

    private Set<Integer> cocktailTypes;
    private Set<Integer> ingredients;
    private Set<Integer> options;

    public Criteria() {
    }

    public Criteria(Set<Integer> cocktailTypes, Set<Integer> ingredients, Set<Integer> options) {
        this.cocktailTypes = cocktailTypes;
        this.ingredients = ingredients;
        this.options = options;
    }

    public Set<Integer> getCocktailTypes() {
        return cocktailTypes;
    }

    public void setCocktailTypes(Set<Integer> cocktailTypes) {
        this.cocktailTypes = cocktailTypes;
    }

    public Set<Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<Integer> getOptions() {
        return options;
    }

    public void setOptions(Set<Integer> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "cocktailTypes=" + cocktailTypes +
                ", ingredients=" + ingredients +
                ", options=" + options +
                '}';
    }
}
