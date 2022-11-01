import request from '@/utils/request';

/**
 * 获取邮箱验证码
 * @param {*} type 发件原因类型
 * @param {*} to 收件邮箱
 * @returns
 */
export const sendValidateCodeApi = function(type, to) {
  return request.get('/mail/code', {
    params: {
      type: type,
      to: to
    }
  });
}

/**
 * 校验验证码
 * @param {*} code
 * @returns
 */
export const checkValidateCodeApi = function(code) {
  return request.get('/mail/checkCode', {
    params: {
      code
    }
  });
}

/**
 * 上传图片
 * @param {*} file
 * @param {*} type
 * @returns
 */
export const uploadImageApi = function(file) {
  return request.post('/img/upload', file);
}

/**
 * 自定义上传函数
 * @param {*} option
 */
export const uploadImagePlusApi = function(option) {
  const formData = new FormData();
  if (option.data) {
    Object.keys(option.data).forEach(key => {
      formData.append(key, option.data[key]);
    });
  }
  formData.append(option.filename, option.file, option.file.name);
  const headers = option.headers || {};
  request.post('/img/upload', formData, { headers })
    .then(res => {
      res.data.data.status = 'success';
      res.data.data.uid = option.file.uid;
      option.onSuccess(res);
    }, err => {
      option.onError(err);
    });
}
