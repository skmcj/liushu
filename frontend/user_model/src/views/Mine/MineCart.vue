<template>
  <!-- 我的-购物车组件 -->
  <div class="mine mine-cart">
    <div class="title-box">
      <span class="icon ic-address-books"></span>
      <span class="text">我的购物车</span>
      <div class="hr"></div>
      <div class="delete-btn" @click.stop="handleDeleteItem">
        <i class="ic-delete"></i>
        <span>删除</span>
      </div>
    </div>
    <div class="mine-cart-main-box">
      <!-- <div class="checkbox-all">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange"
          >全选</el-checkbox
        >
      </div> -->
      <!-- 店铺列表 -->
      <div class="mine-cart-shop-list">
        <!-- 店铺项 -->
        <div class="mine-cart-shop-item" v-for="(obj, key) in cartObj" :key="'cart-item-' + key">
          <!-- 店铺标题 -->
          <div class="mine-cart-shop-title">
            <div class="left">
              <el-checkbox
                :indeterminate="obj.count > 0 && !obj.checkAll"
                v-model="obj.checkAll"
                @change="handleChangeShop(obj)"></el-checkbox>
              <span @click.stop="handleStoreName(obj.storeId)">{{ obj.storeName }}</span>
            </div>
            <i class="ic-more-1"></i>
          </div>
          <!-- 店内商品列表 -->
          <div class="mine-cart-book-list">
            <div class="mine-cart-book-item" v-for="(item, index) in obj.products" :key="key + index">
              <div class="left">
                <el-checkbox
                  :true-label="1"
                  :false-label="0"
                  v-model="item.checked"
                  @change="handleChangeBook(item)"></el-checkbox>
                <div class="book-mess-box">
                  <div class="book-cover">
                    <img :src="item.bookCover" alt="cover" />
                  </div>
                  <div class="book-mess">
                    <div class="book-mess-item">
                      <span class="label">图书名称</span>
                      <span class="text">{{ item.bookName }}</span>
                    </div>
                    <div class="book-mess-item">
                      <span class="label">图书简介</span>
                      <span class="text row">{{ item.bookOutline }}</span>
                    </div>
                    <div class="book-mess-item">
                      <span class="label">借阅时长</span>
                      <span class="text">{{ item.borrowTime + ' 天' }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="right">
                <div class="bottom">
                  <div class="mess">
                    <span class="label">金额</span>
                    <span class="text">{{ '￥ ' + item.amount }}</span>
                  </div>
                  <el-input-number
                    class="double-mini"
                    :min="1"
                    :max="item.inventory"
                    v-model="item.quantity"
                    @change="
                      (currentValue, oldValue) => handleQuantity(item, currentValue, oldValue)
                    "></el-input-number>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 底部信息 -->
      <div class="mine-cart-bottom-box">
        <div class="left">
          <div>
            <el-checkbox v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
          </div>
          <div class="mess-box">
            <div class="mess-item">
              <span class="label">借阅费：</span>
              <span class="text">{{ '￥ ' + $keepTwoNum(allBorrowCost) }}</span>
            </div>
            <div class="mess-item">
              <span class="label">另需包装费：</span>
              <span class="text">{{ '￥ ' + $keepTwoNum(allPackingCost) }}</span>
            </div>
            <div class="mess-item">
              <span class="label">预估配送费：</span>
              <span class="text">{{ '￥ ' + $keepTwoNum(alldeliveryFee) }}</span>
            </div>
            <div class="mess-item">
              <span class="label">押金：</span>
              <span class="text">{{ '￥ ' + $keepTwoNum(allDeposit) }}</span>
            </div>
            <div class="mess-item all">
              <span class="label">总金额：</span>
              <span class="text">{{ '￥ ' + $keepTwoNum(allAmount) }}</span>
            </div>
          </div>
        </div>
        <div class="right">
          <el-button class="btn" type="primary" round @click.stop="handleSettlement">{{
            '结算' + (cartCount > 0 ? '(' + cartCount + ')' : '')
          }}</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getAllCartApi, updateCartMessApi, deleteCartApi } from '@/api/cartApi';

