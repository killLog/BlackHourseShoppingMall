package xyz.sonyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.sonyp.mapper.UserMapper;
import xyz.sonyp.po.User;
import xyz.sonyp.service.IUserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
