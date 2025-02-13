package com.quickpolls.core.dto.model;

import java.util.List;

public class PollData {
    private Long id;
    private String title;
    private UserData user;
    private List<OptionData> options;
    private String created_at;
    private int total_votes;

    public PollData(Long id, String title, UserData user, List<OptionData> options, String created_at, int total_votes) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.options = options;
        this.created_at = created_at;
        this.total_votes = total_votes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public List<OptionData> getOptions() {
        return options;
    }

    public void setOptions(List<OptionData> options) {
        this.options = options;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(int total_votes) {
        this.total_votes = total_votes;
    }
}
