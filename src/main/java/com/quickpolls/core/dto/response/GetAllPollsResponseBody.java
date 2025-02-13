package com.quickpolls.core.dto.response;

import java.util.List;
import com.github.enma11235.generic.poll.system.dto.model.*;
import com.quickpolls.core.dto.model.PollData;

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
