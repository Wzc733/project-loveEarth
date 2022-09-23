import request from "@/utils/request";

export default {
  getList(page, limit, searchObj) {
    return request({
      url: `/admin/core/userFeedback/list/${page}/${limit}`,
      method: "get",
      params: searchObj,
    });
  },
  read(id) {
    return request({
      url: `/admin/core/userFeedback/haveRead?id=` + id,
      method: "get",
    });
  },
  getDetail(id) {
    return request({
      url: `/admin/core/userFeedback/getDetail?id=` + id,
      method: "get",
    });
  },
};
