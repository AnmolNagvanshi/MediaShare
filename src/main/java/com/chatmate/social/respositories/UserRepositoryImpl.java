package com.chatmate.social.respositories;

import com.chatmate.social.entity.User;
import com.chatmate.social.exceptions.UserAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long create(User user) {
        final String SQL_CREATE_USER =
                "INSERT INTO user_data (first_name, last_name, email, password, is_active, date_joined) " +
                        "VALUES(?, ?, ?, ?, true, CURRENT_DATE)";
//        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        SQL_CREATE_USER,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                return ps;
            }, keyHolder);

            Long id = (Long) keyHolder.getKeys().get("id");
            return id;
        } catch (Exception e) {
            throw new UserAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        final String SQL_FIND_BY_EMAIL = "SELECT id, first_name, last_name, email, password, is_active, date_joined " +
                "FROM user_data WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper());
            if (user == null || !BCrypt.checkpw(password, user.getPassword()))
                throw new UserAuthException("Invalid email/password");
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new UserAuthException("Invalid email/password");
        }

    }

    @Override
    public Integer getCountByEmail(String email) {
        final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM user_data WHERE email = ?";
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public User findById(Long userId) {
        final String SQL_FIND_BY_ID = "SELECT id, first_name, last_name, email, password, is_active, date_joined " +
                "FROM user_data WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserAuthException("User with id " + userId + " doesn't exist");
        }
    }

    @Override
    public List<User> findAll() {
        final String SQL_FIND_ALL_USERS = "SELECT id, first_name, last_name, email, password, is_active, date_joined " +
                "FROM user_data";
        return jdbcTemplate.query(SQL_FIND_ALL_USERS, userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBoolean("is_active"),
                rs.getDate("date_joined")
        );
    }

}
