package api.exception;

import api.dto.errors.ErrorModel;
import lombok.Getter;

public class ModelException extends RuntimeException {

    @Getter
    private ErrorModel model;

    public ModelException(ErrorModel model, Throwable cause) {
        super(model.getMessage(), cause);
        this.model = model;
    }

    public ModelException(ErrorModel model) {
        this(model, null);
    }

}
