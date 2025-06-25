package xyz.sonyp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.sonyp.po.User;
import java.time.LocalDateTime;
import java.util.List;
//业务层接口继承IService 业务层实现类实现业务层接口然后再继承ServiceImpl CRUD更加方便了
@SpringBootTest
class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        //user.setId(5L); //非数值类型或者不给就是默认以雪花算法生成
        user.setUsername("Lisa");
        user.setPassword("123");
        user.setPhone("188235240000");
        user.setBalance(200);
        user.setInfo("{\"age\": 20, \"intro\": \"韩国女团\", \"gender\": \"male\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
    }

    @Test
    void testSelectUser() {
        List<User> users = userService.listByIds(List.of(1L,2L,3L));
        users.forEach(System.out::println);
    }
}