package com.den.crudsql.dao.impl;

import com.den.crudsql.dao.MessageTypeDao;
import com.den.crudsql.model.MessageType;
import com.den.crudsql.model.impl.RealMessageType;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@NoArgsConstructor
public class MessageTypeDaoImpl implements MessageTypeDao {
    private NamedParameterJdbcOperations jdbcTemplate;
    private SimpleJdbcInsert messageTypeInsert;
    private MessageTypeWithDetailExtractor extractor;
    private QueryService queryService;

    @Autowired
    public MessageTypeDaoImpl(QueryService queryService) {
        this.queryService = queryService;
    }

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.messageTypeInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("message_type")
                .usingGeneratedKeyColumns("id");
        extractor = new MessageTypeWithDetailExtractor();
    }

    @Override
    public Long create(MessageType messageType) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("Name", messageType.getName())
                .addValue("Template", messageType.getTemplate());
        Long id = messageTypeInsert.executeAndReturnKey(parameterSource).longValue();
        return id;
    }

    @Override
    public MessageType findById(Long id) {
        String query = queryService.getQuery("messageType.findById");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        List<MessageType> messageTypes = jdbcTemplate.query(query, parameterSource, extractor);
        return messageTypes.isEmpty() ? null : messageTypes.get(0);
    }

    @Override
    public boolean delete(Long id) {
        String deleteById = queryService.getQuery("messageType.deleteById");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedRows = jdbcTemplate.update(deleteById, parameterSource);
        return deletedRows > 0;
    }

    @Override
    public boolean update(MessageType messageType) {
        String update = queryService.getQuery("messageType.update");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", messageType.getId())
                .addValue("Name", messageType.getName())
                .addValue("Template", messageType.getTemplate());
        int updatedRows = jdbcTemplate.update(update, parameterSource);
        return updatedRows > 0;
    }

    public List<MessageType> findAll() {
        String query = queryService.getQuery("messageType.findAll");
        return jdbcTemplate.query(query, extractor);
    }


    private final class MessageTypeWithDetailExtractor implements ResultSetExtractor<List<MessageType>> {

        @Override
        public List<MessageType> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<MessageType> messageTypes = new ArrayList<>();
            while (rs.next()) {
                MessageType messageType = new RealMessageType();
                messageType.setId(rs.getLong("id"));
                messageType.setName(rs.getString("Name"));
                messageType.setTemplate(rs.getString("Template"));
                messageTypes.add(messageType);
            }
            return messageTypes;
        }
    }
}
