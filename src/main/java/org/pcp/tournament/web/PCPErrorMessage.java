package org.pcp.tournament.web;

public class PCPErrorMessage {

    private int errorCode;

    private String message;

    public PCPErrorMessage(int errorCode, String errorMessage) {
        setErrorCode(errorCode);
        setMessage(errorMessage);
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }
    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}