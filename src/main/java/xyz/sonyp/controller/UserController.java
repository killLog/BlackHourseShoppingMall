package xyz.sonyp.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.sonyp.domain.dto.UserFormDTO;
import xyz.sonyp.domain.po.User;
import xyz.sonyp.domain.vo.UserVO;
import xyz.sonyp.service.IUserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理接口")
public class UserController {
    /**
     * 使用 Lombok 的 @RequiredArgsConstructor 自动生成包含 final 字段的构造方法，
     * Spring 会通过构造器自动注入 IUserService 实现类。
     */
    private final IUserService userService;

    @PostMapping
    @Operation(summary = "新增用户接口")
    public void insertUser(@RequestBody UserFormDTO userFormDTO) {
        //1.先把DTO拷贝到PO
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        //2.然后再直接用mybatisPlus的新增（都不用自己写接口方法和接口实现类方法）{但还是要按照之前教的在业务层接口进行继承以及业务层实现类继承和实现哈}
        userService.save(user);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除用户接口")
    public void deleteUser(@Parameter(description = "用户id") @PathVariable Long id) {
        userService.removeById(id);
    }

    @GetMapping("{id}")
    @Operation(summary = "根据id查询用户接口")
    public UserVO selectUser(@Parameter(description = "用户id") @PathVariable Long id) {
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @GetMapping
    @Operation(summary = "根据id批量查询用户接口")
    public List<UserVO> selectUsers(@Parameter(description = "用户id集合") @RequestParam List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    //复杂业务接口
    @PutMapping("{id}/deduction/{money}")
    @Operation(summary = "根据id扣减用户余额接口")
    public void deductMoneyById(
            @Parameter(description = "用户id") @PathVariable Long id,
            @Parameter(description = "要扣减的金额") @PathVariable Integer money
    ) {
        userService.deductBalance(id,money);
    }

}

