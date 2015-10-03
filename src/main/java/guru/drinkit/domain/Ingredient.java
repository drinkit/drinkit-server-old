package guru.drinkit.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author pkolmykov
 */
@SuppressWarnings("unused")
@Document
public class Ingredient {

    private Integer id;

    @Indexed(unique = true)
    private String name;

    private Integer vol;

    private String description;

    private String category;

    private List<String> alias;

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

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(final List<String> alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
