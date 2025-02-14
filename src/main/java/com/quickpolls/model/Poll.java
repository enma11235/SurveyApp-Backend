package com.quickpolls.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String created_at;

    @OneToMany(mappedBy = "poll", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<Option> options;

    @Column(nullable = false)
    private int total_votes;

    public int getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(int total_votes) {
        this.total_votes = total_votes;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

