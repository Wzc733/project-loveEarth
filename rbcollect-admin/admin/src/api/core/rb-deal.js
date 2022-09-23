import request from "@/utils/request";

export default {
  getPageList(page, limit, searchObj) {
    return request({
      url: `/admin/core/rbDealItem/list/${page}/${limit}`,
      method: "get",
      params: searchObj, //如果是post传参,要用data关键字
    });
  },
  sendRb(id) {
    return request({
      url: `/admin/core/rbDealItem/sendRb/${id}`,
      method: "get",
    });
  },
  DondRb(id) {
    return request({
      url: `/admin/core/rbDealItem/DoneRb/${id}`,
      method: "get",
    });
  },
  removeById(id) {
    return request({
      url: "/admin/core/rbDealItem/remove/" + id,
      method: "delete",
    });
  },
};
