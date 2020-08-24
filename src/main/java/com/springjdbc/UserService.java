package com.springjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * JdbcTemplate只是对JDBC操作的一个简单封装，它的目的是尽量减少手动编写try(resource) {...}的代码，
 * 对于查询，主要通过RowMapper实现了JDBC结果集到Java对象的转换。
 * <ol><b>总结一下JdbcTemplate的用法:</b>
 * <li>针对简单查询，优选query()和queryForObject()，因为只需提供SQL语句、参数和RowMapper；</li>
 * <li>针对更新操作，优选update()，因为只需提供SQL语句和参数；</li>
 * <li>任何复杂的操作，最终也可以通过execute(ConnectionCallback)实现，因为拿到Connection就可以做任何JDBC操作。</li>
 * </ol>
 */
@Component
public class UserService {
    
    /**
     * Spring提供了一个JdbcTemplate，可以方便地让我们操作JDBC，采用Template模式，
     * 提供了一系列以回调为特点的工具方法，目的是避免繁琐的try...catch语句
     */
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    /********** 查询方法  **********/
    public User getUserById(long id) {
        // 注意传入的是ConnectionCallback:
        return jdbcTemplate.execute((Connection conn) -> {
            // 可以直接使用conn实例，不要释放它，回调结束后JdbcTemplate自动释放:
            // 在内部手动创建的PreparedStatement、ResultSet必须用try(...)释放:
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
                ps.setObject(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new User(rs.getLong("id"), 
                                rs.getString("email"), 
                                rs.getString("password"), 
                                rs.getString("name"));
                    }
                    throw new RuntimeException("user not found by id.");
                }
            }
        });
    }
    
    public User getUserByName(String name) {
        // 需要传入SQL语句，以及PreparedStatementCallback:
        return jdbcTemplate.execute("SELECT * FROM users WHERE name = ?", (PreparedStatement ps) -> {
            // PreparedStatement实例已经由JdbcTemplate创建，并在回调后自动释放:
            ps.setObject(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User( rs.getLong("id"), 
                            rs.getString("email"), 
                            rs.getString("password"), 
                            rs.getString("name"));
                }
                throw new RuntimeException("user not found by id.");
            }
        });
    }
    
    public User getUserByEmail(String email) {
        // 传入SQL，参数和RowMapper实例:
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    // 将ResultSet的当前行映射为一个JavaBean:
                    return new User(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name"));
                });
    }
    
    /**
     * RowMapper不一定返回JavaBean，实际上它可以返回任何Java对象
     */
    public long getUsers() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", null, (ResultSet rs, int rowNum) -> {
            // SELECT COUNT(*)查询只有一列，取第一列数据:
            return rs.getLong(1);
        });
    }
    
    /**
     * 我们期望返回多行记录，而不是一行，可以用query()方法
     * 
     * 直接使用Spring提供的BeanPropertyRowMapper。如果数据库表的结构恰好和JavaBean的属性名称一致，
     * 那么BeanPropertyRowMapper就可以直接把一行记录按列名转换为JavaBean。
     */
    public List<User> getUsers(int pageIndex) {
        int limit = 20;
        int offset = limit * (pageIndex - 1);
        return jdbcTemplate.query("SELECT * FROM users LIMIT ? OFFSET ?", new Object[] { limit, offset },
                new BeanPropertyRowMapper<>(User.class));
    }
    
    
    /********** insert|update|delete方法  **********/
    public void updateUser(User user) {
        // 传入SQL，SQL参数，返回更新的行数:
        if (1 != jdbcTemplate.update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by id");
        }
    }
    
    /**
     * 如果某一列是自增列（例如自增主键），通常，我们需要获取插入后的自增值。JdbcTemplate提供了一个KeyHolder来简化这一操作
     * 
     * 加上@Transactional，表示此public方法自动具有事务支持，在一个事务方法中，如果程序判断需要回滚事务，只需抛出RuntimeException
     * 
     * Spring使用声明式事务，最终也是通过执行JDBC事务来实现功能的，那么，一个事务方法，如何获知当前是否存在事务？答案是使用ThreadLocal。
     * Spring总是把JDBC相关的Connection和TransactionStatus实例绑定到ThreadLocal。如果一个事务方法从ThreadLocal未取到事务，
     * 那么它会打开一个新的JDBC连接，同时开启一个新的事务，否则，它就直接使用从ThreadLocal获取的JDBC连接以及TransactionStatus
     * 事务能正确传播的前提是，方法调用是在一个线程内才行。
     * 
     * 扩展：如果我们想实现跨线程传播事务呢？
     * 原理很简单，就是要想办法把当前线程绑定到ThreadLocal的Connection和TransactionStatus实例传递给新线程，但实现起来非常复杂，根据异常回滚更加复杂，不推荐自己去实现
     * 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User register(String email, String password, String name) {
        // 创建一个KeyHolder:
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update(
            // 参数1:PreparedStatementCreator
            (conn) -> {
                // 创建PreparedStatement时，必须指定RETURN_GENERATED_KEYS:
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users(email,password,name) VALUES(?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, email);
                ps.setObject(2, password);
                ps.setObject(3, name);
                return ps;
            },
            // 参数2:KeyHolder
            holder)
        ) {
            throw new RuntimeException("Insert failed.");
        }
        // 从KeyHolder中获取返回的自增值:
        return new User(holder.getKey().longValue(), email, password, name);
    }
    
    
    
    
}
