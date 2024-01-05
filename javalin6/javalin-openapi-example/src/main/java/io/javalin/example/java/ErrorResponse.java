package io.javalin.example.java;

import java.util.Map;

public class ErrorResponse {
    private String title;
    private int status;
    private String type;
    private Map<String, String> details;

    public ErrorResponse() {
    }

    public ErrorResponse(String title, int status, String type, Map<String, String> details) {
        this.title = title;
        this.status = status;
        this.type = type;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}


