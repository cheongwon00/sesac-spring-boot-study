package com.example.sesac_spring_boot_study.domain;

import java.util.Objects;

public class StudyMember {
    private Long id;
    private String name;
    private State state;

    @Override
    public String toString() {
        return "StudyMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public StudyMember(Long id, String name, State state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudyMember that = (StudyMember) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, state);
    }
}
