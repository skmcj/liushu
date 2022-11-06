package top.skmcj.liushu.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.BookstoreDto;
import top.skmcj.liushu.dto.EmployeeDto;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.*;
import top.skmcj.liushu.util.*;
import top.skmcj.liushu.vo.BusinessAuthVo;
import top.skmcj.liushu.vo.ExamineBusinessVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家相关接口
 */
@Slf4j
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BookstoreService storeService;

    @Autowired
    private BookstoreDetailService detailService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StatisticalService statisticalService;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MailServerUtil mailServerUtil;

    @Value("${liushu.admin.username}")
    private String adminName;

    @Value("${liushu.admin.password}")
    private String adminPass;

    /**
     * 注册商家账号
     * @param employeeDTO
     * @param request
     * @return
     */
    @Transactional
    @PostMapping("/logon")
    public Result<String> logon(@RequestBody EmployeeDto employeeDTO, HttpServletRequest request) {
        // 获取验证码
        HttpSession session = request.getSession();
        Statistical statistical = statisticalService.getById(1);
        String realCode = (String) session.getAttribute("code");
        Long codeTime = (Long) session.getAttribute("codeTime");
        long nowTime = System.currentTimeMillis();
        // 校验验证码
        if(realCode == null) {
            return Result.error("请先获取验证码");
        }
        if(realCode.equals(employeeDTO.getCode()) && nowTime <= codeTime) {
            // 验证码校验成功
            // 根据邮箱查找商家
            LambdaQueryWrapper<Bookstore> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Bookstore::getEmail, employeeDTO.getEmail());
            Bookstore bookstore = storeService.getOne(queryWrapper);
            if(bookstore == null) {
                // 邮箱未注册
                Long record = statistical.getRecord();

                // 注册商家账户，操作bookstore表，新增一条记录
                Bookstore store = new Bookstore();
                // 设置一些默认值
                store.setEmail(employeeDTO.getEmail());
                store.setPassword(employeeDTO.getPassword());
                store.setPrefix(BaseConvertUtil.convert10To36(record + 1024));
                storeService.save(store);
                statistical.setRecord(record + 1);
                statisticalService.updateById(statistical);
                // 获取新增商家id
                session.setAttribute("storeId", store.getId());
                System.out.println("storeId => " + store.getId());
                // 注册员工，权限为店长
                Employee employee = new Employee();
                employee.setStoreId(store.getId());
                employee.setEmail(employeeDTO.getEmail());
                employee.setPassword(employeeDTO.getPassword());
                employee.setName("店长");
                // 设置权限为店长
                employee.setCompetence(0);
                employeeService.save(employee);
                return Result.success(StatusCodeEnum.BUSINESS_LOGON_OK);
            } else {
                // 邮箱已注册
                return Result.error(StatusCodeEnum.BUSINESS_LOGON_EXISTS);
            }
        } else if(nowTime > codeTime) {
            return Result.error(StatusCodeEnum.CHECK_CODE_EXPIRED);
        } else {
            return Result.error(StatusCodeEnum.CHECK_CODE_ERR);
        }
    }

    /**
     * 登录员工账号
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeDto> login(@RequestBody Employee employee, HttpServletRequest request) throws JWTDecodeException {
        Employee dEmployee;
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        EmployeeDto employeeDTO = new EmployeeDto();
        if(ValidateMessUtil.validateEmail(username)) {
            // 邮箱
            queryWrapper.eq(Employee::getEmail, username);
            dEmployee = employeeService.getOne(queryWrapper);
            employeeDTO.setEmail(dEmployee.getEmail());
        } else if(ValidateMessUtil.validatePhone(username)) {
            // 手机号
            queryWrapper.eq(Employee::getPhone, username);
            dEmployee = employeeService.getOne(queryWrapper);
            employeeDTO.setPhone(dEmployee.getPhone());
        } else {
            // 用户名
            queryWrapper.eq(Employee::getUsername, username);
            dEmployee = employeeService.getOne(queryWrapper);
            employeeDTO.setUsername(dEmployee.getUsername());
        }
        if(dEmployee != null) {
            // 查找用户成功
            if(dEmployee.getPassword().equals(employee.getPassword())) {
                // 密码正确，分情况登录
                Bookstore bookstore = storeService.getById(dEmployee.getStoreId());
                if(bookstore.getAuditStatus() == 0 && dEmployee.getCompetence() == 0) {
                    // 未提交审核资料，店长
                    HttpSession session = request.getSession();
                    session.setAttribute("storeId", bookstore.getId());
                    return Result.error(null, StatusCodeEnum.STORE_PROCESS_ONS);
                }
                if(bookstore.getAuditStatus() == 0 && dEmployee.getCompetence() != 0) {
                    // 未提交审核资料，员工
                    return Result.error(null, StatusCodeEnum.STORE_PROCESS_ENS);
                }
                if(bookstore.getAuditStatus() == 1) {
                    // 已提交审核资料，但处于审核中
                    return Result.error(null, StatusCodeEnum.STORE_PROCESS_NP);
                }
                // 审核通过或中途审核中(只有店长能登录)
                if(bookstore.getAuditStatus() == 2 || (bookstore.getAuditStatus() == 4 && dEmployee.getCompetence() == 0)) {
                    // 审核通过，登录
                    // 判断账号状态是否正常
                    if(dEmployee.getStatus() == 0) {
                        // 账号被禁用
                        return Result.error(employeeDTO, StatusCodeEnum.BUSINESS_LOGIN_SERR);
                    }

                    String token = JwtUtil.getToken(dEmployee);
                    employeeDTO.setId(dEmployee.getId());
                    employeeDTO.setStoreId(dEmployee.getStoreId());
                    employeeDTO.setName(dEmployee.getName());
                    employeeDTO.setIdNumber(dEmployee.getIdNumber());
                    employeeDTO.setSex(dEmployee.getSex());
                    employeeDTO.setCompetence(dEmployee.getCompetence());
                    employeeDTO.setStatus(dEmployee.getStatus());
                    employeeDTO.setToken(token);
                    return Result.success(employeeDTO, StatusCodeEnum.BUSINESS_LOGIN_OK);
                }
                if(bookstore.getAuditStatus() == 3) {
                    // 审核不通过
                    return Result.error(null, StatusCodeEnum.STORE_PROCESS_FP);
                }
                return Result.error(null, StatusCodeEnum.UNKNOW);
            } else {
                // 密码错误，登录失败
                return Result.error(null, StatusCodeEnum.LOGIN_PASS_ERR);
            }
        } else {
            // 查找用户失败
            return Result.error(null, StatusCodeEnum.LOGIN_USER_ERR);
        }
        // 登录成功后
        // 判断所属店铺是否已经审核通过，通过则执行下列，未通过则登录失败，返回提示
        // 如果所属店铺审核资料未提交且所登录员工权限为店长，转到审核资料填写界面(前端实现，后端返回相应状态)
    }

    /**
     * 提交审核信息
     * @param authVo
     * @return
     */
    @PostMapping("/auth")
    @Transactional
    public Result<BusinessAuthVo> submitAuthMess(@RequestBody BusinessAuthVo authVo, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long storeId = (Long) session.getAttribute("storeId");
        Bookstore store = storeService.getById(storeId);
        Bookstore bookstore = new Bookstore();
        BookstoreDetail storeDetail = new BookstoreDetail();
        // 基本信息
        bookstore.setId(storeId);
        bookstore.setStoreName(authVo.getStoreName());
        bookstore.setCover(authVo.getCover());
        bookstore.setAddress(authVo.getAddress());
        bookstore.setBusinessHours(authVo.getBusinessHours());
        bookstore.setDeliverFee(authVo.getDeliverFee());
        bookstore.setAuditStatus(1);
        storeService.updateById(bookstore);
        // 详细信息
        storeDetail.setStoreId(storeId);
        storeDetail.setBusinessLicense(authVo.getBusinessLicense());
        storeDetail.setLicenseImg(authVo.getLicenseImg());
        storeDetail.setEnvImgs(authVo.getEnvImgs());
        storeDetail.setOwnerName(authVo.getOwnerName());
        storeDetail.setPhone(authVo.getPhone());
        storeDetail.setIdNumber(authVo.getIdNumber());
        detailService.save(storeDetail);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEmail, store.getEmail());
        Employee employee = employeeService.getOne(queryWrapper);
        if(employee == null) {
            Employee iEmployee = new Employee();
            iEmployee.setName(authVo.getOwnerName());
            iEmployee.setPhone(authVo.getPhone());
            iEmployee.setIdNumber(authVo.getIdNumber());
            iEmployee.setStoreId(store.getId());
            iEmployee.setEmail(store.getEmail());
            iEmployee.setPassword(store.getPassword());
            iEmployee.setCompetence(0);
            employeeService.save(iEmployee);
        } else {
            employee.setIdNumber(authVo.getIdNumber());
            employee.setPhone(authVo.getPhone());
            employee.setName(authVo.getOwnerName());
            employeeService.updateById(employee);
        }
        session.removeAttribute("storeId");
        return Result.success(StatusCodeEnum.STORE_PROCESS_SERR);
    }

    /**
     * 修改认证信息
     * @param request
     * @return
     */
    @Transactional
    @PutMapping("/auth")
    public Result<String> updateAuthMess(@RequestBody BookstoreDetail detail, HttpServletRequest request) throws Exception {
        log.info("edit detail ==> {}", detail);
        // 校验权限
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        // 只有店长可以修改认证材料
        if(lEmployee.getCompetence() > 0) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        Bookstore bookstore = storeService.getById(lEmployee.getStoreId());
        Bookstore store = new Bookstore();
        store.setId(lEmployee.getStoreId());
        if(bookstore.getStatus() == 0) {
            // 检测当前店铺状态，只有处于休息状态(status -> 0)才可以修改
            // 且修改后，需再次审核(审核状态为4)，审核通过后才可以正常营业
            // 在此期间，只有店长能登录查看信息
            // 将不许修改字段置空
            detail.setPhone(null);
            store.setAuditStatus(4);
            detailService.updateById(detail);
            storeService.updateById(store);
            return Result.success("材料提交成功，请耐心等待审核");
        }else {
            return Result.error("需先关闭店铺才可以修改认证材料");
        }
    }

    /**
     * 修改店铺基本信息
     * @param store
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping("/info")
    public Result<String> updateStoreInfo(@RequestBody Bookstore store, HttpServletRequest request) throws Exception {
        // 校验权限
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        // 只有店长可以修改认证材料
        if(lEmployee.getCompetence() > 0) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        Bookstore bookstore = storeService.getById(store.getId());
        if(bookstore.getStatus() == 0) {
            // 将不许修改字段置为空，防止篡改
            store.setPassword(null);
            store.setStatus(null);
            store.setAuditStatus(null);
            store.setPrefix(null);
            store.setEmail(null);
            storeService.updateById(store);
            return Result.success("店铺信息修改完成");
        }else {
            return Result.error("需先关闭店铺才可以修改店铺信息");
        }
    }

    /**
     * 修改商家邮箱
     * @param authVo
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @PutMapping("/email")
    public Result<String> updateStoreEmail(@RequestBody BusinessAuthVo authVo, HttpServletRequest request) throws Exception {
        // 校验权限
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        // 只有店长可以修改认证材料
        if(lEmployee.getCompetence() > 0) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        // 修改
        Bookstore bookstore = new Bookstore();
        Employee employee = new Employee();
        bookstore.setId(lEmployee.getStoreId());
        bookstore.setEmail(authVo.getEmail());
        employee.setId(lEmployee.getId());
        employee.setEmail(authVo.getEmail());
        storeService.updateById(bookstore);
        employeeService.updateById(employee);
        return Result.success("邮箱修改成功，现在可以使用新的邮箱登录了");
    }

    /**
     * 修改商家手机号
     * @param authVo
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    @PutMapping("/phone")
    public Result<String> updateStorePhone(@RequestBody BusinessAuthVo authVo, HttpServletRequest request) throws Exception {
        // 校验权限
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        // 只有店长可以修改认证材料
        if(lEmployee.getCompetence() > 0) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        // 修改
        BookstoreDetail detail = new BookstoreDetail();
        Employee employee = new Employee();
        detail.setId(lEmployee.getStoreId());
        detail.setPhone(authVo.getPhone());
        employee.setId(lEmployee.getId());
        employee.setPhone(authVo.getPhone());
        detailService.updateById(detail);
        employeeService.updateById(employee);
        return Result.success("手机号修改成功，现在可以使用新的手机号登录了");
    }

    /**
     * 获取商家名称及门脸图
     * @param storeId
     * @param request
     * @return
     */
    @GetMapping("/name")
    public Result<Bookstore> getStoreName(Long storeId, HttpServletRequest request) {
        String prefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/img/";
        // System.out.println("storeId => " + storeId + ", type => " + storeId.getClass().getName());
        Bookstore store = storeService.getById(storeId);
        store.setCoverUrl(prefix + store.getCover());
        store.setPassword(null);
        store.setCreateTime(null);
        store.setAuditStatus(null);
        store.setUpdateTime(null);
        return Result.success(store);
    }

    /**
     * 获取商家完整信息
     * @param storeId
     * @param request
     * @return
     */
    @GetMapping("/info")
    public Result<BookstoreDto> getStoreInfo(Long storeId, HttpServletRequest request) {
        BookstoreDto bookstoreDto = new BookstoreDto();
        String prefix = CommonUtil.getImgDoMain(request);
        // System.out.println("storeId => " + storeId + ", type => " + storeId.getClass().getName());
        // 获取商家基本信息
        Bookstore store = storeService.getById(storeId);
        store.setCoverUrl(prefix + store.getCover());
        store.setPassword(null);
        // 获取单家详细信息
        LambdaQueryWrapper<BookstoreDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookstoreDetail::getStoreId, storeId);
        BookstoreDetail detail = detailService.getOne(queryWrapper);
        // 给相关图片添加协议前缀
        List<String> businessLicense = detail.getBusinessLicenseList();
        List<String> licenseImg = detail.getLicenseImgList();
        List<String> envImgs = detail.getEnvImgsList();
        List<String> businessLicenseUrl = businessLicense.stream().map(item -> {
            item = prefix + item;
            return item;
        }).collect(Collectors.toList());
        List<String> licenseImgUrl = licenseImg.stream().map(item -> {
            item = prefix + item;
            return item;
        }).collect(Collectors.toList());
        List<String> envImgsUrl = envImgs.stream().map(item -> {
            item = prefix + item;
            return item;
        }).collect(Collectors.toList());
        detail.setBusinessLicenseUrl(JSON.toJSONString(businessLicenseUrl));
        detail.setLicenseImgUrl(JSON.toJSONString(licenseImgUrl));
        detail.setEnvImgsUrl(JSON.toJSONString(envImgsUrl));
        // 封装进dto里
        bookstoreDto.setBookstore(store);
        bookstoreDto.setBookstoreDetail(detail);
        return Result.success(bookstoreDto);
    }

    /**
     * 修改店铺经营状态
     *   - 0 -> 休息
     *   - 1 -> 营业
     * @return
     */
    @GetMapping("/status")
    public Result<String> updateStoreStatus(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 0) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        Long storeId = lEmployee.getStoreId();
        Bookstore store = storeService.getById(storeId);
        Integer status = store.getStatus();
        if(status == 1) {
            // 关店，status 1 -> 0
            // 直接修改，无需校验
            store.setStatus(0);
            storeService.updateById(store);
            return Result.success("店铺营业状态修改成功");
        } else {
            // 开店，status 0 -> 1
            // 校验店铺信息是否完整，若不完整，需补充完整才可以提交
            // 是否校验不通过
            boolean flag = this.checkStoreMess(store);
            if(flag) {
                // 校验失败
                return Result.error("店铺信息尚不完善，完善后才可以开店");
            } else {
                store.setStatus(1);
                storeService.updateById(store);
                return Result.success("店铺营业状态修改成功");
            }
        }
    }

    /**
     * 审核商家
     * @return
     */
    @PostMapping("/examine")
    public Result<String> examineBusiness(@RequestBody ExamineBusinessVo examineVo) {
        if(!adminName.equals(examineVo.getUsername()) || !adminPass.equals(examineVo.getPassword())) {
            return Result.error("您没有权限执行该接口");
        }
        // 检查员工表是否有商家店长的记录
        Bookstore store = storeService.getById(examineVo.getStoreId());
        if(store == null) {
            return Result.error("该商家不存在");
        }
        store.setAuditStatus(examineVo.getResult() == 1 ? 2 : 3);
        storeService.updateById(store);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getEmail, store.getEmail());
        Employee employee = employeeService.getOne(queryWrapper);
        if(employee == null) {
            Employee iEmployee = new Employee();
            iEmployee.setName("店长");
            iEmployee.setStoreId(store.getId());
            iEmployee.setEmail(store.getEmail());
            iEmployee.setPassword(store.getPassword());
            iEmployee.setCompetence(0);
            employeeService.save(iEmployee);
        }
        Map<String, String> data = new HashMap<>();
        data.put("cause", "您之前所提交的书店审核资料结果已出");
        data.put("result", examineVo.getResult().toString());
        if(examineVo.getResult() == 1) {
            try {
                // 发送邮件
                mailServerUtil.sendExTemplateMail(store.getEmail(), "审核状态", data);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("邮件发送失败", StatusCodeEnum.STORE_PROCESS_OK);
            }
            return Result.success("邮件发送成功", StatusCodeEnum.STORE_PROCESS_OK);
        } else {
            try {
                // 发送邮件
                mailServerUtil.sendExTemplateMail(store.getEmail(), "审核状态", data);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("邮件发送失败", StatusCodeEnum.STORE_PROCESS_FP);
            }
            return Result.error("邮件发送成功", StatusCodeEnum.STORE_PROCESS_FP);
        }
    }

    /**
     * 商家店内分类
     */
    /**
     * 分页获取店内分类信息
     * @param page
     * @param pageSize
     * @param storeId
     * @return
     */
    @GetMapping("/cate/page")
    public Result<Page> getCatePage(int page, int pageSize, String storeId, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<GoodsCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsCate::getStoreId, storeId != null ? storeId : lEmployee.getStoreId());
        queryWrapper.orderByDesc(GoodsCate::getSort);
        queryWrapper.orderByDesc(GoodsCate::getUpdateTime);
        goodsCateService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 根据id获取指定分类
     * @param id
     * @return
     */
    @GetMapping("/cate")
    public Result<GoodsCate> getCateById(Long id) {
        GoodsCate goodsCate = goodsCateService.getById(id);
        return Result.success(goodsCate);
    }

    /**
     * 获取所有店内分类
     * @param storeId
     * @return
     */
    @GetMapping("/cate/all")
    public Result<List<GoodsCate>> getAllCate(Long storeId, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        LambdaQueryWrapper<GoodsCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsCate::getStoreId, storeId != null ? storeId : lEmployee.getStoreId());
        List<GoodsCate> list = goodsCateService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 根据id修改指定分类
     * @param cate
     * @return
     */
    @PutMapping("/cate")
    public Result<String> editCate(@RequestBody GoodsCate cate, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        cate.setStoreId(lEmployee.getStoreId());
        cate.setUpdateUser(lEmployee.getId());
        boolean isEdit = goodsCateService.updateById(cate);
        if(!isEdit) {
            return Result.error(StatusCodeEnum.UPDATE_ERR);
        }
        return Result.success(StatusCodeEnum.UPDATE_OK);
    }

    /**
     * 根据id删除指定分类
     * @param id
     * @return
     */
    @DeleteMapping("/cate")
    public Result<String> delCate(Long id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        // 判断该分类下有无相关书籍，有则不能删除
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getGoodsCateId, id);
        long count = bookService.count(queryWrapper);
        if(count > 0) {
            return Result.error(StatusCodeEnum.GOODSCATE_DEL_ERR);
        }
        boolean isDel = goodsCateService.removeById(id);
        if(!isDel) {
            return Result.error(StatusCodeEnum.DELETE_ERR);
        }
        return Result.success(StatusCodeEnum.DELETE_OK);
    }

    /**
     * 新增店内分类
     * @param cate
     * @return
     */
    @PostMapping("/cate")
    public Result<String> saveCate(@RequestBody GoodsCate cate, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        cate.setStoreId(lEmployee.getStoreId());
        cate.setCreateUser(lEmployee.getId());
        cate.setUpdateUser(lEmployee.getId());
        boolean isSave = goodsCateService.save(cate);
        if(!isSave) {
            return Result.error(StatusCodeEnum.SAVE_ERR);
        }
        return Result.success(StatusCodeEnum.SAVE_OK);
    }

    /**
     * 校验书店基本信息
     * @param store
     * @return
     */
    private boolean checkStoreMess(Bookstore store) {
        boolean flag = false;
        if(store.getAuditStatus() != 2) {
            flag = true;
        }else if(store.getStoreName() == null) {
            flag = true;
        }else if(store.getCover() == null) {
            flag = true;
        }else if(store.getAddress() == null) {
            flag = true;
        }else if(store.getBusinessHours() == null) {
            flag = true;
        }else if(store.getDistribution() == null) {
            flag = true;
        }else if(store.getDeliverFee() == null) {
            flag = true;
        }else if(store.getBorrowDay() == null) {
            flag = true;
        }else if(store.getRenewDay() == null) {
            flag = true;
        }
        return flag;
    }
}