export default {
  data() {
    return {
      // 购物车信息是否有改变
      changeFlag: false,
      // 是否全选
      checkAll: false,
      // 购物车图书选中数量
      cartCount: 0,
      // 用户购物车项
      cartList: [],
      // 购物车对象
      cartObj: {},
      // 总借阅费
      allBorrowCost: 0,
      // 总包装费
      allPackingCost: 0,
      // 总配送费
      alldeliveryFee: 0,
      // 押金
      allDeposit: 0,
      // 总金额
      allAmount: 0,
      orderSet: {}
    };
  },
  created() {
    this.getCartMess();
  },
  methods: {
    /**
     * 获取购物车信息
     */
    getCartMess() {
      this.initAboutData();
      getAllCartApi().then(
        res => {
          if (res.data.flag) {
            this.cartList = res.data.data;
            this.cartObj = this.packingCartObj(this.cartList);
          } else {
            this.$showMsg('网络繁忙，请稍后刷新重试', { type: 'warning' });
          }
        },
        err => {
          this.$showMsg('出错啦~', { type: 'error' });
        }
      );
    },
    /**
     * 初始化相关参数
     */
    initAboutData() {
      this.changeFlag = false;
      this.checkAll = false;
      this.cartCount = 0;
      this.orderSet = {};
      // 总借阅费
      this.allBorrowCost = 0;
      // 总包装费
      this.allPackingCost = 0;
      // 总配送费
      this.alldeliveryFee = 0;
      // 押金
      this.allDeposit = 0;
      // 总金额
      this.allAmount = 0;
    },
    /**
     * 包装
     * @param {Array} list
     */
    packingCartObj(list) {
      let obj = {};
      for (let i = 0; i < list.length; i++) {
        if (Object.prototype.hasOwnProperty.call(obj, list[i].storeId)) {
          // obj 已含有指定商家
          if (list[i].checked === 1) obj[list[i].storeId].count = obj[list[i].storeId].count + 1;
          obj[list[i].storeId].products.push(list[i]);
        } else {
          // obj 未包含指定商家
          let shopItem = {
            storeId: list[i].storeId,
            storeName: list[i].storeName,
            deliveryFee: 2,
            // 店铺全选状态
            checkAll: false,
            // 店铺选中
            count: 0,
            products: []
          };
          shopItem.products.push(list[i]);
          if (list[i].checked === 1) shopItem.count = shopItem.count + 1;
          obj[list[i].storeId] = shopItem;
        }
      }
      this.computeCheckAll(obj);
      return obj;
    },
    /**
     * 计算全选状态
     * @param {Object} obj
     */
    computeCheckAll(obj) {
      for (let key in obj) {
        if (obj[key].products.length === obj[key].count) {
          obj[key].checkAll = true;
        }
      }
    },
    /**
     * 店铺全选按钮状态改变
     */
    handleChangeShop(obj) {
      // console.log('shop obj =>', obj);
      let changeCount = 0;
      this.changeShopCheck(obj, obj.checkAll);
    },
    /**
     * 图书复选框状态改变
     */
    handleChangeBook(item) {
      // console.log('book item =>', item);
      // 根据状态改变店铺内数量
      if (item.checked) {
        this.cartObj[item.storeId].count += 1;
        this.cartCount += 1;
        // 计算费用
        this.changeItemCost(item, true);
      } else {
        this.cartObj[item.storeId].count -= 1;
        this.cartCount -= 1;
        // 计算费用
        this.changeItemCost(item, false);
      }
      // 改变店铺全选状态
      if (this.cartObj[item.storeId].count === this.cartObj[item.storeId].products.length) {
        this.cartObj[item.storeId].checkAll = true;
      } else {
        this.cartObj[item.storeId].checkAll = false;
      }
    },
    /**
     * 改变商品数量
     */
    handleQuantity(item, currentVal, oldVal) {
      // console.log('change quantity =>', item, currentVal, oldVal, currentVal - oldVal);
      this.changeFlag = true;
      let q = currentVal - oldVal;
      let updateAmount = (item.borrowCost + item.packingCost + item.deposit) * q;
      item.amount += updateAmount;
      if (item.checked) {
        // 如果该商品被选中，需改变相应的金额信息
        // 总借阅费
        this.allBorrowCost += item.borrowCost * q;
        // 总包装费
        this.allPackingCost += item.packingCost * q;
        // 押金
        this.allDeposit += item.deposit * q;
        // 总金额
        this.allAmount += updateAmount;
      }
    },
    /**
     * 全选改变
     */
    handleCheckAllChange(val) {
      // console.log('check all =>', val);
      if (val) {
        // 全选
        for (let key in this.cartObj) {
          this.cartObj[key].checkAll = true;
          this.changeShopCheck(this.cartObj[key], true);
        }
      } else {
        // 取消全选
        for (let key in this.cartObj) {
          this.cartObj[key].checkAll = false;
          this.changeShopCheck(this.cartObj[key], false);
        }
      }
    },
    /**
     * 改变店铺选中状态
     * @param {Boolean} flag true - 选中 | false - 取消选中
     */
    changeShopCheck(obj, flag = true) {
      let changeCount = 0;
      if (flag) {
        // 全选
        obj.products.forEach(item => {
          if (item.checked === 0) {
            item.checked = 1;
            changeCount += 1;
            // 计算费用
            this.changeItemCost(item, true);
          }
        });
        obj.count = obj.products.length;
        this.cartCount += changeCount;
      } else {
        // 取消全选
        obj.products.forEach(item => {
          if (item.checked === 1) {
            item.checked = 0;
            changeCount += 1;
            // 计算费用
            this.changeItemCost(item, false);
          }
        });
        this.cartCount -= changeCount;
        obj.count = 0;
      }
    },
    /**
     * 计算选中项金额
     */
    changeItemCost(item, flag = true) {
      let q = item.quantity;
      let adddeliveryFee = 0;
      if (flag) {
        // 加
        // 总借阅费
        this.allBorrowCost += item.borrowCost * q;
        // 总包装费
        this.allPackingCost += item.packingCost * q;
        // 总配送费
        if (!this.orderSet[item.storeId]) {
          this.orderSet[item.storeId] = 1;
          adddeliveryFee = this.cartObj[item.storeId].deliveryFee;
        } else {
          this.orderSet[item.storeId] += 1;
        }
        this.alldeliveryFee += adddeliveryFee;
        // 押金
        this.allDeposit += item.deposit * q;
        // 总金额
        this.allAmount += item.amount + adddeliveryFee;
      } else {
        // 减
        // 总借阅费
        this.allBorrowCost -= item.borrowCost * q;
        // 总包装费
        this.allPackingCost -= item.packingCost * q;
        // 总配送费
        if (this.orderSet[item.storeId] === 1) {
          adddeliveryFee = this.cartObj[item.storeId].deliveryFee;
          this.orderSet[item.storeId] -= 1;
        } else {
          this.orderSet[item.storeId] -= 1;
        }
        this.alldeliveryFee -= adddeliveryFee;
        // 押金
        this.allDeposit -= item.deposit * q;
        // 总金额
        this.allAmount -= item.amount + adddeliveryFee;
      }
    },
    /**
     * 点击商家名
     */
    handleStoreName(id) {
      this.$router.push({ path: '/shop', query: { id } });
    },
    /**
     * 删除购物车项
     */
    handleDeleteItem() {
      this.$confirm('是否确认删除选中项, 此操作不可恢复?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'danger'
      })
        .then(() => {
          let checkList = [];
          // 只留下未选中的项
          this.cartList.forEach(item => {
            if (item.checked) {
              checkList.push(item.id);
            }
          });
          if (checkList.length > 0) {
            let ids = checkList.join(',');
            deleteCartApi(ids).then(res => {
              if (res.data.flag) {
                this.$showMsg('删除成功', {
                  type: 'success',
                  closeFunc: () => {
                    this.getCartMess();
                  }
                });
              } else {
                this.$showMsg('网络繁忙，请稍后再试', { type: 'error' });
              }
            });
          }
        })
        .catch(() => {});
    },
    /**
     * 结算
     */
    handleSettlement() {
      let checkedObj = this.getCheckedGoods();
      if (this.$isEmpty(checkedObj)) {
        this.$showMsg('请先选中商品');
      } else {
        // 存储选中对象
        this.$store.dispatch('setSettlementObj', {
          // 类型
          type: 'cart',
          // 商品数据
          data: checkedObj,
          // 总借阅费
          allBorrowCost: this.allBorrowCost,
          // 总包装费
          allPackingCost: this.allPackingCost,
          // 总配送费
          alldeliveryFee: this.alldeliveryFee,
          // 押金
          allDeposit: this.allDeposit,
          // 总金额
          allAmount: this.allAmount
        });
        // 特别注意: 如果传递path那么默认的 会将 params 忽略,就是说你就算传了也没有用,所以在使用params传递数据的时候一定要注意,不能使用path,只能使用name来指定路由的目标对象
        this.$router.push('/settlement');
      }
      // console.log('check obj =>', checkedObj);
    },
    /**
     * 获取选中商品
     */
    getCheckedGoods() {
      let checkedObj = {};
      for (let key in this.cartObj) {
        this.cartObj[key].products.forEach(item => {
          if (item.checked === 1) {
            if (Object.prototype.hasOwnProperty.call(checkedObj, item.storeId)) {
              // 商铺已添加
              checkedObj[item.storeId].products.push(item);
            } else {
              // 商铺未添加
              // obj 未包含指定商家
              let shopItem = {
                storeId: item.storeId,
                storeName: item.storeName,
                deliveryFee: 2,
                products: []
              };
              shopItem.products.push(item);
              checkedObj[item.storeId] = shopItem;
            }
          }
        });
      }
      return checkedObj;
    },
    /**
     * 更新购物车信息
     * @param {Array} carts
     */
    updateCart(carts) {
      updateCartMessApi(carts).then(res => {});
    }
  },
  beforeDestroy() {
    if (this.changeFlag) {
      this.updateCart(this.cartList);
    }
  },
  watch: {
    // 监听购物车选择商品数量
    cartCount(val) {
      if (this.cartCount === this.cartList.length) {
        this.checkAll = true;
      } else {
        this.checkAll = false;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.title-box {
  position: relative;
  .delete-btn {
    cursor: pointer;
    user-select: none;
    position: absolute;
    bottom: 6px;
    right: 12px;
    display: flex;
    align-items: center;
    color: #999;
    &:hover {
      color: var(--danger);
    }
    i {
      transition: color 0.5s ease-in-out;
      font-size: 16px;
    }
    span {
      transition: color 0.5s ease-in-out;
      margin-left: 5px;
      font-size: 14px;
    }
  }
}
.mine-cart-main-box {
  margin-top: 18px;
  width: 100%;
  display: flex;
  flex-direction: column;
  .checkbox-all {
    width: 100%;
    height: 36px;
    box-sizing: border-box;
    padding: 0 12px;
    display: flex;
    align-items: center;
  }
  .mine-cart-shop-list {
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 0 12px;
    box-sizing: border-box;
    padding-bottom: 120px;
    .mine-cart-shop-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      box-sizing: border-box;
      border-radius: 8px;
      border: 1px solid #c9cfc1;
      overflow: hidden;
      .mine-cart-shop-title {
        user-select: none;
        width: 100%;
        height: 36px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        box-sizing: border-box;
        padding: 0 12px;
        .left {
          .el-checkbox {
            margin-right: 12px;
          }
          span {
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            color: var(--primary-text);
          }
        }
        border-bottom: 1px solid #c9cfc1;
      }
      .mine-cart-book-list {
        width: 100%;
        display: flex;
        flex-direction: column;
        box-sizing: border-box;
        padding: 6px 12px 6px 24px;
        .mine-cart-book-item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          box-sizing: border-box;
          padding: 12px 8px;
          .el-checkbox {
            margin-right: 18px;
          }
          .left {
            width: 64%;
            display: flex;
            align-items: center;
            .book-mess-box {
              display: flex;
              align-items: center;
              .book-cover {
                width: 108px;
                height: 108px;
                display: flex;
                flex-shrink: 0;
                align-items: center;
                justify-content: center;
                overflow: hidden;
                border-radius: 5px;
                img {
                  width: 100%;
                  height: 100%;
                  object-fit: cover;
                }
              }
              .book-mess {
                min-height: 98px;
                margin-left: 8px;
                display: flex;
                flex-direction: column;
                .book-mess-item {
                  display: flex;
                  .label {
                    min-width: 76px;
                    text-align: left;
                    font-size: 12px;
                    color: var(--primary-text);
                  }
                  .text {
                    font-size: 12px;
                    color: #6b798e;
                    &.row {
                      display: -webkit-box;
                      overflow: hidden;
                      -webkit-line-clamp: 3;
                      -webkit-box-orient: vertical;
                    }
                  }
                  &.book-mess-item {
                    margin-top: 3px;
                  }
                }
              }
            }
          }
          .right {
            height: 100%;
            display: flex;
            align-items: flex-end;
            flex-direction: column;
            // justify-content: space-between;
            justify-content: flex-end;
            .top {
              display: flex;
              flex-direction: row-reverse;
              align-items: center;
              i {
                cursor: pointer;
                font-size: 14px;
                color: #999;
                transition: color 0.5s ease-in-out;
                &:hover {
                  color: var(--error);
                }
              }
            }
            .bottom {
              display: flex;
              flex-direction: row-reverse;
              align-items: center;
              .mess {
                min-width: 85px;
                user-select: none;
                .label {
                  font-size: 14px;
                  color: #999;
                }
                .text {
                  margin-left: 6px;
                  font-size: 14px;
                  color: var(--primary-text);
                }
              }
              .double-mini {
                margin-right: 36px;
              }
            }
          }
          & + .mine-cart-book-item {
            margin-top: 8px;
            border-top: 1px solid #f5f3f2;
          }
        }
      }
      & + .mine-cart-shop-item {
        margin-top: 12px;
      }
    }
  }
  .mine-cart-bottom-box {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 8px 12px;
    border-top: 1px solid #dadada;
    margin-bottom: 12px;
    .left {
      display: flex;
      .mess-box {
        margin-left: 24px;
        display: flex;
        flex-direction: column;
        .mess-item {
          display: flex;
          align-items: center;
          font-size: 12px;
          .label {
            min-width: 85px;
            color: #999;
          }
          .text {
            color: var(--primary-text);
          }
          &.all {
            font-size: 14px;
            font-weight: bold;
            .label {
              color: #999;
            }
            .text {
              color: var(--danger);
            }
          }
          & + .mess-item {
            margin-top: 3px;
          }
        }
      }
    }
    .right {
      .btn {
        height: 40px;
        padding: 0 42px;
        line-height: normal;
      }
    }
  }
}
</style>
