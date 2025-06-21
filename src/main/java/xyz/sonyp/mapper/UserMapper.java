package xyz.sonyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import xyz.sonyp.po.User;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(Long id);

    List<User> queryAllUser(List<Long> ids);
}
