package com.quickpolls.core.dto.model;

public class OptionData {
    private Long id;
    private String name;
    private int votes;

    public OptionData(Long id, String name, int votes) {
        this.id = id;
        this.name = name;
        this.votes = votes;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
