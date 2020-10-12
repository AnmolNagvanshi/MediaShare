package com.chatmate.social.respositories;

import com.chatmate.social.entity.User;
import com.chatmate.social.exceptions.EtAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Integer create(String firstName, String lastName,
                          String email, String password) {
        final String SQL_CREATE_USER =
                "INSERT INTO user_data (first_name, last_name, email, password, is_active, date_joined) " +
                        "VALUES(?, ?, ?, ?, true, CURRENT_DATE)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        SQL_CREATE_USER,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("user_id");

        } catch (Exception e) {
            throw new EtAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        final String SQL_FIND_BY_EMAIL = "SELECT user_id, first_name, last_name, email, password " +
                "FROM user_data WHERE email = ?";
        User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[]{email}, userRowMapper());
        if (user == null || !BCrypt.checkpw(password, user.getPassword()))
            throw new EtAuthException("Invalid email/password");
        return user;
    }

    @Override
    public Integer getCountByEmail(String email) {
        final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM user_data WHERE email = ?";
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        final String SQL_FIND_BY_ID = "SELECT user_id, first_name, last_name, email, password " +
                "FROM user_data WHERE user_id = ?";
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper());
    }

    @Override
    public List<User> findAll() {
        final String SQL_FIND_ALL_USERS = "SELECT user_id, first_name, last_name, email, password " +
                "FROM user_data";
        return jdbcTemplate.query(SQL_FIND_ALL_USERS, userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        };
    }

}
