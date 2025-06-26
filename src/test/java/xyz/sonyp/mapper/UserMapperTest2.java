package xyz.sonyp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.sonyp.domain.po.User;
import java.util.List;

/*利用MyBatisPlus进行半自动化操作*/
//解决的痛点：一些非常复杂的SQL语句的WHERE条件是非常复杂的，使用MyBatisPlus可以帮助我们解决复杂WHERE
@SpringBootTest
class UserMapperTest2 {
    @Autowired
    private UserMapper userMapper;
    /**
     * 我们可以利用MyBatisPlus的Wrapper来构建复杂的Where条件，然后自定义SQL语句中剩下的部分
     */
    @Test
    void test01() {
        List<Long> ids = List.of(1L, 2L, 4L);
        int amount = 200;//需求就是每个人的账户余额扣200，因此这个变量就是扣掉的量
        //1.基于Wrapper构建where条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getId,ids);
        //2.自定义剩下的SQL（调用自定义SQL）
        //2.1 在mapper对应方法的形参中用Param注解声明wrapper变量名称，必须是ew不能是别的名字
        //2.2 给mapper自定义方法在对应的mapper.xml中编写真正的自定义SQL
        //2.3 调用自定义SQL
        userMapper.updateBalanceByIds(wrapper,amount);
    }
}

