package com.quickpolls.core.controller;

import com.quickpolls.core.dto.model.OptionData;
import com.quickpolls.core.dto.model.PollData;
import com.quickpolls.core.dto.model.UserData;
import com.quickpolls.core.dto.request.AddVoteRequestBody;
import com.quickpolls.core.dto.request.CreatePollRequestBody;
import com.quickpolls.core.dto.response.GetAllPollsResponseBody;
import com.quickpolls.core.dto.response.AddVoteResponseBody;
import com.quickpolls.core.dto.response.ResponseBody;
import com.quickpolls.core.model.Option;
import com.quickpolls.core.model.Poll;
import com.quickpolls.core.service.PollService;
import com.quickpolls.core.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/poll")
public class PollController {

    private final PollService pollService;


    @Autowired
    public PollController(PollService pollService, AuthController authController, UserService userService) {
        this.pollService = pollService;
    }

    // CREATE POLL
    @PostMapping
    public ResponseEntity<ResponseBody> createSurvey(@RequestBody @Valid CreatePollRequestBody body, @RequestHeader("token") String token) {
        pollService.createPoll(body.getTitle(), body.getOptions(), token);
        ResponseBody responseBody = new ResponseBody("Poll created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    //GET ALL POLLS
    @GetMapping
    public ResponseEntity<GetAllPollsResponseBody> getAllPolls() {
        List<PollData> polls = pollService.getAllPolls();
        GetAllPollsResponseBody responseBody = new GetAllPollsResponseBody(polls);
        return ResponseEntity.ok(responseBody);
    }

    //ADD VOTE
    @PostMapping("/vote")
    public ResponseEntity<AddVoteResponseBody> addVote(@RequestBody AddVoteRequestBody body, @RequestHeader("token") String token) {
        Poll poll = pollService.vote(body.getOption_id(), token);

        UserData userData = new UserData(poll.getUser().getId(), poll.getUser().getNickname());
        List<OptionData> optionsData = new ArrayList<>();
        for(Option o : poll.getOptions()) {
            optionsData.add(new OptionData(o.getId(), o.getName(), o.getVotes().size()));
        }
        PollData pollData = new PollData(poll.getId(), poll.getTitle(), userData, optionsData, poll.getCreated_at(), poll.getTotal_votes());

        AddVoteResponseBody responseBody = new AddVoteResponseBody(pollData);
        return ResponseEntity.ok(responseBody);
    }
}