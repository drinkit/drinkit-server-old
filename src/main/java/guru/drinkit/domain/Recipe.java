package guru.drinkit.domain;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("UnusedDeclaration")
public class Recipe {
    @NotNull
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
    private Integer views;


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Recipe recipe = (Recipe) o;

        return new EqualsBuilder()
                .append(cocktailTypeId, recipe.cocktailTypeId)
                .append(published, recipe.published)
                .append(id, recipe.id)
                .append(description, recipe.description)
                .append(name, recipe.name)
                .append(options, recipe.options)
                .append(ingredientsWithQuantities, recipe.ingredientsWithQuantities)
                .append(imageUrl, recipe.imageUrl)
                .append(thumbnailUrl, recipe.thumbnailUrl)
                .append(createdDate, recipe.createdDate)
                .append(addedBy, recipe.addedBy)
                .append(likes, recipe.likes)
                .append(views, recipe.views)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(cocktailTypeId)
                .append(description)
                .append(name)
                .append(options)
                .append(ingredientsWithQuantities)
                .append(imageUrl)
                .append(thumbnailUrl)
                .append(createdDate)
                .append(addedBy)
                .append(published)
                .append(likes)
                .append(views)
                .toHashCode();
    }

    public Integer getViews() {

        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
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
        public boolean equals(final Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            final IngredientWithQuantity that = (IngredientWithQuantity) o;

            return new EqualsBuilder()
                    .append(ingredientId, that.ingredientId)
                    .append(quantity, that.quantity)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(ingredientId)
                    .append(quantity)
                    .toHashCode();
        }
    }
}
