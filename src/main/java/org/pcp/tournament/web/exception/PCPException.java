package org.pcp.tournament.web.exception;

public class PCPException extends Exception {


    private PCPError error;

    /**
     * @return the error
     */
    public PCPError getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(PCPError error) {
        this.error = error;
    }

    public PCPException(PCPError error, String message) {
        super(message);
        setError(error);
    }

}