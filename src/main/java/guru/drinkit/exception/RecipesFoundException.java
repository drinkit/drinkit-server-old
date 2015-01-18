package guru.drinkit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author pkolmykov
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Found recipes with this ingredient")
public class RecipesFoundException extends RuntimeException {

    public RecipesFoundException(String message) {
        super(message);
    }

    public RecipesFoundException(int size) {
        super(String.format("Ingredient could not be deleted. Used in %s recipes", size));
    }
}
