import { emojiMap, emojiUrl } from './emojiMap';
import TIM from '@/packages/tim-sdk/tim-js-friendship';

/** 传入messageBody（群系统消息SystemMessage，群提示消息GroupTip除外）
 * payload = {
 *  msgType: 'TIMTextElem',
 *  msgContent: {
 *    text: 'AAA[龇牙]AAA[龇牙]AAA[龇牙AAA]'
 *  }
 *}
 **/
export const decodeText = function(payload) {
  let renderDom = [];
  // 文本消息
  let temp = payload.text;
  let left = -1;
  let right = -1;
  while (temp !== '') {
    left = temp.indexOf('[');
    right = temp.indexOf(']');
    switch (left) {
      case 0:
        if (right === -1) {
          renderDom.push({
            name: 'text',
            text: temp
          });
          temp = '';
        } else {
          let _emoji = temp.slice(0, right + 1);
          if (emojiMap[_emoji]) {
            renderDom.push({
              name: 'img',
              src: emojiUrl + emojiMap[_emoji]
            });
            temp = temp.substring(right + 1);
          } else {
            renderDom.push({
              name: 'text',
              text: '['
            });
            temp = temp.slice(1);
          }
        }
        break;
      case -1:
        renderDom.push({
          name: 'text',
          text: temp
        });
        temp = '';
        break;
      default:
        renderDom.push({
          name: 'text',
          text: temp.slice(0, left)
        });
        temp = temp.substring(left);
        break;
    }
  }
  return renderDom;
};

/**
 * 返回年月日
 * @export
 * @param {Date} date
 * @param {string} [splitor='-']
 * @returns
 */
export const getDate = function(date, splitor = '-') {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  return `${year}${splitor}${addZeroPrefix(month)}${splitor}${addZeroPrefix(day)}`;
};

/**
 * 返回时分秒/时分
 * @export
 * @param {*} date
 * @param {boolean} [withSecond=false]
 * @returns
 */
export const getTime = function(date, withSecond = false) {
  const hour = date.getHours();
  const minute = date.getMinutes();
  const second = date.getSeconds();
  return withSecond
    ? `${addZeroPrefix(hour)}:${addZeroPrefix(minute)}:${addZeroPrefix(second)}`
    : `${hour}:${addZeroPrefix(minute)}`;
};

export const getFullDate = function(date) {
  return `${getDate(date)} ${getTime(date)}`;
};

export const isToday = function(date) {
  return date.toDateString() === new Date().toDateString();
};

/**
 * 个位数，加0前缀
 * @param {*} number
 * @returns
 */
function addZeroPrefix(number) {
  return number < 10 ? `0${number}` : number;
}

export const formatTime = function(secondTime) {
  let time = secondTime;
  let newTime, hour, minite, seconds;
  if (time >= 3600) {
    hour = parseInt(time / 3600) < 10 ? '0' + parseInt(time / 3600) : parseInt(time / 3600);
    minite = parseInt((time % 60) / 60) < 10 ? '0' + parseInt((time % 60) / 60) : parseInt((time % 60) / 60);
    seconds = time % 3600 < 10 ? '0' + (time % 3600) : time % 3600;
    if (seconds > 60) {
      minite = parseInt(seconds / 60) < 10 ? '0' + parseInt(seconds / 60) : parseInt(seconds / 60);
      seconds = seconds % 60 < 10 ? '0' + (seconds % 60) : seconds % 60;
    }
    newTime = hour + ':' + minite + ':' + seconds;
  } else if (time >= 60 && time < 3600) {
    minite = parseInt(time / 60) < 10 ? '0' + parseInt(time / 60) : parseInt(time / 60);
    seconds = time % 60 < 10 ? '0' + (time % 60) : time % 60;
    newTime = '00:' + minite + ':' + seconds;
  } else if (time < 60) {
    seconds = time < 10 ? '0' + time : time;
    newTime = '00:00:' + seconds;
  }
  return newTime;
};

/**
 * 利用 document.title 做新消息提示
 * @export
 * @param {Number} count
 */
export const titleNotify = function(count) {
  const hasNewMessage = count > 0;
  if (hasNewMessage) {
    if (document.title.search(/\((.*?)\)/) >= 0) {
      document.title = document.title.replace(/\((.*?)\)/, `(${count > 99 ? '99+' : count})`);
    } else {
      document.title = `(${count})${document.title}`;
    }
  } else {
    document.title = document.title.replace(/\((.*?)\)/, '');
  }
}

export const errorMap = {
  500: '服务器错误',
  602: '用户名或密码不合法',
  610: '用户名格式错误',
  612: '用户已存在',
  620: '用户不存在',
  621: '密码错误'
}

