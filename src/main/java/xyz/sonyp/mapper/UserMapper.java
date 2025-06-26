package xyz.sonyp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import xyz.sonyp.domain.po.User;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(Long id);

    List<User> queryAllUser(List<Long> ids);

    //自定义SQL的mapper层方法
    void updateBalanceByIds(@Param("ew")LambdaQueryWrapper<User> wrapper,@Param("amount") int amount);

    @Update("update user set balance = balance - #{money} where id = #{id}")
    void deductBalance(Long id, Integer money);
}
