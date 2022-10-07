import Axios from 'axios';

// 创建请求实例并配置根路径
const request = Axios.create({
  // 设置请求根路径
  // baseURL: 'http://localhost:8080'
});

// 导出请求模块
export default request;
