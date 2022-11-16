<template>
  <div class="mine mine-address">
    <div class="title-box">
      <span class="icon ic-address-books"></span>
      <span class="text">我的地址</span>
      <div class="hr"></div>
    </div>
    <div class="btns">
      <div class="btn" @click="dialogVisible = true">
        <i class="ic-add"></i>
        <span>新增收货地址</span>
      </div>
    </div>
    <div class="mine-address-list">
      <div
        class="mess-address-item"
        v-for="(item, index) in address"
        :key="'address-item-' + index"
        :class="{ 'is-default': item.isDefault === 1 }">
        <div class="address-content-box">
          <div class="title">
            <span class="tit">学习</span>
            <span class="default-label" v-if="item.isDefault === 1">默认地址</span>
          </div>
          <div class="mess-box">
            <div class="mess-item">
              <span class="label">联系人：</span>
              <span class="text">{{ item.consignee + ' ' + (item.sex ? '先生' : '女士') }}</span>
            </div>
            <div class="mess-item">
              <span class="label">联系电话：</span>
              <span class="text">{{ item.areaCode + ' ' + item.phone }}</span>
            </div>
            <div class="mess-item">
              <span class="label">收货地址：</span>
              <span class="text">{{ item.location }}</span>
            </div>
            <div class="mess-item">
              <span class="label">详细地址：</span>
              <span class="text">{{ item.detail }}</span>
            </div>
          </div>
        </div>
        <div class="tool-box">
          <div class="t-btn delete" @click.stop="handleDetate(item.id)">
            <i class="ic-delete"></i>
          </div>
          <div class="t-btns">
            <div class="t-btn" @click.stop="handleEdit(item.id)">
              <i class="ic-edit"></i>
            </div>
            <div class="t-btn" v-if="item.isDefault !== 1" @click.stop="handleDefault(item.id)">
              <i class="ic-default-1"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      class="address-dialog"
      :modal-append-to-body="false"
      center>
      <el-form
        :model="addressForm"
        :rules="addressRule"
        ref="addressForm"
        label-width="120px"
        class="address-form"
        hide-required-asterisk>
        <el-form-item label="联系人" prop="consignee">
          <el-input
            v-model="addressForm.consignee"
            style="width: auto; flex-grow: 1"
            placeholder="请输入联系人姓名"></el-input>
          <div class="sex-box">
            <div class="sex-item" :class="{ 'is-checked': addressForm.sex === 0 }" @click.stop="addressForm.sex = 0">
              先生
            </div>
            <div class="sex-item" :class="{ 'is-checked': addressForm.sex === 1 }" @click.stop="addressForm.sex = 1">
              女士
            </div>
          </div>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-select
            v-model="addressForm.areaCode"
            placeholder="区号"
            style="min-width: 85px; max-width: 92px"
            popper-class="area-poper"
            :popper-append-to-body="false">
            <el-option
              v-for="(area, i) in areaCodeList"
              :key="'area-code-' + i"
              :label="area.value"
              :value="area.value">
              <span style="float: left">{{ area.value }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ area.label }}</span>
            </el-option>
          </el-select>
          <el-input
            v-model="addressForm.phone"
            style="margin-left: 12px; flex-grow: 1"
            placeholder="请输入联系方式"></el-input>
        </el-form-item>
        <el-form-item label="收货地址" prop="location">
          <el-input v-model="addressForm.location" placeholder="请输入收货地址"></el-input>
        </el-form-item>
        <el-form-item label="详细地址" prop="detail">
          <el-input type="textarea" v-model="addressForm.detail" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="label">
          <el-input
            v-model="addressForm.label"
            style="width: auto; flex-grow: 1"
            placeholder="请输入地址标签"></el-input>
          <div class="label-box">
            <div
              class="label"
              :class="{ 'is-checked': addressForm.label === '家' }"
              @click.stop="addressForm.label = '家'">
              家
            </div>
            <div
              class="label"
              :class="{ 'is-checked': addressForm.label === '学校' }"
              @click.stop="addressForm.label = '学校'">
              学校
            </div>
            <div
              class="label"
              :class="{ 'is-checked': addressForm.label === '公司' }"
              @click.stop="addressForm.label = '公司'">
              公司
            </div>
          </div>
        </el-form-item>
        <el-form-item label="设为默认地址" prop="isDefault" class="middle-right">
          <el-switch
            v-model="addressForm.isDefault"
            :active-value="1"
            :inactive-value="0"
            active-color="#83ccd2"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="address-footer">
        <el-button type="primary" @click="handleSave">保存地址</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      address: [],
      areaCodeList: [
        { label: '中国大陆', value: '+86' },
        { label: '中国香港', value: '+852' },
        { label: '中国澳门', value: '+853' },
        { label: '中国台湾', value: '+866' }
      ],
      addressForm: {
        id: '',
        consignee: '',
        sex: 0,
        areaCode: '+86',
        phone: '',
        location: '',
        detail: '',
        label: '',
        isDefault: 0
      },
      addressRule: {
        consignee: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        location: [{ required: true, message: '请输入收货地址', trigger: 'blur' }],
        label: [{ required: true, message: '请输入地址标签', trigger: 'blur' }]
      },
      dialogTitle: '新增地址',
      dialogType: 'add',
      dialogVisible: false
    };
  },
  created() {
    this.address = this.getAddressTest(5);
  },
  methods: {
    getAddressTest(n) {
      let list = [];
      for (let i = 0; i < n; i++) {
        let address = {
          id: i,
          consignee: '张三',
          sex: i % 2,
          areaCode: '+86',
          phone: '13489568956',
          location: '广东省韶关市韶关大学',
          detail: '西区曼陀罗苑',
          label: '学校',
          isDefault: 0
        };
        list.push(address);
      }
      list[0].isDefault = 1;
      return list;
    },
    /**
     * 保存地址
     */
    handleSave() {
      this.dialogVisible = false;
      console.log('保存地址');
      this.$refs.addressForm.validate(valid => {
        if (valid) {
          // 验证通过
          this.dialogVisible = false;
          console.log('address form =>', this.dialogType, this.addressForm);
        }
      });
    },
    /**
     * 删除地址
     */
    handleDetate(id) {
      this.$confirm('是否确认删除该地址, 此操作不可恢复?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'danger'
      })
        .then(() => {
          // 删除操作
        })
        .catch(() => {
          // 取消删除
        });
    },
    /**
     * 编辑地址
     */
    handleEdit(id) {
      this.dialogTitle = '编辑地址';
      this.dialogType = 'edit';
      this.dialogVisible = true;
      this.addressForm = this.address[id];
    },
    /**
     * 设置默认地址
     */
    handleDefault(id) {
      this.$confirm('是否确认将该地址设为默认地址?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'info'
      })
        .then(() => {
          // 删除操作
        })
        .catch(() => {
          // 取消删除
        });
    }
  }
};
</script>

