import Axios from 'axios';
import Vue from '@/main';
import { Message } from 'element-ui';

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
  let info = JSON.parse(window.localStorage.getItem('employeeInfo'));
  if (info) {
    config.headers.Authorization = info.token;
    return config;
    // 这里经常搭配token使用，将token值配置到tokenkey中，将tokenkey放在请求头中
    // config.headers['accessToken'] = Token;
  }
  return config;
}, (error) => {
  // Do something with request error
  return Promise.reject(error);
});

// http response 拦截器
request.interceptors.response.use(
  res => {
    if(res.data) {
      if(!res.data.flag) {
        switch(res.data.code) {
          case 52901:
            // token过期
            Message({
              showClose: true,
              message: 'Token已过期，请重新进行登录',
              type: 'error',
              onClose: () => {
                window.localStorage.removeItem('employeeInfo');
                Vue.$router.replace('/login');
              },
              duration: 1500
            });
        }
      }
    }
    return res;
  },
  err => {
    // if (error.response) {
    //   switch (error.response.status) {
    //     case 401:
    //       // 返回 401 清除token信息并跳转到登录页面
    //       router.replace()
    //   }
    // }
    return Promise.reject(err.response.data)   // 返回接口返回的错误信息
  });


// 导出请求模块
export default request;
