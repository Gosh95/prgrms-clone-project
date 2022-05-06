package prgrms.project.starbooks.util.exception;

public enum ErrorMessage {

    NOTHING_INSERTED("Nothing was inserted."),

    NOTHING_UPDATED("Nothing was updated."),

    CANT_FIND_CUSTOMER("Can't find customer."),

    CANT_FIND_PRODUCT("Can't find product."),

    CANT_FIND_ORDER("Can't find order.");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }
}
