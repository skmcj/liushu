package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.entity.Employee;
import top.skmcj.liushu.service.EmployeeService;
import top.skmcj.liushu.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 员工相关接口
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    /**
     * REST 风格
     * GET - 查
     * POST - 增
     * PUT - 改
     * DELETE - 删
     */

    @Autowired
    private EmployeeService employeeService;

    /**
     * 保存员工
     * @param employee
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping
    public Result<String> save(@RequestBody Employee employee, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        employee.setCreateUser(lEmployee.getId());
        employee.setUpdateUser(lEmployee.getId());
        employeeService.save(employee);
        return Result.success(StatusCodeEnum.EMPLOYEE_ADD_OK);
    }

    /**
     * 修改员工信息
     * @param employee
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping
    public Result<String> editById(@RequestBody Employee employee, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        employee.setUpdateUser(lEmployee.getId());
        employeeService.updateById(employee);
        return Result.success(StatusCodeEnum.EMPLOYEE_EDIT_OK);
    }

    /**
     * 根据id删除员工
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam Long id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        employeeService.removeById(id);
        return Result.success(StatusCodeEnum.EMPLOYEE_DEL_OK);
    }

    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    @GetMapping
    public Result<Employee> getEmployeeById(@RequestParam Long id) {
        log.info("get employee id => " + id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee, StatusCodeEnum.EMPLOYEE_SEARCH_OK);
    }

    /**
     * 分页查询员工信息
     * @param page
     * @param pageSize
     * @param storeId
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String storeId, String name) {
        // 构建分页构造器
        Page pageInfo = new Page(page, pageSize);
        // 构建条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq(Employee::getStoreId, storeId);
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行请求
        employeeService.page(pageInfo, queryWrapper);
        // 剔除密码
        List<Employee> records = pageInfo.getRecords();
        List<Employee> list = records.stream().map((item) -> {
            Employee smallEm = new Employee();
            BeanUtils.copyProperties(item, smallEm);
            smallEm.setPassword(null);
            return smallEm;
        }).collect(Collectors.toList());
        pageInfo.setRecords(list);
        return Result.success(pageInfo);
    }
}
