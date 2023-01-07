package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.entity.LsAccount;

import java.math.BigDecimal;

/**
 * 流书网账户
 */
public interface LsAccountService extends IService<LsAccount> {

    boolean addFundOfLS(BigDecimal inc);

    boolean reduceFundOfLS(BigDecimal dec);

    boolean handleOrderIncomeOfLS(BigDecimal amount, BigDecimal income);
}
