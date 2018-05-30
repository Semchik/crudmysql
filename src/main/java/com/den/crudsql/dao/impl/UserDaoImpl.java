package com.den.crudsql.dao.impl;

import com.den.crudsql.dao.UserDao;
import com.den.crudsql.model.User;
import com.den.crudsql.model.impl.RealUser;
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
public class UserDaoImpl implements UserDao {
    private NamedParameterJdbcOperations jdbcTemplate;
    private SimpleJdbcInsert userInsert;
    private UserWithDetailExtractor extractor;
    private QueryService queryService;

    @Autowired
    public UserDaoImpl(QueryService queryService) {
        this.queryService = queryService;
    }

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
        extractor = new UserWithDetailExtractor();
    }

    @Override
    public Long create(User user) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", user.getName());
        Long id = userInsert.executeAndReturnKey(parameterSource).longValue();
        return id;
    }

    @Override
    public User findById(Long id) {
        String query = queryService.getQuery("user.findById");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        List<User> users = jdbcTemplate.query(query, parameterSource, extractor);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public boolean delete(Long id) {
        String deleteById = queryService.getQuery("user.deleteById");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedRows = jdbcTemplate.update(deleteById, parameterSource);
        return deletedRows > 0;
    }

    @Override
    public boolean update(User user) {
        String update = queryService.getQuery("user.update");
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName());
        int updatedRows = jdbcTemplate.update(update, parameterSource);
        return updatedRows > 0;
    }

    public List<User> findAll() {
        String query = queryService.getQuery("user.findAll");
        return jdbcTemplate.query(query, extractor);
    }

    private final class UserWithDetailExtractor implements ResultSetExtractor<List<User>> {

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new RealUser();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                users.add(user);
            }
            return users;
        }
    }
}
