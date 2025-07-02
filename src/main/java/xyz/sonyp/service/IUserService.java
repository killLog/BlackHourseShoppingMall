package xyz.sonyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.sonyp.domain.po.User;
import xyz.sonyp.domain.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {

    void deductBalance(Long id, Integer money);

    List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance);

    UserVO queryUserAndAddressById(Long id);
}
