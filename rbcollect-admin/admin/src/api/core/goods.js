// request封装了基本请求参数 还有axios
// @ 符号在vue.config.js 中配置， 表示 'src' 路径的别名
import request from "@/utils/request";
export default {
  list() {
    return request({
      //调用该方法return封装好的request对象
      url: "/admin/core/goods/list", //baseURL在.env.development配置好了
      method: "get",
    });
  },
  onUploadRemove(url) {
    return request({
      url: "/api/oss/file/remove?url=" + url,
      method: "delete",
    });
  },
  save(GoodsQuery) {
    return request({
      url: "/admin/core/goods/save",
      method: "post",
      data: GoodsQuery,
    });
  },
  getById(id) {
    return request({
      url: "/admin/core/goods/get/" + id,
      method: "get",
    });
  },
  updateById(GoodsQuery) {
    return request({
      url: "/admin/core/goods/update",
      method: "post",
      data: GoodsQuery,
    });
  },
  removeById(id) {
    return request({
      url: "/admin/core/goods/remove/" + id,
      method: "delete",
    });
  },
};
