package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.dao.mapper.AddressMapper;
import top.skmcj.liushu.entity.Address;
import top.skmcj.liushu.service.AddressService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
    /**
     * 设置默认地址
     * @param id
     */
    @Override
    @Transactional
    public void setDefault(Long id, Long userId) {
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getUserId, userId);
        List<Address> list = this.list(queryWrapper);
        List<Long> idList = new ArrayList<>();
        list.stream().forEach(item -> {
            if(item.getIsDefault() == 1) {
                idList.add(item.getId());
            }
        });
        idList.stream().forEach(item -> {
            Address address = new Address();
            address.setId(item);
            address.setIsDefault(0);
            this.updateById(address);
        });
        Address address = new Address();
        address.setId(id);
        address.setIsDefault(1);
        this.updateById(address);
    }
}
