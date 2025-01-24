package com.github.enma11235.generic.poll.system.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @OneToMany(mappedBy = "option", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vote> votes;

    public Poll getPoll() {
        return poll;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
