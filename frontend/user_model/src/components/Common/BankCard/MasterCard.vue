<template>
  <div
    class="card"
    data-dis-type="simultaneous"
    data-dis-particle-type="ExplodingParticle"
    data-dis-reduction-factor="111"
    :class="{
      'primary-card': size === 'primary',
      'small-card': size === 'small',
      'mini-card': size === 'mini',
      'is-click': click
    }"
    @click.stop="onChecked">
    <div class="flip">
      <div class="front">
        <div
          class="strip-bottom"
          :style="{
            'background-image': 'linear-gradient(' + newHex[0] + ', ' + newHex[1] + ')'
          }"></div>
        <div
          class="strip-top"
          :style="{
            'background-image': 'linear-gradient(' + newHex[0] + ', ' + newHex[1] + ')'
          }"></div>
        <svg
          t="1676129871752"
          class="logo"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="11502"
          width="200"
          height="200">
          <path
            d="M953.9584 385.1776l-139.5712-206.4384a32.5632 32.5632 0 0 0-26.9824-14.336H236.6976a32.512 32.512 0 0 0-26.7776 14.336L70.0416 385.1776a32.5632 32.5632 0 0 0 2.304 39.7824l415.0272 481.28a32.5632 32.5632 0 0 0 49.3568 0l414.72-481.6384a32.5632 32.5632 0 0 0 2.5088-39.424z m-31.2832 17.2032a21.6064 21.6064 0 0 1-26.112 15.9232l-187.4432-45.5168-176.64 468.8896a21.6576 21.6576 0 0 1-40.448 0L310.8864 372.8384l-183.3472 45.312a20.48 20.48 0 0 1-5.12 0.6656 21.6064 21.6064 0 0 1-20.992-16.4352 21.7088 21.7088 0 0 1 15.8208-26.2144l174.08-43.008L231.1168 230.4a21.6064 21.6064 0 0 1 7.7824-29.5936 21.504 21.504 0 0 1 10.8544-2.9696 21.76 21.76 0 0 1 18.7392 10.752L336.64 325.632h346.88l71.68-117.76a21.6064 21.6064 0 1 1 36.9152 22.528l-62.5664 102.8096 177.2032 43.0592a21.3504 21.3504 0 0 1 13.3632 9.728 21.6576 21.6576 0 0 1 2.56 16.384z"
            fill="#fff"
            p-id="11503"></path>
          <path d="M511.7952 773.3248l152.576-404.4288H355.7376l156.0576 404.4288z" fill="#fff" p-id="11504"></path>
        </svg>
        <div class="investor">Investor</div>
        <div class="chip">
          <div class="chip-line"></div>
          <div class="chip-line"></div>
          <div class="chip-line"></div>
          <div class="chip-line"></div>
          <div class="chip-main"></div>
        </div>
        <svg class="wave" viewBox="0 3.71 26.959 38.787" width="26.959" height="38.787" fill="white">
          <path
            d="M19.709 3.719c.266.043.5.187.656.406 4.125 5.207 6.594 11.781 6.594 18.938 0 7.156-2.469 13.73-6.594 18.937-.195.336-.57.531-.957.492a.9946.9946 0 0 1-.851-.66c-.129-.367-.035-.777.246-1.051 3.855-4.867 6.156-11.023 6.156-17.718 0-6.696-2.301-12.852-6.156-17.719-.262-.317-.301-.762-.102-1.121.204-.36.602-.559 1.008-.504z"></path>
          <path
            d="M13.74 7.563c.231.039.442.164.594.343 3.508 4.059 5.625 9.371 5.625 15.157 0 5.785-2.113 11.097-5.625 15.156-.363.422-1 .472-1.422.109-.422-.363-.472-1-.109-1.422 3.211-3.711 5.156-8.551 5.156-13.843 0-5.293-1.949-10.133-5.156-13.844-.27-.309-.324-.75-.141-1.114.188-.367.578-.582.985-.542h.093z"></path>
          <path
            d="M7.584 11.438c.227.031.438.144.594.312 2.953 2.863 4.781 6.875 4.781 11.313 0 4.433-1.828 8.449-4.781 11.312-.398.387-1.035.383-1.422-.016-.387-.398-.383-1.035.016-1.421 2.582-2.504 4.187-5.993 4.187-9.875 0-3.883-1.605-7.372-4.187-9.875-.321-.282-.426-.739-.266-1.133.164-.395.559-.641.984-.617h.094zM1.178 15.531c.121.02.238.063.344.125 2.633 1.414 4.437 4.215 4.437 7.407 0 3.195-1.797 5.996-4.437 7.406-.492.258-1.102.07-1.36-.422-.257-.492-.07-1.102.422-1.359 2.012-1.075 3.375-3.176 3.375-5.625 0-2.446-1.371-4.551-3.375-5.625-.441-.204-.676-.692-.551-1.165.122-.468.567-.785 1.051-.742h.094z"></path>
        </svg>
        <div class="card-number">
          <div class="section" v-for="(item, index) of cardNumberList" :key="'card-number-' + index">{{ item }}</div>
        </div>
        <div class="end">
          <span class="end-text">exp.end:</span><span class="end-date"> {{ endMonth + '/' + endYear }}</span>
        </div>
        <div class="card-holder">{{ 'mr ' + owner }}</div>
        <div class="master">
          <div class="circle master-red"></div>
          <div class="circle master-yellow"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
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
  computed: {
    cardNumberList() {
      return this.cardNumber.replace(/\d{4}/g, '$& ').trim().split(' ');
    },
    newHex() {
      // const rDeg = Math.random() * 360;
      const rDeg = Number(this.cardNumber) % 361;
      if (this.hexRDeg !== undefined) {
        return [this.changeHue(this.firstOHex, this.hexRDeg), this.changeHue(this.secondOHex, this.hexRDeg)];
      }
      return [this.changeHue(this.firstOHex, rDeg), this.changeHue(this.secondOHex, rDeg)];
    }
  },
  methods: {
    /**
     * 选择
     */
    onChecked() {
      if (!this.click) return;
      let card = {};
      card.cardNumber = this.cardNumber;
      card.investor = this.investor;
      card.owner = this.owner;
      card.endMonth = this.endMonth;
      card.endYear = this.endYear;
      this.$emit('onSelect', card);
    },
    /**
     * 旋转色调
     * @param {*} rgb 源色
     * @param {*} degree 色调旋转值
     * @returns
     */
    changeHue(rgb, degree) {
      let hsl = this.rgbToHSL(rgb);
      hsl.h += degree;
      if (hsl.h > 360) {
        hsl.h -= 360;
      } else if (hsl.h < 0) {
        hsl.h += 360;
      }
      return this.hslToRGB(hsl);
    },

    // exepcts a string and returns an object
    rgbToHSL(rgb) {
      // strip the leading # if it's there
      rgb = rgb.replace(/^\s*#|\s*$/g, '');

      // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
      if (rgb.length === 3) {
        rgb = rgb.replace(/(.)/g, '$1$1');
      }

      let r = parseInt(rgb.substr(0, 2), 16) / 255;
      let g = parseInt(rgb.substr(2, 2), 16) / 255;
      let b = parseInt(rgb.substr(4, 2), 16) / 255;
      let cMax = Math.max(r, g, b);
      let cMin = Math.min(r, g, b);
      let delta = cMax - cMin;
      let l = (cMax + cMin) / 2;
      let h = 0;
      let s = 0;

      if (delta === 0) {
        h = 0;
      } else if (cMax === r) {
        h = 60 * (((g - b) / delta) % 6);
      } else if (cMax === g) {
        h = 60 * ((b - r) / delta + 2);
      } else {
        h = 60 * ((r - g) / delta + 4);
      }

      if (delta === 0) {
        s = 0;
      } else {
        s = delta / (1 - Math.abs(2 * l - 1));
      }

      return {
        h: h,
        s: s,
        l: l
      };
    },

    // expects an object and returns a string
    hslToRGB(hsl) {
      let h = hsl.h;
      let s = hsl.s;
      let l = hsl.l;
      let c = (1 - Math.abs(2 * l - 1)) * s;
      let x = c * (1 - Math.abs(((h / 60) % 2) - 1));
      let m = l - c / 2;
      let r;
      let g;
      let b;

      if (h < 60) {
        r = c;
        g = x;
        b = 0;
      } else if (h < 120) {
        r = x;
        g = c;
        b = 0;
      } else if (h < 180) {
        r = 0;
        g = c;
        b = x;
      } else if (h < 240) {
        r = 0;
        g = x;
        b = c;
      } else if (h < 300) {
        r = x;
        g = 0;
        b = c;
      } else {
        r = c;
        g = 0;
        b = x;
      }

      r = this.normalizeRgbValue(r, m);
      g = this.normalizeRgbValue(g, m);
      b = this.normalizeRgbValue(b, m);

      return this.rgbToHex(r, g, b);
    },

    normalizeRgbValue(color, m) {
      color = Math.floor((color + m) * 255);
      if (color < 0) {
        color = 0;
      }
      return color;
    },

    rgbToHex(r, g, b) {
      return '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
    }
  }
};
</script>

