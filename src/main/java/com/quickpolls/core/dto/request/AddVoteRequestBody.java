package com.quickpolls.core.dto.request;

public class AddVoteRequestBody {
    private Long option_id;

    public AddVoteRequestBody(Long option_id) {
        this.option_id = option_id;
    }

    public Long getOption_id() {
        return option_id;
    }

    public void setOption_id(Long option_id) {
        this.option_id = option_id;
    }
}
