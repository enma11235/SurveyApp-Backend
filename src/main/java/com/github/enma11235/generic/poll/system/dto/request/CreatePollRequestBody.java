package com.github.enma11235.generic.poll.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.*;

public class CreatePollRequestBody {

    @NotBlank(message = "Title is required")
    private String title;

    @NotEmpty(message = "Options are required")
    @NotNull
    private List<String> options;

    @NotBlank(message = "Token is required")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title;}

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
