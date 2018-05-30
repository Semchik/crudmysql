package com.den.crudsql.model.impl;

import com.den.crudsql.model.MessageType;

public class RealMessageType implements MessageType {
    private Long id;
    private String name;
    private String template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", template='" + template + '\'' +
                '}';
    }
}
