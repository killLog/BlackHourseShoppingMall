package xyz.sonyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.sonyp.mapper.UserMapper;
import xyz.sonyp.domain.po.User;
import xyz.sonyp.service.IUserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductBalance(Long id, Integer money) {
        //1.查询用户
        User user = getById(id);
        //2.校验用户状态
        if (user == null || user.getStatus() == null || user.getStatus() == 2){
            throw new RuntimeException("用户状态异常！");
        }
        //3.校验用户余额是否充足
        if (user.getBalance() < money){
            throw new RuntimeException("用户余额不足以扣减！");
        }
        //4.扣减金额 update tb_user set balance = balance - ?
        baseMapper.deductBalance(id,money);
    }
}
