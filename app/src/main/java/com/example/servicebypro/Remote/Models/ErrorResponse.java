package com.example.servicebypro.Remote.Models;

public class ErrorResponse {

    private String errorMessage = "Unknown Error";
    private int errorCode;
    private String documentation;

    public ErrorResponse() {

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDocumentation() {
        return documentation;
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
}
