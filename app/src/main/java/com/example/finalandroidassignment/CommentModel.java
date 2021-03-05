package com.example.finalandroidassignment;

public class CommentModel {
    private String name,comment,date;

    public CommentModel() {
    }

    public CommentModel(String name, String comment, String date) {
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
