package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Employee;

@Data
public class EmployeeDTO extends Employee {
    /**
     * 验证码
     */
    private String code;

    /**
     * token
     */
    private String token;
}
