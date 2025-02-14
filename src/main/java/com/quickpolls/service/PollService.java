package com.quickpolls.service;

import com.quickpolls.dto.model.OptionData;
import com.quickpolls.dto.model.PollData;
import com.quickpolls.dto.model.UserData;
import com.quickpolls.exception.AuthException;
import com.quickpolls.exception.SurveyNotFoundException;
import com.quickpolls.exception.UserNotFoundException;
import com.quickpolls.model.Option;
import com.quickpolls.model.Poll;
import com.quickpolls.model.User;
import com.quickpolls.model.Vote;
import com.quickpolls.repository.OptionRepository;
import com.quickpolls.repository.PollRepository;
import com.quickpolls.repository.UserRepository;
import com.quickpolls.repository.VoteRepository;
import com.quickpolls.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public PollService(PollRepository pollRepository, OptionRepository optionRepository, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, VoteRepository voteRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
    }

    //CREATE POLL
    public void createPoll(String title, List<String> options, String token) {
        boolean validToken = jwtTokenProvider.validateToken(token);
        if(validToken) {
            String nickname = jwtTokenProvider.getUsernameFromToken(token);
            Optional<User> user = userRepository.findByNickname(nickname);
            if(user.isPresent()) {
                LocalDate date = LocalDate.now();
                String dateString = date.toString();

                Poll poll = new Poll();
                poll.setTitle(title);
                poll.setUser(user.get());
                List<Option> pollOptions = new ArrayList<Option>();
                for(String name : options) {
                    Option op = new Option();
                    op.setSurvey(poll);
                    op.setName(name);
                    pollOptions.add(op);
                }
                poll.setOptions(pollOptions);
                poll.setCreated_at(dateString);
                poll.setTotal_votes(0);
                Poll savedPoll = pollRepository.save(poll);
            } else {
                throw new UserNotFoundException("It seems like your user was deleted");
            }
        } else {
            throw new AuthException("Invalid token, are you logged in?");
        }
    }

    //GET ALL POLLS
    public List<PollData> getAllPolls() {
        List<Poll> polls = pollRepository.findAll();

        List<PollData> returnList = new ArrayList<PollData>();

        for(Poll p : polls) {
            UserData userData = new UserData(p.getUser().getId(), p.getUser().getNickname(), p.getUser().getImg());
            List<Option> options = p.getOptions();
            List<OptionData> optionsData = new ArrayList<OptionData>();
            for(Option o : options) {
                optionsData.add(new OptionData(o.getId(), o.getName(), o.getVotes().size()));
            }
            PollData pollData = new PollData(p.getId(), p.getTitle(), userData, optionsData, p.getCreated_at(), p.getTotal_votes());
            returnList.add(pollData);
        }
        return returnList;
    }

    //ADD VOTE
    public Poll vote(long option_id, String token) {
        boolean validToken = jwtTokenProvider.validateToken(token);
        if(validToken) {
            String nickname = jwtTokenProvider.getUsernameFromToken(token);
            Optional<User> user = userRepository.findByNickname(nickname);
            Optional<Option> option = optionRepository.findById(option_id);
            if(option.isPresent() && user.isPresent()) {
                Poll poll = option.get().getSurvey();
                //se verifica que el usuario no ha votado antes alguna opcion de la misma encuesta
                boolean userAlreadyVoteAnOption = false;
                List<Option> survey_options = poll.getOptions();
                Option optWithVoteToRemove = null;
                Vote voteToRemove = null;
                for(Option opt : survey_options) {
                    List<Vote> opt_votes = opt.getVotes();
                    for(Vote vote : opt_votes) {
                        if(vote.getUser().getNickname().equals(nickname)) {
                            userAlreadyVoteAnOption = true;
                            optWithVoteToRemove = opt;
                            voteToRemove = vote;
                        }
                    }
                }
                if(userAlreadyVoteAnOption) {
                    optWithVoteToRemove.getVotes().remove(voteToRemove); //intuitivamente, si elimino el voto de la lista de votos de la opcion, spring deberia eliminar el voto automaticamente
                    optionRepository.save(optWithVoteToRemove);
                    //como spring ha eliminado el voto, necesito crear uno nuevo
                    Vote newVote = new Vote(user.get(), option.get());
                    option.get().getVotes().add(newVote);
                    voteRepository.save(newVote);
                    optionRepository.save(option.get());
                    return pollRepository.save(poll);
                } else {
                    //como spring ha eliminado el voto, necesito crear uno nuevo
                    Vote newVote = new Vote(user.get(), option.get());
                    option.get().getVotes().add(newVote);
                    poll.setTotal_votes(poll.getTotal_votes() + 1);
                    voteRepository.save(newVote);
                    optionRepository.save(option.get());
                    return pollRepository.save(poll);
                }
            } else {
                throw new SurveyNotFoundException("The survey does not exist");
            }
        } else {
            throw new AuthException("Invalid token");
        }
    }
}
