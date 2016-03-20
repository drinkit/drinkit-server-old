package guru.drinkit.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class Recipe {

    private Integer id;
    private int cocktailTypeId;
    private String description;
    private String name;
    private int[] options;
    private List<IngredientWithQuantity> ingredientsWithQuantities;
    private String imageUrl;
    private String thumbnailUrl;
    private Date createdDate = new Date();
    private String addedBy;
    private boolean published;
    private Integer likes;


    //-------------------------------------------------------------------


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public int getCocktailTypeId() {
        return cocktailTypeId;
    }

    public void setCocktailTypeId(int cocktailTypeId) {
        this.cocktailTypeId = cocktailTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getOptions() {
        return options;
    }

    public void setOptions(int[] options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (cocktailTypeId != recipe.cocktailTypeId) return false;
        if (published != recipe.published) return false;
        if (!id.equals(recipe.id)) return false;
        if (description != null ? !description.equals(recipe.description) : recipe.description != null) return false;
        if (!name.equals(recipe.name)) return false;
        if (!Arrays.equals(options, recipe.options)) return false;
        if (ingredientsWithQuantities != null ? !ingredientsWithQuantities.equals(recipe.ingredientsWithQuantities) : recipe.ingredientsWithQuantities != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(recipe.imageUrl) : recipe.imageUrl != null) return false;
        if (thumbnailUrl != null ? !thumbnailUrl.equals(recipe.thumbnailUrl) : recipe.thumbnailUrl != null)
            return false;
        if (createdDate != null ? !createdDate.equals(recipe.createdDate) : recipe.createdDate != null) return false;
        if (addedBy != null ? !addedBy.equals(recipe.addedBy) : recipe.addedBy != null) return false;
        return likes != null ? likes.equals(recipe.likes) : recipe.likes == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + cocktailTypeId;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + Arrays.hashCode(options);
        result = 31 * result + (ingredientsWithQuantities != null ? ingredientsWithQuantities.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (addedBy != null ? addedBy.hashCode() : 0);
        result = 31 * result + (published ? 1 : 0);
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        return result;
    }

    public List<IngredientWithQuantity> getIngredientsWithQuantities() {
        return ingredientsWithQuantities;
    }

    public void setIngredientsWithQuantities(final List<IngredientWithQuantity> ingredientsWithQuantities) {
        this.ingredientsWithQuantities = ingredientsWithQuantities;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(final boolean published) {
        this.published = published;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public static class IngredientWithQuantity {
        private Integer ingredientId;
        private Integer quantity;

        public IngredientWithQuantity() {
        }

        public IngredientWithQuantity(final Integer ingredientId, final Integer quantity) {
            this.ingredientId = ingredientId;
            this.quantity = quantity;
        }

        public Integer getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(final Integer ingredientId) {
            this.ingredientId = ingredientId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(final Integer quantity) {
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IngredientWithQuantity that = (IngredientWithQuantity) o;

            if (!ingredientId.equals(that.ingredientId)) return false;
            return quantity.equals(that.quantity);

        }

        @Override
        public int hashCode() {
            int result = ingredientId.hashCode();
            result = 31 * result + quantity.hashCode();
            return result;
        }
    }
}
