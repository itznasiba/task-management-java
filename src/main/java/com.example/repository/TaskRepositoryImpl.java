package com.example.repository;

import com.example.model.Role;
import com.example.model.Status;
import com.example.model.Task;
import com.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    // adding new task
    public void save(Task task) {
        String sql = "INSERT INTO tasks(title,description,status,user_id)VALUES(?,?,?,?)";
        Long user_id = (task.getAssignedUser() != null) ? task.getAssignedUser().getId() : null;
        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getStatus().name(), user_id);
    }

    // get task by id
    public Optional<Task> findById(long id) {
        String sql = "SELECT*FROM tasks WHERE id=?";
        List<Task> tasks = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(Status.valueOf(rs.getString("status")));

            long userId =rs.getLong("user_id");
            if(userId>0){
                User user = new User();
                user.setId(userId);
                task.setAssignedUser(user);
            }
            return task;
        },id);
        return tasks.stream().findFirst();
    }
    // get all tasks

    public List<Task> findAll(){
        String sql="SELECT*FROM tasks";
        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            Task task=new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(Status.valueOf(rs.getString("status")));
            //task.setId(rs.getLong("assignedUser"));

            long userId=rs.getLong("user_id");
            if(userId>0){
                User user=new User();
                user.setId(userId);
                task.setAssignedUser(user);
            }
            return task;
        });
    }
    // get tasks by user id
    public List<Task> getTasksByUserId(long userId){
        String sql="SELECT t.id AS task_id, t.title, t.description, t.status AS task_status, "+
                "u.id AS user_id, u.name AS user_name, u.email AS user_email, u.role AS user_role "+
                "FROM tasks t "+"INNER JOIN users u ON t.user_id = u.id "+
                " WHERE t.user_id = ?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            Task task =new Task();
            task.setId(rs.getLong("task_id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(Status.valueOf(rs.getString("task_status")));

           User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("user_name"));
            user.setEmail(rs.getString("user_email"));
            user.setRole(Role.valueOf(rs.getString("user_role")));

           task.setAssignedUser(user);
           return task;
        },userId);

    }
        // delete task
    public void deleteById(long id){
        String sql="DELETE FROM tasks WHERE id=?";
        jdbcTemplate.update(sql,id);
    }
        //edit task(change status of task)
    public void updateStatus(long id, Status status){
        String sql="UPDATE tasks SET status=? WHERE id =?";
        jdbcTemplate.update(sql, status.name(),id);
    }

    // assign task
    public void assignTask(long userId,long taskId){
        String sql="UPDATE tasks SET user_id=? WHERE id=?";
        jdbcTemplate.update(sql,userId,taskId);
    }
}

