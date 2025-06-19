package xyz.sonyp.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.sonyp.po.User;
import java.time.LocalDateTime;

@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setUsername("SongY");
        user.setPassword("123");
        user.setPhone("15502376719");
        user.setBalance(200);
        user.setInfo("{\"age\": 20, \"intro\": \"佛系青年\", \"gender\": \"male\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }
}