export const filterCallingMessage = function(currentMessageList) {
  currentMessageList.forEach(item => {
    if (item.callType) {
      // 对于自己伪造的消息不需要解析
      return;
    }
    if (item.type === TIM.TYPES.MSG_MERGER && item.payload.downloadKey !== '') {
      let promise = window.tim.downloadMergerMessage(item);
      promise
        .then(function(imResponse) {
          // 下载成功后，SDK会更新 message.payload.messageList 等信息
          item = imResponse;
        })
        .catch(function(imError) {
          // 下载失败
          console.warn('downloadMergerMessage error:', imError);
        });
    }
    if (item.type === TIM.TYPES.MSG_CUSTOM) {
      let payloadData = {};
      try {
        payloadData = JSON.parse(item.payload.data);
      } catch (e) {
        payloadData = {};
      }
      if (payloadData.businessID === 1) {
        if (item.conversationType === TIM.TYPES.CONV_GROUP) {
          if (payloadData.actionType === 5) {
            item.nick = payloadData.inviteeList ? payloadData.inviteeList.join(',') : item.from;
          }
          let _text = window.trtcCalling.extractCallingInfoFromMessage(item);
          let groupText = `${_text}`;
          item.type = TIM.TYPES.MSG_GRP_TIP;
          let customData = {
            operationType: 256,
            text: groupText,
            userIDList: []
          };
          item.payload = customData; // JSON.stringify(customData)
        }
        if (item.conversationType === TIM.TYPES.CONV_C2C) {
          let c2cText = window.trtcCalling.extractCallingInfoFromMessage(item);
          let customData = {
            text: c2cText
          };
          item.payload = customData; // JSON.stringify(customData)
        }
      }
    }
  });
}

/**
 * 随机颜色池
 */
const colors = [
  // 黄白游
  '#FFF799',
  // 松花
  '#FFEE6F',
  // 缃叶
  '#ECD452',
  // 苍黄
  '#B6A014',
  // 天缥
  '#D5EBE1',
  // 沧浪
  '#B1D5C8',
  // 苍筤
  '#99BCAC',
  // 缥碧
  '#80A492',
  // 流黄
  '#8B7042',
  // 栗壳
  '#775039',
  // 龙战
  '#5F4321',
  // 青骊细
  '#422517',
  // 海天霞
  '#F3A694',
  // 缙云
  '#EE7959',
  // 纁黄
  '#BA5140',
  // 珊删瑚赫
  '#C12C1F',
  // 苏梅
  '#DD7694',
  // 紫茎屏风
  '#A76283',
  // 老僧衣
  '#A46244',
  // 玄天
  '#6B5458',
  // 缊拔
  '#984F31',
  // 紫瓯
  '#7C461E',
  // 碧山
  '#779649',
  // 青青
  '#4F6F46',
  // 綪茷
  '#9E2A22',
  // 顺圣
  '#7C191E',
  // 长春
  '#DC6B82',
  // 牙绯
  '#C35C5D',
  // 黄不老
  '#DB9B34',
  // 柘黄
  '#C67915',
  // 青黛
  '#45465E',
  // 绀蝶
  '#2C2F3B',
  // 茈藐
  '#A67EB7',
  // 紫魅
  '#7D5284',
  // 翠微
  '#4C8045',
  // 翠虬
  '#446A37',
  // 青雀头黛
  '#354E6B',
  // 螺子黛
  '#13393E',
  // 緅絺
  '#804C2E',
  // 目童子
  '#5B3222'
];

/**
 * 根据传入文字生成图片
 * @param {String} text 文字
 * @param {Array} size 大小 - [w, h]
 * @returns {String} 生成图片的base64编码字符串
 */
export const genAvatarByText = function(text, size, bgColor = undefined) {
  text = text.charAt(0);
  let cvs = document.createElement('canvas');
  cvs.setAttribute('width', size[0]);
  cvs.setAttribute('height', size[1]);
  let ctx = cvs.getContext('2d');
  ctx.fillStyle = bgColor ? bgColor : colors[Math.floor(Math.random() * colors.length)];
  ctx.fillRect(0, 0, size[0], size[1]);
  ctx.fillStyle = 'rgb(255,255,255)';
  ctx.font = size[0] * 0.6 + 'px 幼圆';
  ctx.textBaseline = 'middle';
  ctx.textAlign = 'center';
  ctx.fillText(text, size[0] / 2, size[1] / 2);
  let imgBase64 = cvs.toDataURL('image/jpeg', 1);
  cvs.remove();
  return imgBase64;
}

/**
 * 根据字符获取颜色库里的颜色
 * @param {String} text
 */
export const getColorByText = function(text, inc = 0) {
  let colorIndex = (text.charCodeAt() + inc) % colors.length;
  return colors[colorIndex];
}
