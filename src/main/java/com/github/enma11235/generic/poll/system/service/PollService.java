package com.github.enma11235.generic.poll.system.service;

import com.github.enma11235.generic.poll.system.dto.model.SurveyCreator;
import com.github.enma11235.generic.poll.system.dto.model.PollDTO;
import com.github.enma11235.generic.poll.system.dto.model.SurveyOption;
import com.github.enma11235.generic.poll.system.dto.response.GetSurveysResponseBody;
import com.github.enma11235.generic.poll.system.exception.AuthException;
import com.github.enma11235.generic.poll.system.exception.SurveyNotFoundException;
import com.github.enma11235.generic.poll.system.exception.UserNotFoundException;
import com.github.enma11235.generic.poll.system.model.Option;
import com.github.enma11235.generic.poll.system.model.Poll;
import com.github.enma11235.generic.poll.system.model.User;
import com.github.enma11235.generic.poll.system.model.Vote;
import com.github.enma11235.generic.poll.system.repository.OptionRepository;
import com.github.enma11235.generic.poll.system.repository.PollRepository;
import com.github.enma11235.generic.poll.system.repository.UserRepository;
import com.github.enma11235.generic.poll.system.repository.VoteRepository;
import com.github.enma11235.generic.poll.system.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import com.github.enma11235.generic.poll.system.utils.DateDifferenceCalculator;

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
        jwtTokenProvider.validateToken(token);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Optional<User> user = userRepository.findByNickname(username);
        if(user.isPresent()) {
            Poll newPoll = new Poll();
            newPoll.setTitle(title);
            newPoll.setUser(user.get());
            List<Option> newOptions = new ArrayList<>();
            for(String name : options) {
                Option opt = new Option();
                opt.setPoll(newPoll);
                opt.setName(name);
                newOptions.add(opt);
            }
            newPoll.setOptions(newOptions);
            newPoll.setTotal_votes(0);
            pollRepository.save(newPoll);
        } else {
            throw new UserNotFoundException("There is no user with username: " + username);
        }
    }

    //GET ALL SURVEYS
    public List<GetSurveysResponseBody> getAllSurveys() {
        List<Poll> polls = pollRepository.findAll();

        List<GetSurveysResponseBody> returnList = new ArrayList<GetSurveysResponseBody>();

        for(Poll s : polls) {
            //debemos sacar los usuarios y las opciones de cada survey
            HashMap<String, Object> creator = new HashMap<String, Object>();
            creator.put("id", s.getUser().getId());
            creator.put("nickname", s.getUser().getUsername());
            creator.put("image", s.getUser().getImg());

            List<HashMap<String, Object>> optionsList = new ArrayList<HashMap<String, Object>>();
            for(Option op : s.getOptions()) {
                HashMap<String, Object> optionHashMap = new HashMap<String, Object>();
                optionHashMap.put("id", op.getId());
                optionHashMap.put("name", op.getName());
                optionHashMap.put("votes", op.getVotes().size());
                optionsList.add(optionHashMap);
            }
            long date = DateDifferenceCalculator.calcularDiferenciaDias(s.getCreated_at(), LocalDate.now().toString());
            String dateString;
            if(date == 0) {
                dateString = "today";
            } else {
                dateString = date + " days ago";
            }
            returnList.add(new GetSurveysResponseBody(s.getId(), s.getTitle(), creator, optionsList, s.getTotal_votes(), dateString));

        }
        return returnList;
    }

    //ADD VOTE
    public Poll vote(long option_id, String token) {
        jwtTokenProvider.validateToken(token);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Optional<User> user = userRepository.findByNickname(username);
        if(user.isPresent()) {
            Optional<Option> option = optionRepository.findById(option_id);
            if(option.isPresent()) {
                Poll poll = option.get().getPoll();
                //se verifica que el usuario no ha votado antes en la encuesta
                boolean userHasAlreadyVoted = false;
                Option optWithVoteToRemove = null;
                Vote voteToRemove = null;
                for(Option opt : poll.getOptions()) {
                    List<Vote> opt_votes = opt.getVotes();
                    for(Vote vote : opt_votes) {
                        if(vote.getUser().getUsername().equals(username)) {
                            userHasAlreadyVoted = true;
                            optWithVoteToRemove = opt;
                            voteToRemove = vote;
                        }
                    }
                }
                if(userHasAlreadyVoted) {
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
            throw new UserNotFoundException("There is no user with username: " + username);
        }
    }
}
