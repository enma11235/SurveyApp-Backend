package com.quickpolls.core.dto.response;

import com.quickpolls.core.dto.model.PollData;

public class AddVoteResponseBody {

    private PollData pollData;

    public AddVoteResponseBody(PollData pollData) {
        this.pollData = pollData;
    }

    public PollData getPollData() {
        return pollData;
    }

    public void setPollData(PollData pollData) {
        this.pollData = pollData;
    }
}
