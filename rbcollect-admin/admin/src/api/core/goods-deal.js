import request from "@/utils/request";

export default {
  getPageList(page, limit, searchObj) {
    return request({
      url: `/admin/core/goodsDealItem/list/${page}/${limit}`,
      method: "get",
      params: searchObj, //如果是post传参,要用data关键字
    });
  },
  sendGoods(id) {
    return request({
      url: `/admin/core/goodsDealItem/sendGoods/${id}`,
      method: "get",
    });
  },
  removeById(id) {
    return request({
      url: "/admin/core/goodsDealItem/remove/" + id,
      method: "delete",
    });
  },
  DondGoods(id) {
    return request({
      url: `/admin/core/goodsDealItem/doneGoods/${id}`,
      method: "get",
    });
  },
};
