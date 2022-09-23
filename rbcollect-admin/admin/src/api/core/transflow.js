import request from "@/utils/request";

export default {
  getTransFlowList(dealItemNo) {
    return request({
      url: `/admin/core/transFlow/list/${dealItemNo}`,
      method: "get",
    });
  },
};
