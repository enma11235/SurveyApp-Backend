package com.github.enma11235.generic.poll.system.controller;

import com.github.enma11235.generic.poll.system.dto.request.*;
import com.github.enma11235.generic.poll.system.dto.model.*;
import com.github.enma11235.generic.poll.system.dto.response.*;
import com.github.enma11235.generic.poll.system.model.*;
import com.github.enma11235.generic.poll.system.service.*;

import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/poll")
public class PollController {

    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    // CREATE POLL
    @PostMapping("/create")
    public ResponseEntity<Void> createPoll(@RequestBody @Valid CreatePollRequestBody body) {
        pollService.createPoll(body.getTitle(), body.getOptions(), body.getToken());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // GET SURVEY
    @GetMapping("/{id}")
    public ResponseEntity<GetSurveyResponseBody> getSurveyById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        //obtenemos el token
        String token = authorizationHeader.substring(7);
        PollDTO pollDTO = pollService.getPollById(id, token);
        GetSurveyResponseBody responseBody = new GetSurveyResponseBody(pollDTO.getId(), pollDTO.getTitle(), pollDTO.getCreator(), pollDTO.getOptions(), pollDTO.getCreated_at());
        return ResponseEntity.ok(responseBody);
    }

    //GET ALL SURVEYS
    @GetMapping
    public ResponseEntity<List<GetSurveysResponseBody>> getSurveys() {
        List<GetSurveysResponseBody> responseBody = pollService.getAllSurveys();
        return ResponseEntity.ok(responseBody);
    }

    //VOTE
    @PostMapping("/{survey_id}/{option_id}")
    public ResponseEntity<VoteResponseBody> vote(@PathVariable Long survey_id, @PathVariable Long option_id, @RequestBody @Valid VoteRequestBody body) {
        Poll poll = pollService.vote(option_id, body.getToken());
        HashMap<String, Object> creator = new HashMap<String, Object>();
        creator.put("id", poll.getUser().getId());
        creator.put("nickname", poll.getUser().getUsername());
        creator.put("image", poll.getUser().getImg());

        List<HashMap<String, Object>> optionsList = new ArrayList<HashMap<String, Object>>();
        for(Option op : poll.getOptions()) {
            HashMap<String, Object> optionHashMap = new HashMap<String, Object>();
            optionHashMap.put("id", op.getId());
            optionHashMap.put("name", op.getName());
            optionHashMap.put("votes", op.getVotes().size());
            optionsList.add(optionHashMap);
        }

        VoteResponseBody responseBody = new VoteResponseBody(poll.getId(), poll.getTitle(), creator, optionsList, poll.getTotal_votes(), poll.getCreated_at());
        return ResponseEntity.ok(responseBody);
    }
}