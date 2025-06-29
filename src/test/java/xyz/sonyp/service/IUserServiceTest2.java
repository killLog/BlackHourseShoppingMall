package xyz.sonyp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.sonyp.domain.po.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//批处理-批量新增测试类
@SpringBootTest
class IUserServiceTest2 {
    @Autowired
    private IUserService userService;

    //单个新增的方法
    private User buildUser(int i){
        User user = new User();
        user.setUsername("user"+i);
        user.setPassword("test");
        user.setPhone(""+(18688190000L + i));
        user.setBalance(2000);
        user.setInfo("{\"age\": 18, \"intro\": \"测试数据\", \"gender\": \"male\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }
    /**
     * 批量操作原理说明：
     * 老方法是每插入一条数据就执行一次 SQL 插入语句，效率较低；
     * 真正的批量操作（如批量插入）会将多条插入语句合并为一条 SQL，一次性发送给数据库执行，
     * 减少了网络往返和事务开销，从而显著提升插入性能。
     *<p>
     * 示例对比：
     * - 单条插入：INSERT INTO table VALUES(...); × N 次
     * - 批量插入：INSERT INTO table VALUES(...), (...), (...) × 1 次
     *</p>
     * 优势体现：
     * - 减少 JDBC 调用次数
     * - 降低事务提交频率（若开启事务）
     * - 提升数据库处理效率
     *<p>
     * 🔥需要提前在application.yml中的datasource中的url最后加上&rewriteBatchedStatements=true
     *</p>
     */
    @Test //需要提前在
    void batchInsertUser() {
        // 准备一个容量为 1000 的用户集合用于批量插入
        List<User> userList = new ArrayList<>(1000);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            // 构建用户对象并添加到集合
            userList.add(buildUser(i));
            // 每满 1000 条执行一次批量插入
            if (i % 1000 == 0) {
                userService.saveBatch(userList); // MyBatis Plus saveBatch 底层基于 JDBC 批处理实现
                userList.clear(); // 清空集合，准备下一批数据
            }
        }
        // 可选：记录耗时日志便于性能分析
        long duration = System.currentTimeMillis() - start;
        System.out.println("插入 10 万条数据耗时：" + duration + " ms");
    }

}