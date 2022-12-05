package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.entity.Address;
import top.skmcj.liushu.entity.User;
import top.skmcj.liushu.service.AddressService;
import top.skmcj.liushu.util.CommonUtil;
import top.skmcj.liushu.vo.AddressVo;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, HttpServletRequest request) throws Exception {
        User user = CommonUtil.getTokenMessByUser(request);
        Page<Address> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getUserId, user.getId());
        // 排序，保证默认地址在前面
        queryWrapper.orderByDesc(Address::getIsDefault);
        queryWrapper.orderByDesc(Address::getUpdateTime);
        addressService.page(pageInfo, queryWrapper);
        System.out.println("user ==> " + user);
        return Result.success(pageInfo, StatusCodeEnum.GET_OK);
    }

    /**
     * 根据ID获取地址
     * @param id
     * @return
     */
    @GetMapping
    public Result<Address> getById(int id) {
        Address address = addressService.getById(id);
        return Result.success(address, StatusCodeEnum.GET_OK);
    }

    /**
     * 设置默认地址
     * @param address
     * @return
     */
    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody AddressVo address, HttpServletRequest request) throws Exception {
        User user = CommonUtil.getTokenMessByUser(request);
        addressService.setDefault(address.getId(), user.getId());
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 添加默认地址
     * @param address
     * @return
     */
    @Transactional
    @PostMapping
    public Result<String> addAddress(@RequestBody Address address, HttpServletRequest request) throws Exception {
        User user = CommonUtil.getTokenMessByUser(request);
        address.setUserId(user.getId());
        boolean saveFlag = addressService.save(address);
        if(!saveFlag) return Result.error(StatusCodeEnum.ADD_ERR);
        if(address.getIsDefault() == 1) {
            addressService.setDefault(address.getId(), user.getId());
        }
        return Result.success(StatusCodeEnum.ADD_OK);
    }

    /**
     * 修改默认地址
     * @param address
     * @return
     */
    @Transactional
    @PutMapping
    public Result<String> editAddress(@RequestBody Address address, HttpServletRequest request) throws Exception {
        User user = CommonUtil.getTokenMessByUser(request);
        if(address.getId() == null) {
            return Result.error("地址信息不全");
        }
        if(address.getIsDefault() == 1) {
            addressService.setDefault(address.getId(), user.getId());
        }
        address.setUserId(null);
        address.setIsDefault(null);
        address.setCreateTime(null);
        address.setUpdateTime(null);
        boolean flag = addressService.updateById(address);
        if(!flag) return Result.error(StatusCodeEnum.UPDATE_ERR);
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteAddress(int id) {
        boolean flag = addressService.removeById(id);
        if(!flag) return Result.error(StatusCodeEnum.DELETE_ERR);
        return Result.success(StatusCodeEnum.DELETE_OK);
    }
}
