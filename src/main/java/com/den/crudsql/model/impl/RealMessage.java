package com.den.crudsql.model.impl;

import com.den.crudsql.model.Message;


import java.time.LocalDate;

public class RealMessage implements Message {
    private Long messageId;
    private Long messageTypeId;
    private Long author;
    private Long userId;
    private Long projectId;
    private Long stageId;
    private Long taskId;
    private Long artefactId;
    private LocalDate date;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(Long messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(Long artefactId) {
        this.artefactId = artefactId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", messageTypeId=" + messageTypeId +
                ", author=" + author +
                ", userId=" + userId +
                ", projectId=" + projectId +
                ", stageId=" + stageId +
                ", taskId=" + taskId +
                ", artefactId=" + artefactId +
                ", date=" + date +
                '}';
    }
}
