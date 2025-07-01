package xyz.sonyp.service.impl;

import xyz.sonyp.domain.po.Address;
import xyz.sonyp.mapper.AddressMapper;
import xyz.sonyp.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author killLog
 * @since 2025-07-01
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
