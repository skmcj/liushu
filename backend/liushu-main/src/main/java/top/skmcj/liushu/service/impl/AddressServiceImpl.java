package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.AddressMapper;
import top.skmcj.liushu.entity.Address;
import top.skmcj.liushu.service.AddressService;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
}
