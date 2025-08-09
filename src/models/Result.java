package models;

public class Result {
    private final Boolean IsSuccessful;
    private final String message;

    public Result(Boolean IsSuccessful, String message) {
        this.IsSuccessful = IsSuccessful;
        this.message = message;
    }

    public Boolean getIsSuccessful() {
        return IsSuccessful;
    }
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
