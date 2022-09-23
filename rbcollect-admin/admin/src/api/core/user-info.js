import request from "@/utils/request";

export default {
  getPageList(page, limit, searchObj) {
    return request({
      url: `/admin/core/userInfo/list/${page}/${limit}`,
      method: "get",
      params: searchObj, //如果是post传参,要用data关键字
    });
  },
  lock(id, status) {
    return request({
      url: `/admin/core/userInfo/lock/${id}/${status}`,
      method: "get",
    });
  },
  getuserLoginRecordTop50(userId) {
    return request({
      url: `/admin/core/userLoginRecord/listTop50/${userId}`,
      method: "get",
    });
  },
  getUserCountWithTime() {
    return request({
      url: `/admin/core/userInfo/getUserCountWithTime`,
      method: "get",
    });
  },
  getForCharts() {
    return request({
      url: `/admin/core/userInfo/getUserInfoForCharts`,
      method: "get",
    });
  },
};
