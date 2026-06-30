package com.example.model;

import com.fasterxml.jackson.annotation.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Task {
    private long id;
    private String title;
    private String description;
    private Status status;
    //@JsonBackReference
    private User assignedUser;

    public Task(){}
    public Task(long id,String title,String description,Status status, User assignedUser){
        this.id=id;
        this.title=title;
        this.description=description;
        this.status=status;
        this.assignedUser=assignedUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }
}
