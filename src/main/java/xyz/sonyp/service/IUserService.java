package xyz.sonyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.sonyp.domain.po.User;

public interface IUserService extends IService<User> {

    void deductBalance(Long id, Integer money);
}
