import TIM from './tim-js-friendship';
import TIMUploadPlugin from './tim-upload-plugin';

// 初始化 SDK 实例

const tim = TIM.create({
  SDKAppID: 1400779173
});

window.setLogLevel = tim.setLogLevel;

/**
 * 0 普通级别，日志量较多，接入时建议使用
 * 1 release级别，SDK 输出关键信息，生产环境时建议使用
 * 2 告警级别，SDK 只输出告警和错误级别的日志
 * 3 错误级别，SDK 只输出错误级别的日志
 * 4 无日志级别，SDK 将不打印任何日志
 */
tim.setLogLevel(4);

// 注册 cos
tim.registerPlugin({ 'tim-upload-plugin': TIMUploadPlugin });
export default tim;