<style lang="less" scoped>
.btns {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  box-sizing: border-box;
  padding: 24px 12px;
  .btn {
    cursor: pointer;
    user-select: none;
    height: 36px;
    line-height: 36px;
    box-sizing: border-box;
    padding: 0 21px;
    font-size: 16px;
    color: var(--white);
    border-radius: 8px;
    background-color: var(--primary-btn);
    transition: background-color 0.5s ease-in-out;
    display: flex;
    align-items: center;
    i {
      font-size: 16px;
      color: var(--white);
      & + span {
        margin-left: 5px;
      }
    }
    &:hover {
      background-color: var(--primary-btn-h);
    }
    &:active {
      background-color: var(--primary-btn-a);
    }
  }
}
.mine-address-list {
  display: flex;
  align-items: center;
  flex-direction: column;
  box-sizing: border-box;
  padding-bottom: 120px;
  .mess-address-item {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 8px 12px;
    border-radius: 12px;
    border: 2px solid #f4f4f4;
    transition: all 0.5s ease-in-out;
    .address-content-box {
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding-right: 24px;
      .title {
        width: 100%;
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        .tit {
          user-select: none;
          font-size: 16px;
          font-weight: bold;
          color: var(--primary-text);
          & + .default-label {
            margin-left: 24px;
          }
        }
        .default-label {
          user-select: none;
          height: 22px;
          padding: 0 6px;
          font-size: 10px;
          color: var(--primary);
          box-sizing: border-box;
          border-radius: 3px;
          border: 1px solid var(--primary);
        }
      }
      .mess-box {
        display: flex;
        flex-direction: column;
        margin-left: 12px;
        .mess-item {
          display: flex;
          align-items: center;
          font-size: 14px;
          .label {
            user-select: none;
            width: 85px;
            text-align: right;
            color: var(--placeholder-text);
          }
          .text {
            color: var(--regular-text);
          }
          & + .mess-item {
            margin-top: 8px;
          }
        }
      }
    }
    .tool-box {
      height: 100%;
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
      flex-direction: column;
      margin-right: 12px;
      .t-btns {
        display: flex;
        align-items: center;
        flex-direction: row-reverse;
        justify-content: flex-end;
      }
      .t-btn {
        cursor: pointer;
        transition: color 0.5s ease-in-out;
        i {
          transition: color 0.5s ease-in-out;
          font-size: 18px;
          color: #666666;
        }
        & + .t-btn {
          margin-right: 16px;
        }
        &:hover {
          i {
            color: var(--primary-btn);
          }
        }
        &.delete {
          margin-top: 4px;
          &:hover {
            i {
              color: var(--danger);
            }
          }
        }
      }
    }
    & + .mess-address-item {
      margin-top: 12px;
    }
    &.is-default {
      background-color: #f2fafa;
      border-color: var(--primary);
    }
  }
}
.address-dialog {
  :deep(.el-dialog) {
    width: 45%;
    &.el-dialog--center {
      .el-dialog__header {
        text-align: left;
      }
    }
    .el-dialog__body {
      padding: 25px 15% 30px;
      .el-form {
        .el-form-item {
          .el-form-item__content {
            display: flex;
            align-items: center;
            .sex-box {
              margin-left: 18px;
              height: 24px;
              display: flex;
              align-items: center;
              .sex-item {
                cursor: pointer;
                user-select: none;
                height: 24px;
                line-height: 24px;
                box-sizing: border-box;
                padding: 0 8px;
                border-radius: 3px;
                color: #dcdfe6;
                border: 1px solid #dcdfe6;
                font-size: 12px;
                transition: color 0.5s ease-in-out, border-color 0.5s ease-in-out;
                & + .sex-item {
                  margin-left: 12px;
                }
                &.is-checked {
                  border-color: var(--primary);
                  color: var(--primary);
                }
              }
            }
            .el-select {
              .el-select-dropdown {
                &.area-poper {
                  width: 160px;
                }
              }
            }
            .label-box {
              min-width: 144px;
              margin-left: 18px;
              height: 24px;
              display: flex;
              align-items: center;
              .label {
                cursor: pointer;
                user-select: none;
                height: 24px;
                line-height: 24px;
                box-sizing: border-box;
                padding: 0 8px;
                border-radius: 3px;
                color: #dcdfe6;
                border: 1px solid #dcdfe6;
                font-size: 12px;
                transition: color 0.5s ease-in-out, border-color 0.5s ease-in-out;
                & + .label {
                  margin-left: 12px;
                }
                &.is-checked {
                  border-color: var(--primary);
                  color: var(--primary);
                }
              }
            }
          }
          &.middle-right {
            .el-form-item__content {
              height: 40px;
              justify-content: flex-end;
            }
          }
        }
      }
    }
  }
}
</style>
