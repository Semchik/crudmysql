package com.den.crudsql.model;

import java.time.LocalDate;

public interface Message {
    public Long getMessageId();

    public void setMessageId(Long messageId);

    public Long getMessageTypeId();

    public void setMessageTypeId(Long messageTypeId);

    public Long getAuthor();

    public void setAuthor(Long author);

    public Long getUserId();

    public void setUserId(Long userId);

    public Long getProjectId();

    public void setProjectId(Long projectId);

    public Long getStageId();

    public void setStageId(Long stageId);

    public Long getTaskId();

    public void setTaskId(Long taskId);

    public Long getArtefactId() ;

    public void setArtefactId(Long artefactId);

    public LocalDate getDate();

    public void setDate(LocalDate date);
}
