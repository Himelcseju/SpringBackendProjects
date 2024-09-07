package com.example.demo.Entity;

/**
 *
 * @author admin
 */
public class ApiResponse {

    private String message;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApiResponse{" + "message=" + message + ", status=" + status + '}';
    }

}
