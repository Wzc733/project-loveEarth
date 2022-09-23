// request封装了基本请求参数 还有axios
// @ 符号在vue.config.js 中配置， 表示 'src' 路径的别名
import request from '@/utils/request'
export default {
  list() {
    return request({
      //调用该方法return封装好的request对象
      url: '/admin/core/integralGrade/list', //baseURL在.env.development配置好了
      method: 'get',
    })
  },

  removeById(id) {
    return request({
      url: '/admin/core/integralGrade/remove/' + id,
      method: 'delete',
    })
  },

  save(integralGrade) {
    return request({
      url: '/admin/core/integralGrade/save',
      method: 'post',
      data: integralGrade,
    })
  },

  getById(id) {
    return request({
      url: '/admin/core/integralGrade/get/' + id,
      method: 'get',
    })
  },

  updateById(integralGrade) {
    return request({
      url: '/admin/core/integralGrade/update',
      method: 'post',
      data: integralGrade,
    })
  },
}
