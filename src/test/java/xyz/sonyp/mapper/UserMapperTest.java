package xyz.sonyp.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
    void testSelects() {
        List<User> users = userMapper.selectByIds(List.of(1L, 2L, 3L, 4L));
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

    //mp的复杂查询
    @Test
    void testQueryWrapper() {
        //1.构建查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        //2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    //使用mp的wrapper进行更新操作
    @Test
    void testWrapperUpdate() {
        //1.要更新的数据
        User user = new User();
        user.setBalance(2000);
        //2.更新的where条件
        Wrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", "Jack");
        //3.执行更新
        userMapper.update(user, wrapper);
    }

    //更新id为1，2，4的用户的余额，扣200
    @Test
    void testWrapperUpdate2() {
        List<Long> ids = List.of(1L, 2L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")
                .in("id",ids);
        userMapper.update(null, wrapper);
    }
    //解决前面的硬编码问题，使用lambdaWrapper
    @Test
    void testLambdaQueryWrapper() {
        //1.构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId,User::getUsername,User::getInfo,User::getBalance,User::getCreateTime)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        //2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }


}

