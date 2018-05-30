package com.den.crudsql.shell;

import com.den.crudsql.dao.MessageDao;
import com.den.crudsql.dao.MessageTypeDao;
import com.den.crudsql.dao.UserDao;
import com.den.crudsql.model.Message;
import com.den.crudsql.model.MessageType;
import com.den.crudsql.model.User;
import com.den.crudsql.model.impl.RealMessage;
import com.den.crudsql.model.impl.RealMessageType;
import com.den.crudsql.model.impl.RealUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDate;
import java.util.List;

@ShellComponent
public class ViewShell {

    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private MessageTypeDao messageTypeDao;

    @ShellMethod(value = "Show all users")
    public List<User> showUsers() {
        return userDao.findAll();
    }

    @ShellMethod(value = "Show all messages")
    public List<Message> showMessages() {
        return messageDao.findAll();
    }

    @ShellMethod(value = "Show all messageTypes")
    public List<MessageType> showTypes(){
        return messageTypeDao.findAll();
    }

    @ShellMethod(value = "Show user by id")
    public User showUser(Long id){
        return userDao.findById(id);
    }

    @ShellMethod(value = "Show message by id")
    public Message showMessage(Long id){
        return messageDao.findById(id);
    }

    @ShellMethod(value = "Show message type by id")
    public MessageType showType(Long id){
        return messageTypeDao.findById(id);
    }

    @ShellMethod(value = "Create user")
    public String createUser(String name){
        User user = new RealUser();
        user.setName(name);
        Long id =  userDao.create(user);
        return "User " + id + " created.";
    }

    @ShellMethod(value = "Create message")
    public String createMessage(Long messageTypeId, Long author, Long userId, Long projectId, Long stageId, Long taskId, Long artefactId){
        Message message = new RealMessage();
        message.setMessageTypeId( messageTypeId);
        message.setAuthor(author);
        message.setUserId(userId);
        message.setProjectId(projectId);
        message.setStageId(stageId);
        message.setTaskId(taskId);
        message.setArtefactId(artefactId);
        message.setDate(LocalDate.now());
        Long id = messageDao.create(message);
        return "Message " + " created";
    }

    @ShellMethod(value = "Create message type")
    public String createMessageType(String name, String template){
        MessageType messageType = new RealMessageType();
        messageType.setName(name);
        messageType.setTemplate(template);
        Long id = messageTypeDao.create(messageType);
        return "MessageType " + " created";
    }

    @ShellMethod(value = "Update user")
    public String updateUser(Long id, String name) {
        User user = new RealUser();
        user.setId(id);
        user.setName(name);
        if (userDao.update(user)) {
            return "User " + user.getId() + " updated.";
        }
        return "Something wrong";
    }

    @ShellMethod(value = "Update messages")
    public String updateMessage(Long id, Long messageTypeId, Long author, Long userId, Long projectId, Long stageId, Long taskId, Long artefactId){
        Message message = new RealMessage();
        message.setMessageId(id);
        message.setMessageTypeId( messageTypeId);
        message.setAuthor(author);
        message.setUserId(userId);
        message.setProjectId(projectId);
        message.setStageId(stageId);
        message.setTaskId(taskId);
        message.setArtefactId(artefactId);
        if(messageDao.update(message)){
            return "User " + message.getMessageId() + " updated.";
        }
        return "Something wrong.";
    }

    @ShellMethod(value = "Update message type")
    public String updateMessageType(Long id, String name, String template){
        MessageType messageType = new RealMessageType();
        messageType.setId(id);
        messageType.setName(name);
        messageType.setTemplate(template);
        if(messageTypeDao.update(messageType)) {
            return "MessageType " + messageType.getId() + " created";
        }
        return "Something wrong";
    }

    @ShellMethod(value = "Delete user by id")
    public String deleteUser(Long id){
        if (userDao.delete(id)){
            return "User " + id + " deleted";
        }
        return "Something wrong";
    }

    @ShellMethod(value = "Delete Message by id")
    public String deleteMessage(Long id){
        if (messageDao.delete(id)){
            return "Message " + id + " deleted";
        }
        return "Something wrong";
    }

    @ShellMethod(value = "Delete Message Type by id")
    public String deleteMessageType(Long id){
        if (messageTypeDao.delete(id)){
            return "MessageType " + id + " deleted";
        }
        return "Something wrong";
    }
}
