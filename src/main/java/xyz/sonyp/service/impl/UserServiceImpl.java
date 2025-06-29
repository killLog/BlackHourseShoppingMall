package xyz.sonyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.sonyp.mapper.UserMapper;
import xyz.sonyp.domain.po.User;
import xyz.sonyp.service.IUserService;

import java.util.List;

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

    /**
     * 根据动态条件查询用户列表（支持模糊匹配、状态筛选和余额范围查询）
     * <p>该方法在业务层实现类中使用 MyBatis Plus 的 LambdaQueryWrapper 构建查询条件，
     *  * 支持根据用户名、用户状态、最小余额和最大余额进行组合查询。</p>
     * @param name
     * @param status
     * @param minBalance
     * @param maxBalance
     * @return list
     */
    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }
}
