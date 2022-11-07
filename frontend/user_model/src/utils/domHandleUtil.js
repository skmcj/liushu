/**
 * 检查传入dom上是否有指定类名
 * @param {HTMLElement} elem Dom元素
 * @param {String} cls 类名
 */
const hasClass = function(elem, cls = '') {
  if (cls.replace(/\s/g, '').length === 0) return false; // 当cls没有参数时，返回false
  return new RegExp(' ' + cls + ' ').test(' ' + elem.className + ' ');
}

/**
 * 为传入dom上添加指定类名
 * @param {HTMLElement} elem Dom元素
 * @param {String} cls 类名
 */
const addClass = function(elem, cls) {
  if (!hasClass(elem, cls)) {
    elem.className = elem.className === '' ? cls : elem.className + ' ' + cls;
  }
}

/**
 * 为传入dom上删除指定类名
 * @param {HTMLElement} elem Dom元素
 * @param {String} cls 类名
 */
const removeClass = function(elem, cls) {
  if (hasClass(elem, cls)) {
    let newClass = ' ' + elem.className.replace(/[\t\r\n]/g, '') + ' ';
    while (newClass.indexOf(' ' + cls + ' ') >= 0) {
      newClass = newClass.replace(' ' + cls + ' ', ' ');
    }
    elem.className = newClass.replace(/^\s+|\s+$/g, '');
  }
}

/**
 * 获取dom元素的指定样式值
 * @param {HTMLElement} obj Dom元素
 * @param {String} name 样式名称
 * @returns
 */
const getStyle = function(obj, name) {
  if(window.getComputedStyle) {
    return getComputedStyle(obj, null)[name];
  }else{
    return obj.currentStyle[name];
  }
}

/**
 * 获取滚动条宽度
 * @returns 滚动条宽度
 */
const getScrollBarWidth = function() {
  let scrollBarWidth;

  const outer = document.createElement('div');
  outer.style.overflow = 'scroll';
  outer.style.height = '100%';
  outer.style.visibility = 'hidden';
  outer.style.width = '100px';
  outer.style.position = 'absolute';
  outer.style.top = '-9999px';
  document.body.appendChild(outer);

  const widthNoScroll = outer.offsetWidth;
  outer.style.overflow = 'scroll';

  const inner = document.createElement('div');
  inner.style.width = '100%';
  outer.appendChild(inner);

  const widthWithScroll = inner.offsetWidth;
  outer.parentNode.removeChild(outer);
  scrollBarWidth = widthNoScroll - widthWithScroll;

  return scrollBarWidth;
}



export default {
  hasClass,
  addClass,
  removeClass,
  getStyle,
  getScrollBarWidth
}
