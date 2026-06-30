package com.example.repository;

import com.example.model.Role;
import com.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private  final JdbcTemplate jdbcTemplate;
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    //find user by id
    public Optional<User>getUserById(long id){
        String sql="SELECT*FROM users WHERE id=?";
        List<User> users=jdbcTemplate.query(sql,(rs, rowNum) -> {
            User user =new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setRole(Role.valueOf(rs.getString("role")));
            return user;
        },id);
        return users.stream().findFirst();
    }

    //find all users
    public List<User> findAll() {
        String sql = "SELECT*FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setRole(Role.valueOf(rs.getString("role")));
            return user;
        });
    }
    //add new user
    public void save(User user){
        String sql="INSERT INTO users(name,email,role)VALUES(?,?,?)";
        jdbcTemplate.update(sql,user.getName(),user.getEmail(),user.getRole().name());
        }
    //update user
    public void updateUser(User user){
        String sql="UPDATE users SET name=?, email=?, role=? WHERE id=?";
        jdbcTemplate.update(sql,user.getName(),user.getEmail(),user.getRole().name(),user.getId());
    }
    //delete user
    public void deleteById(long id){
        String sql="DELETE FROM users WHERE id=?";
        jdbcTemplate.update(sql,id);
    }

}
