// request封装了基本请求参数 还有axios
// @ 符号在vue.config.js 中配置， 表示 'src' 路径的别名
import request from "@/utils/request";
export default {
  getPageList(page, limit) {
    return request({
      url: `/admin/core/news/list/${page}/${limit}`,
      method: "get",
      //如果是post传参,要用data关键字
    });
  },

  removeById(id) {
    return request({
      url: "/admin/core/news/remove/" + id,
      method: "delete",
    });
  },

  save(news) {
    return request({
      url: "/admin/core/news/save",
      method: "post",
      data: news,
    });
  },

  getById(id) {
    return request({
      url: "/admin/core/news/get/" + id,
      method: "get",
    });
  },

  updateById(news) {
    return request({
      url: "/admin/core/news/update",
      method: "post",
      data: news,
    });
  },
  onUploadRemove(url) {
    return request({
      url: "/api/oss/file/remove?url=" + url,
      method: "delete",
    });
  },
};