<style lang="less" scoped>
@var1: #808080;
@var2: #ffffff;
@var3: #333333;
@var4: #eb001b;

*,
*:before,
*:after {
  box-sizing: border-box;
}
.card {
  // margin: auto;
  width: 400px;
  height: 250px;
  text-align: left;
  &-number {
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 50px 25px 15px;
    font-size: 23px;
    font-family: 'cc font', monospace;
  }
  &-holder {
    margin: 10px 25px;
    text-transform: uppercase;
    font-family: 'cc font', monospace;
  }
  &.is-click:active {
    .front {
      box-shadow: 0 1px 5px 1px rgba(0, 0, 0, 0.2);
    }
  }
}
.small-card {
  transform: scale(0.75);
  -webkit-transform: scale(0.75);
  -moz-transform: scale(0.75);
  -ms-transform: scale(0.75);
  -o-transform: scale(0.75);
}
.mini-card {
  transform: scale(0.5);
  -webkit-transform: scale(0.5);
  -moz-transform: scale(0.5);
  -ms-transform: scale(0.5);
  -o-transform: scale(0.5);
}
.is-click {
  cursor: pointer;
}
.flip {
  width: inherit;
  height: inherit;
}
.front {
  position: absolute;
  width: inherit;
  height: inherit;
  border-radius: 15px;
  color: @var2;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.3);
  box-shadow: 0 1px 10px 1px rgba(0, 0, 0, 0.3);
  backface-visibility: hidden;
  background-image: linear-gradient(to right, #111, #555);
  overflow: hidden;
}
.front {
  transform: translateZ(0);
}
.strip-bottom,
.strip-top {
  position: absolute;
  right: 0;
  height: inherit;
  background-image: linear-gradient(to bottom, #ff6767, #ff4545);
  box-shadow: 0 0 10px 0px rgba(0, 0, 0, 0.5);
}
.strip-bottom {
  width: 200px;
  transform: skewX(-15deg) translateX(50px);
}
.strip-top {
  width: 180px;
  transform: skewX(20deg) translateX(50px);
}
.logo {
  width: 40px;
  height: 40px;
  position: absolute;
  top: 30px;
  right: 25px;
}
.investor {
  position: relative;
  top: 30px;
  left: 25px;
  text-transform: uppercase;
}
.chip {
  position: relative;
  top: 60px;
  left: 25px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 40px;
  border-radius: 5px;
  background-image: linear-gradient(to bottom left, #ffecc7, #d0b978);
  overflow: hidden;
  .chip-line {
    position: absolute;
    width: 100%;
    height: 1px;
    background-color: @var3;
    &:nth-child(1) {
      top: 13px;
    }
    &:nth-child(2) {
      top: 20px;
    }
    &:nth-child(3) {
      top: 28px;
    }
    &:nth-child(4) {
      left: 25px;
      width: 1px;
      height: 50px;
    }
  }
  .chip-main {
    width: 20px;
    height: 25px;
    border: 1px solid @var3;
    border-radius: 3px;
    background-image: linear-gradient(to bottom left, #efdbab, #e1cb94);
    z-index: 1;
  }
}
.wave {
  position: relative;
  top: 20px;
  left: 100px;
}
.end {
  margin-left: 25px;
  text-transform: uppercase;
  font-family: 'cc font', monospace;
  .end-text {
    font-size: 9px;
    color: rgba(255, 255, 255, 0.8);
  }
}
.master {
  position: absolute;
  right: 20px;
  bottom: 20px;
  display: flex;
  .circle {
    width: 25px;
    height: 25px;
    border-radius: 50%;
  }
  .master-red {
    background-color: @var4;
  }
  .master-yellow {
    margin-left: -10px;
    background-color: rgba(255, 209, 0, 0.6);
  }
}
button {
  background: none;
  color: @var2;
  border: none;
  outline: none;
  cursor: pointer;
}
</style>
