package xyz.sonyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.sonyp.po.User;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void saveUser(@Param("user") User user);

    void deleteUser(@Param("id") Long id);

    void updateUser(@Param("user") User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryAllUser(List<Long> ids);
}
