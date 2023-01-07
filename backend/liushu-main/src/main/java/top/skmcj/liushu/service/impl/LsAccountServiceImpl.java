package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.LsAccountMapper;
import top.skmcj.liushu.entity.LsAccount;
import top.skmcj.liushu.service.LsAccountService;
import top.skmcj.liushu.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * 流书网账户
 */
@Service
public class LsAccountServiceImpl extends ServiceImpl<LsAccountMapper, LsAccount> implements LsAccountService {

    /**
     * 流书网账户ID
     */
    @Value("${liushu.account.ls-id}")
    private int lsAccountId;

    /**
     * 流书网订单服务费百分比
     */
    @Value("${liushu.account.service-fee}")
    private BigDecimal serviceFee;

    /**
     * 增加流书网账户流动资金
     * @param inc 增量
     * @return
     */
    @Override
    public boolean addFundOfLS(BigDecimal inc) {
        // 获取账户信息
        LsAccount account = this.getById(lsAccountId);
        // 新资金信息
        LsAccount incFund = new LsAccount();
        incFund.setId(lsAccountId);
        incFund.setFund(BigDecimalUtil.add(account.getFund(), inc));
        boolean flag = this.updateById(incFund);
        return flag;
    }

    /**
     * 减少流书网账户流动资金
     * @param dec 减量
     * @return
     */
    @Override
    public boolean reduceFundOfLS(BigDecimal dec) {
        // 获取账户信息
        LsAccount account = this.getById(lsAccountId);
        // 新资金信息
        LsAccount incFund = new LsAccount();
        incFund.setId(lsAccountId);
        incFund.setFund(BigDecimalUtil.subtract(account.getFund(), dec));
        boolean flag = this.updateById(incFund);
        return flag;
    }

    /**
     * 处理订单(已完成)的资金流动
     * @param amount 订单实付金额(包含押金)
     * @param income 订单实际收入
     * @return
     */
    @Override
    public boolean handleOrderIncomeOfLS(BigDecimal amount, BigDecimal income) {
        // 获取账户信息
        LsAccount account = this.getById(lsAccountId);
        // 计算新的账户对象
        LsAccount lsAccount = new LsAccount();
        lsAccount.setId(lsAccountId);
        // 流动资金减少
        lsAccount.setFund(BigDecimalUtil.subtract(account.getFund(), amount));
        // 获取订单收入
        BigDecimal inc = BigDecimalUtil.multiply(income, serviceFee);
        lsAccount.setOrderRevenue(BigDecimalUtil.add(account.getOrderRevenue(), inc));
        boolean flag = this.updateById(lsAccount);
        return flag;
    }
}
