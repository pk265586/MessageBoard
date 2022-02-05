package utils;

import lombok.Getter;

/**
 * Class encapsulating operation result (either success or error) and an error message in case of error
 */
public class OperationResult {
    @Getter
    private String errorMessage;

    public boolean isSuccess() {
        return errorMessage == null || errorMessage.length() == 0;
    }

    public boolean isError() {
        return !isSuccess();
    }

    private OperationResult() {
    }

    private OperationResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Static factory method returning class instance denoting successful operation.
     * @return OperationResult instance denoting "success".
     */
    public static OperationResult Success() {
        return new OperationResult();
    }

    /**
     * Static factory method returning class instance denoting failure in operation.
     * @param errorMessage Error message to set in the instance.
     * @return OperationResult instance denoting "failure".
     */
    public static OperationResult Failure(String errorMessage) {
        return new OperationResult(errorMessage);
    }
}
