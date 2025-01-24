package com.github.enma11235.generic.poll.system.dto.model;

import java.util.List;

public class PollDTO {
    private Long id;
    private String title;
    private SurveyCreator creator;
    private List<SurveyOption> options;
    private String created_at;
    private int total_votes;

    public PollDTO(Long id, String title, SurveyCreator creator, List<SurveyOption> options, String created_at, int total_votes) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.options = options;
        this.created_at = created_at;
        this.total_votes = total_votes;
    }

    public int getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(int total_votes) {
        this.total_votes = total_votes;
    }

    public Long getId() {
        return id;
    }

    public List<SurveyOption> getOptions() {
        return options;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setOptions(List<SurveyOption> options) {
        this.options = options;
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

    public SurveyCreator getCreator() {
        return creator;
    }

    public void setCreator(SurveyCreator creator) {
        this.creator = creator;
    }
}
