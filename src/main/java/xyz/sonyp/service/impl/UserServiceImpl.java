package xyz.sonyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.sonyp.mapper.UserMapper;
import xyz.sonyp.domain.po.User;
import xyz.sonyp.service.IUserService;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 虽然加了 @Transactional，但它不是用来解决并发冲突的，而是为了：
     * 保证当前操作作为一个整体提交或回滚
     * 配合乐观锁使用，增强并发场景下的数据一致性
     * @param id
     * @param money
     */
    @Override
    @Transactional //事务注解，要么全部成功，要么全部失败回滚
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
        //4.扣减金额 update tb_user set balance = balance - ? baseMapper.deductBalance(id,money);
        int remainBalance = user.getBalance() - money;
        // 使用乐观锁更新余额：只有当数据库中的余额仍等于查询出的原始值时才允许更新
        // 目的是避免多线程下因并发操作导致余额计算错误或负数余额等问题
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2) // 若余额为0，则冻结账户
                .eq(User::getId, id)                                      // 确保更新的是指定用户
                .eq(User::getBalance, user.getBalance())                  // 乐观锁：确保余额未被其他线程修改
                .update();                                                // 构建好了记得执行
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
