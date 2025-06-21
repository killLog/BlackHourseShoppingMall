package xyz.sonyp.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.sonyp.po.User;
import java.time.LocalDateTime;
import java.util.List;

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
    @Test
    void testSelect() {
        User user = userMapper.selectById(5L);
        System.out.println(user);
    }
    @Test
    void testSelects(){
        List<User> users = userMapper.selectByIds(List.of(1L,2L,3L,4L));
    }
    @Test
    void testUpdate() {
        User user = new User();
        user.setId(5L);
        user.setUsername("彭嵩洋");
        userMapper.updateById(user);
    }
    @Test
    void testDelete() {
        int name = userMapper.deleteById(5L);
        System.out.println(name);
    }
}

