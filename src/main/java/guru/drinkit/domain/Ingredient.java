package guru.drinkit.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author pkolmykov
 */
public class Ingredient {
    private Integer id;
    private String name;
    private Integer vol;
    private String description;
    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVol() {
        return vol;
    }

    public void setVol(Integer vol) {
        this.vol = vol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
