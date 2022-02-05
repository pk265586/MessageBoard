package utils;

import lombok.Getter;

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

    public static OperationResult Success(){
        return new OperationResult();
    }

    public static OperationResult Failure(String errorMessage){
        return new OperationResult(errorMessage);
    }
}
