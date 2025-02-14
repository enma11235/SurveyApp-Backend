package com.quickpolls.dto.response;

import java.util.List;
import com.quickpolls.dto.model.PollData;

public class GetAllPollsResponseBody {
    private List<PollData> polls;

    public GetAllPollsResponseBody(List<PollData> polls) {
        this.polls = polls;
    }

    public List<PollData> getPolls() {
        return polls;
    }

    public void setPolls(List<PollData> polls) {
        this.polls = polls;
    }
}
