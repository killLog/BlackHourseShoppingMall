package xyz.sonyp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.sonyp.domain.po.Address;
import xyz.sonyp.domain.po.User;
import xyz.sonyp.domain.vo.AddressVO;
import xyz.sonyp.domain.vo.UserVO;
import xyz.sonyp.mapper.UserMapper;
import xyz.sonyp.service.IUserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 虽然加了 @Transactional，但它不是用来解决并发冲突的，而是为了：
     * 保证当前操作作为一个整体提交或回滚
     * 配合乐观锁使用，增强并发场景下的数据一致性
     *
     * @param id
     * @param money
     */
    @Override
    @Transactional //事务注解，要么全部成功，要么全部失败回滚
    public void deductBalance(Long id, Integer money) {
        //1.查询用户
        User user = getById(id);
        //2.校验用户状态
        if (user == null || user.getStatus() == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常！");
        }
        //3.校验用户余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足以扣减！");
        }
        //4.扣减金额 update tb_user set balance = balance - ? baseMapper.deductBalance(id,money);
        int remainBalance = user.getBalance() - money;
        // 使用乐观锁更新余额：只有当数据库中的余额仍等于查询出的原始值时才允许更新
        // 目的是避免多线程下因并发操作导致余额计算错误或负数余额等问题
        lambdaUpdate().set(User::getBalance, remainBalance).set(remainBalance == 0, User::getStatus, 2) // 若余额为0，则冻结账户
                .eq(User::getId, id)                                      // 确保更新的是指定用户
                .eq(User::getBalance, user.getBalance())                  // 乐观锁：确保余额未被其他线程修改
                .update();                                                // 构建好了记得执行
    }

    /**
     * 根据动态条件查询用户列表（支持模糊匹配、状态筛选和余额范围查询）
     * <p>该方法在业务层实现类中使用 MyBatis Plus 的 LambdaQueryWrapper 构建查询条件，
     * * 支持根据用户名、用户状态、最小余额和最大余额进行组合查询。</p>
     *
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

    /**
     * <p>根据用户ID查询用户信息及其地址列表，并封装为 UserVO 返回。</p>
     *
     * <p><strong>【为什么使用 Db.lambdaQuery(Address.class)？】</strong></p>
     *
     * <p>在本业务中，User 和 Address 是两个独立的实体，
     * 分别由 UserService 和 AddressService 管理。
     * 当我们需要在 UserService 中获取 Address 数据时，
     * 若通过注入 IAddressService 来调用其方法，
     * 而 AddressService 中又恰好注入了 UserService（例如地址业务中需要回查用户信息），
     * 就会形成如下依赖链：</p>
     *
     * <p>UserService --> AddressService --> UserService</p>
     *
     * <p>此时 Spring 在初始化 Bean 时会出现循环依赖问题，抛出异常：
     * "Requested bean is currently in creation: Is there an unresolvable circular reference?"</p>
     *
     * <p><strong>【解决方案】</strong></p>
     *
     * <p>使用 MyBatis-Plus 提供的 Db.lambdaQuery(Address.class) 工具类进行直接查询，
     * 可避免注入 AddressService，从而打破循环依赖链，使程序正常运行。</p>
     *
     * <p><strong>【适用范围】</strong></p>
     *
     * <p>该方式适用于简单查询场景，不涉及复杂业务逻辑或事务管理。
     * 若业务较复杂，建议通过合理设计 Service 依赖关系、
     * 使用 @Lazy 注解或重构模块结构来解决。</p>
     */
    @Override
    public UserVO queryUserAndAddressById(Long id) {
        //1.查询用户
        User user = getById(id);
        if (user == null || user.getStatus() == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常！");
        }
        //2.查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, user.getId())
                .list();
        //3.封装VO
        //3.1转User的PO为VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //3.2转地址VO
        if (CollUtil.isNotEmpty(addresses)) {
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

    /**
     * <p>根据用户ID集合批量查询用户信息及其关联地址，并封装为 UserVO 列表返回。</p>
     *
     * <p><strong>【方法亮点】</strong></p>
     *
     * <p>1. <strong>分层清晰、逻辑分明：</strong><br>
     *    方法分为三大步骤：查询用户 → 查询地址 → 转换并组装 VO，结构清晰易读，便于维护和扩展。</p>
     *
     * <p>2. <strong>空值安全处理：</strong><br>
     *    使用 CollUtil.isNotEmpty 对用户列表进行判空，避免后续空指针异常，增强程序健壮性。</p>
     *
     * <p>3. <strong>批量查询优化性能：</strong><br>
     *    通过 listByIds 实现一次数据库查询获取多个用户数据，相比循环单查大幅提升效率。</p>
     *
     * <p>4. <strong>使用 Db.lambdaQuery 巧妙规避依赖注入问题：</strong><br>
     *    直接调用 Db.lambdaQuery(Address.class) 查询地址信息，避免因 AddressService 注入引发的 Service 层循环依赖。</p>
     *
     * <p>5. <strong>Stream 流式编程提升可读性：</strong><br>
     *    使用 stream 提取用户ID、分组地址等操作，代码简洁流畅，符合现代 Java 编程风格。</p>
     *
     * <p>6. <strong>Map 分组提升查找效率：</strong><br>
     *    将地址按 userId 分组存入 Map，后续通过 key 快速获取对应地址列表，时间复杂度 O(1)，优于遍历查找。</p>
     *
     * <p>7. <strong>对象拷贝统一规范：</strong><br>
     *    使用 BeanUtil.copyProperties 和 copyToList 统一完成 PO 到 VO 的转换，简化代码，降低出错率。</p>
     *
     * <p>8. <strong>提前初始化集合容量：</strong><br>
     *    初始化 userVO 列表时传入初始容量，减少扩容次数，提高性能。</p>
     *
     * <p><strong>【适用场景】</strong></p>
     *
     * <p>适用于需要批量查询用户及关联数据（如地址、订单等）并返回聚合视图的业务场景，是典型的“一对多”数据聚合封装案例。</p>
     */
    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        //1.查询用户
        List<User> users = listByIds(ids);
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }
        //2.查询地址
        //2.1获取用户id集合
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        //2.2根据用户id查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIds)
                .list();
        //2.3转换地址VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        //2.4对用户地址集合分组处理：相同用户的放入一个集合（组）当中
        Map<Long, List<AddressVO>> addressMap = new HashMap<>(0);
        if (CollUtil.isNotEmpty(addressVOList)) {
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }
        //3.转换VO返回
        List<UserVO> list = new ArrayList<>(userIds.size());
        for (User user : users) {
            //3.1转换user的PO为VO
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            list.add(userVO);
            //3.2转换地址VO
            userVO.setAddresses(addressMap.get(user.getId()));
        }
        return list;
    }
}
