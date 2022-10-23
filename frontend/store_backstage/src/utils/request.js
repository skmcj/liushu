import Axios from 'axios';

// 创建请求实例并配置根路径
const request = Axios.create({
  // 设置请求根路径
  // 为解决跨域，设置为项目根路径
  baseURL: 'http://localhost:8081'
  // baseURL: process.env.NODE_ENV === 'development' ? './' : '/store/'
});

// http request拦截器 添加一个请求拦截器
request.interceptors.request.use((config) => {
  // 将token添加到请求头
  let token = window.localStorage.getItem('businessToken')
  if (token) {
    config.headers.Authorization = token;
    return config;
    // 这里经常搭配token使用，将token值配置到tokenkey中，将tokenkey放在请求头中
    // config.headers['accessToken'] = Token;
  }
  return config;
}, (error) => {
  // Do something with request error
  return Promise.reject(error);
});


// 导出请求模块
export default request;
