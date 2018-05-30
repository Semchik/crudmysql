package com.den.crudsql.dao.impl;

import com.den.crudsql.dao.MessageDao;
import com.den.crudsql.model.Message;
import com.den.crudsql.model.impl.RealMessage;
import com.den.crudsql.service.QueryService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@NoArgsConstructor
public class MessageDaoImpl implements MessageDao {
    private NamedParameterJdbcOperations jdbcTemplate;
    private SimpleJdbcInsert messageInsert;
    private MessageWithDetailExtractor extractor;
    private QueryService queryService;

    @Autowired
    public MessageDaoImpl(QueryService queryService) {
        this.queryService = queryService;
    }

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.messageInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Message")
                .usingGeneratedKeyColumns("Message_id");
        extractor = new MessageWithDetailExtractor();
    }

    @Override
    public Long create(Message message) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("MessageType_id", message.getMessageTypeId())
                .addValue("Author", message.getAuthor())
                .addValue("User_Id", message.getUserId())
                .addValue("Project_Id", message.getProjectId())
                .addValue("Stage_Id", message.getStageId())
                .addValue("Task_Id", message.getTaskId())
                .addValue("Artefact_Id", message.getArtefactId())
                .addValue("Date", Date.valueOf(message.getDate()));
        Long id = messageInsert.executeAndReturnKey(parameterSource).longValue();
        return id;
    }

    @Override
    public Message findById(Long id) {
        String query = queryService.getQuery("message.findById");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        List<Message> messages = jdbcTemplate.query(query, parameterSource, extractor);
        return messages.isEmpty() ? null : messages.get(0);
    }

    @Override
    public boolean delete(Long id) {
        String deleteById = queryService.getQuery("message.deleteById");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedRows = jdbcTemplate.update(deleteById, parameterSource);
        return deletedRows > 0;
    }

    @Override
    public boolean update(Message message) {
        String update = queryService.getQuery("message.update");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", message.getMessageId())
                .addValue("MessageType_id", message.getMessageTypeId())
                .addValue("Author", message.getAuthor())
                .addValue("User_Id", message.getUserId())
                .addValue("Project_Id", message.getProjectId())
                .addValue("Stage_Id", message.getStageId())
                .addValue("Task_Id", message.getTaskId())
                .addValue("Artefact_Id", message.getArtefactId())
                .addValue("Date", Date.valueOf(message.getDate()));
        int updatedRows = jdbcTemplate.update(update, parameterSource);
        return updatedRows > 0;
    }

    public List<Message> findAll() {
        String query = queryService.getQuery("message.findAll");
        return jdbcTemplate.query(query, extractor);
    }

    private final class MessageWithDetailExtractor implements ResultSetExtractor<List<Message>> {

        @Override
        public List<Message> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                Message message = new RealMessage();
                message.setMessageId(rs.getLong("Message_id"));
                message.setArtefactId(rs.getLong("Artefact_Id"));
                message.setAuthor(rs.getLong("Author"));
                message.setMessageTypeId(rs.getLong("MessageType_id"));
                message.setProjectId(rs.getLong("Project_Id"));
                message.setStageId(rs.getLong("Stage_Id"));
                message.setTaskId(rs.getLong("Task_Id"));
                message.setUserId(rs.getLong("User_Id"));
                message.setDate(rs.getDate("Date").toLocalDate());

                messages.add(message);
            }
            return messages;
        }
    }
}
