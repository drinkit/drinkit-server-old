package guru.drinkit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author pkolmykov
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not be deleted due record not found")
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
