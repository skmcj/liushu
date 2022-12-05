package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.entity.Address;

public interface AddressService extends IService<Address> {

    /**
     * 设置默认地址
     * @param id
     */
    void setDefault(Long id, Long userId);
}
