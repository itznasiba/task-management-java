package com.example.repository;

import com.example.model.Role;
import com.example.model.Status;
import com.example.model.Task;
import com.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Currency;
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
        String sql=" SELECT u.id AS user_id, u.name AS user_name, u.email AS user_email, u.role, "+
                " t.id AS task_id, t.title, t.description, t.status AS task_status "+
                " FROM users u "+" LEFT JOIN tasks t ON u.id = t.user_id "+" WHERE u.id=?";
        //List<User> users=jdbcTemplate.query(sql,(rs, rowNum) ->

        User user=jdbcTemplate.query(sql,rs->{
            User currentUser = null;
            while(rs.next()){
                if(currentUser==null){
                    currentUser =new User();
                    currentUser.setId(rs.getLong("user_id"));
                    currentUser.setName(rs.getString("user_name"));
                    currentUser.setEmail(rs.getString("user_email"));
                    currentUser.setRole(Role.valueOf(rs.getString("role")));
                    currentUser.setTasks(new ArrayList<>());
                }
                long taskId=rs.getLong("task_id");
                if(taskId!=0){
                    Task task=new Task();
                    task.setId(taskId);
                    task.setTitle(rs.getString("title"));
                    task.setDescription(rs.getString("description"));
                    task.setStatus(Status.valueOf(rs.getString("task_status")));
                    task.setAssignedUser(currentUser);

                    currentUser.getTasks().add(task);
                }

            }
            return currentUser;
        },id);
        return Optional.ofNullable(user);
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
