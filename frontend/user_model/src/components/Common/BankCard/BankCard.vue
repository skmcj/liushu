<template>
  <div class="card-container">
    <template v-if="organisation === 'master'">
      <MasterCard
        :card-number="cardNumber"
        :end-year="endYear"
        :end-month="endMonth"
        :investor="investor"
        :owner="owner"
        :first-o-hex="firstOHex"
        :second-o-hex="secondOHex"
        :hex-r-deg="hexRDeg"
        :size="size"
        :click="click"
        @onSelect="onSelect" />
    </template>
  </div>
</template>

<script>
import MasterCard from '@/components/Common/BankCard/MasterCard';

export default {
  components: {
    MasterCard
  },
  props: {
    organisation: {
      type: String,
      default: 'master'
    },
    cardNumber: {
      // 16位数字字符
      type: String,
      default: '0000000000000000'
    },
    endYear: {
      type: [String, Number],
      default: '00'
    },
    endMonth: {
      type: [String, Number],
      default: '00'
    },
    investor: {
      type: String,
      default: 'Investor'
    },
    owner: {
      type: String,
      default: 'owner'
    },
    firstOHex: {
      type: String,
      default: '#ff6767'
    },
    secondOHex: {
      type: String,
      default: '#ff4545'
    },
    hexRDeg: {
      type: Number,
      default: undefined
    },
    size: {
      type: String,
      // primary | small | mini
      default: 'primary'
    },
    click: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    onSelect(val) {
      this.$emit('onSelect', val);
    }
  }
};
</script>

<style lang="less" scoped></style>
