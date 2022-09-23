package com.fwzc.rbcollect.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fwzc.common.exception.Assert;
import com.fwzc.common.result.ResponseEnum;
import com.fwzc.common.util.LendNoUtils;
import com.fwzc.rbcollect.core.enums.CommonEnum;
import com.fwzc.rbcollect.core.mapper.*;
import com.fwzc.rbcollect.core.pojo.entity.*;
import com.fwzc.rbcollect.core.pojo.entity.dto.SortByImgDTO;
import com.fwzc.rbcollect.core.pojo.entity.dto.SortDTO;
import com.fwzc.rbcollect.core.pojo.entity.query.RbDealItemQuery;
import com.fwzc.rbcollect.core.service.RbDealItemService;
import com.fwzc.rbcollect.core.service.TransFlowService;
import com.fwzc.rbcollect.core.utils.ConsumeHelper;
import com.fwzc.rbcollect.core.utils.CountHelper;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fwzc.rbcollect.core.enums.CommonEnum.UN_FINISH;

/**
 * <p>
 * 垃圾回收下单订单 服务实现类
 * </p>
 *
 * @author wzc
 * @since 2022-03-30
 */
@Service
public class RbDealItemServiceImpl extends ServiceImpl<RbDealItemMapper, RbDealItem> implements RbDealItemService {

    @Resource
    private TransFlowMapper transFlowMapper;
    @Value(value = "${tianApi.APIKEY}")
    private String APIKEY;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserIntegralMapper userIntegralMapper;
    @Resource
    private UserLocalMapper userLocalMapper;
    @Resource
    private RbDealItemService rbDealItemService;
    @Resource
    private RbDealItemMapper rbDealItemMapper;
    @Resource
    private TransFlowService transFlowService;
    @Resource
    private IntegralGradeMapper integralGradeMapper;
    @Resource
    private DictMapper dictMapper;


    @Override
    public IPage<RbDealItem> listPage(Long userId,Page<RbDealItem> pageParm, RbDealItemQuery rbDealItemQuery) {
        QueryWrapper<RbDealItem> rbDealItemQueryWrapper = new QueryWrapper<>();
        if (userId!=null){
            rbDealItemQueryWrapper.eq("rb_user_id",userId);
        }
        rbDealItemQueryWrapper.orderByDesc("create_time");
        if (rbDealItemQuery==null||rbDealItemQuery.getRbUserId() == null) {
            Page<RbDealItem> rbDealItemPage = baseMapper.selectPage(pageParm, rbDealItemQueryWrapper);
            List<RbDealItem> records = rbDealItemPage.getRecords();
            records.forEach(rbDealItem -> {
                String rbName = dictMapper.selectById(rbDealItem.getRbName()).getName();
                String rbType = dictMapper.selectById(rbDealItem.getRbType()).getName();
                String status = CommonEnum.getMsgByType(Integer.parseInt(rbDealItem.getStatus()));
                rbDealItem.setRbName(rbName);
                rbDealItem.setRbType(rbType);
                rbDealItem.setStatus(status);
            });
            rbDealItemPage.setRecords(records);
            return rbDealItemPage;
        }

        Long rbUserId = rbDealItemQuery.getRbUserId();


        rbDealItemQueryWrapper.eq(rbUserId != null, "rb_user_id", rbUserId);
        Page<RbDealItem> rbDealItemPage = baseMapper.selectPage(pageParm, rbDealItemQueryWrapper);
        List<RbDealItem> records = rbDealItemPage.getRecords();
        records.forEach(rbDealItem -> {
            String rbName = dictMapper.selectById(rbDealItem.getRbName()).getName();
            String rbType = dictMapper.selectById(rbDealItem.getRbType()).getName();
            String status = CommonEnum.getMsgByType(Integer.parseInt(rbDealItem.getStatus()));
            rbDealItem.setRbName(rbName);
            rbDealItem.setRbType(rbType);
            rbDealItem.setStatus(status);
        });
        rbDealItemPage.setRecords(records);
        return rbDealItemPage;
    }



    @Override
    public SortDTO getRbSort(String word) throws IOException {
        String httpUrl = "http://api.tianapi.com/lajifenlei/index?key=" + APIKEY + "&word=" + word + "&mode=" + 1;
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        result = sbf.toString();
        System.out.println(result);
        SortDTO sortVO = JSON.parseObject(result, SortDTO.class);
        return sortVO;
    }

    @Override
    public SortByImgDTO getRbSortByImg(String imgUrl) throws IOException {
        //新建一个客户对象
        CloseableHttpClient client = HttpClients.createDefault();
        //新建一个HttpPost请求的对象 --并将uri传给这个对象
        HttpPost post = new HttpPost("http://api.tianapi.com/imglajifenlei/index");
        //使用NameValuePair  键值对  对参数进行打包
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("key", APIKEY));
        list.add(new BasicNameValuePair("imgurl", imgUrl));
        //4.对打包好的参数，使用UrlEncodedFormEntity工具类生成实体的类型数据
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        //5.将含有请求参数的实体对象放到post请求中
        post.setEntity(entity);
        //6.新建一个响应对象来接收客户端执行post的结果
        CloseableHttpResponse response = client.execute(post);
        //7.从响应对象中提取需要的结果-->实际结果,与预期结果进行对比
        if (response.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(response.getEntity());
            SortByImgDTO sortByImgDTO = JSON.parseObject(result, SortByImgDTO.class);
            return sortByImgDTO;
        }else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRbDealItem(Long userId, UserInfo userInfo,RbDealItem rbdealitem) {

        String name = userInfo.getName();
        String itemNo = LendNoUtils.getRbDealItemNo();
        Integer status = userInfo.getStatus();


        //通过前端传的RbDealItem对象的UserLocal的id来取地址内容设置进RbDealItem对象有关地址的字段
        Long userLocalId = rbdealitem.getUserLocalId();
        UserLocal userLocal = userLocalMapper.selectById(userLocalId);
        String userMobile = userLocal.getUserMobile();
        String local = userLocal.getUserLocal();
        String userName = userLocal.getUserName();

        String rbType = rbdealitem.getRbType();
        String rbName = rbdealitem.getRbName();
        String rbWeight = rbdealitem.getRbWeight();
        LocalDateTime serviceTime = rbdealitem.getServiceTime();

        RbDealItem rbDealItem = new RbDealItem();
        rbDealItem.setDealItemNo(itemNo);
        rbDealItem.setRbUserId(userId);
        rbDealItem.setRbUserName(name);
        rbDealItem.setRbName(rbName);
        rbDealItem.setRbType(rbType);
        rbDealItem.setRbWeight(rbWeight);
        rbDealItem.setDeliveryUserLocal(local);
        rbDealItem.setDeliveryUserMobile(userMobile);
        rbDealItem.setDeliveryUserName(userName);
        //前端传过来的Date数据直接存进数据库了，我们手动给它加8小时
        rbDealItem.setServiceTime(serviceTime.plusHours(8));
        //未完成
        rbDealItem.setStatus(UN_FINISH.getType().toString());
        //通过userIntegrals计算在该重量应该消耗多少积分
        List<IntegralGrade> integralGrades = integralGradeMapper.selectList(null);
        Integer consumeIntegral = ConsumeHelper.ConsumeHOfIntegral(Integer.parseInt(rbWeight), integralGrades);

        Integer integral = CountHelper.CalculationOfIntegral(rbType, rbWeight,consumeIntegral);
        rbDealItem.setExpectIntegral(integral);
        //生成交易流水记录
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userId);
        transFlow.setUserName(userName);
        transFlow.setDealItemNo(itemNo);
        transFlow.setTransType(CommonEnum.RB_REBACK.getType());
        transFlow.setTransAmount(integral);
        transFlow.setMemo(CommonEnum.APPLY_FOR_RECYCLING.getMsg());
        transFlowService.save(transFlow);

        //设置流水线记录id和用户地址id
        QueryWrapper<TransFlow> transFlowQueryWrapper = new QueryWrapper<>();
        transFlowQueryWrapper.eq("deal_item_no",itemNo);
        TransFlow flow = transFlowMapper.selectOne(transFlowQueryWrapper);
        rbDealItem.setUserLocalId(userLocalId);
        rbDealItem.setTransFlowId(flow.getId());

        boolean save = rbDealItemService.save(rbDealItem);
        return save;
    }


    @Override
    public List<RbDealItem> listFiveItem(Long userId) {
        QueryWrapper<RbDealItem> wrapper = new QueryWrapper<>();
        wrapper.eq("rb_user_id",userId).orderByDesc("update_time").last("limit 5");
        List<RbDealItem> rbDealItems = baseMapper.selectList(wrapper);
        rbDealItems.forEach(rbDealItem -> {
            String rbName = dictMapper.selectById(rbDealItem.getRbName()).getName();
            String rbType = dictMapper.selectById(rbDealItem.getRbType()).getName();
            String status = CommonEnum.getMsgByType(Integer.parseInt(rbDealItem.getStatus()));
            rbDealItem.setRbName(rbName);
            rbDealItem.setRbType(rbType);
            rbDealItem.setStatus(status);
        });
        return rbDealItems;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRbDeal(Integer rbDealId) {
        try {
            QueryWrapper<RbDealItem> wrapper = new QueryWrapper<>();
            wrapper.eq("id",rbDealId);
            RbDealItem rbDealItem = baseMapper.selectOne(wrapper);
            //用户申请取消1  直接删除
            baseMapper.deleteById(rbDealId);
            //查询数据拼接给TransFlow对象
            Long rbUserId = rbDealItem.getRbUserId();
            String rbUserName = rbDealItem.getRbUserName();
            String dealItemNo = rbDealItem.getDealItemNo();

            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(dealItemNo);
            transFlow.setTransType(CommonEnum.RB_REBACK.getType());
            transFlow.setMemo(CommonEnum.CANCEL_THE_RECYCLING.getMsg());

            transFlowMapper.insert(transFlow);
            rbDealItemMapper.updateById(rbDealItem);
        }catch (Exception e){
            return  false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendRb(Long id) {
        try {
            RbDealItem rbDealItem = rbDealItemMapper.selectById(id);
            //状态修改
            rbDealItem.setStatus(CommonEnum.IN_RECYCLING.getType().toString());



            //添加流水线
            Long rbUserId = rbDealItem.getRbUserId();
            String rbUserName = rbDealItem.getRbUserName();
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(LendNoUtils.getRbDealItemNo());
            transFlow.setTransType(CommonEnum.RB_REBACK.getType());
            transFlow.setMemo(CommonEnum.CONFIRM_THE_RECYCLING_ADMIN.getMsg());
            transFlowMapper.insert(transFlow);
            baseMapper.updateById(rbDealItem);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean DoneRb(Long id) {
        try {
            RbDealItem rbDealItem = rbDealItemMapper.selectById(id);
            //状态修改
            rbDealItem.setStatus(CommonEnum.All_FINISH.getType().toString());


            //添加流水线
            Long rbUserId = rbDealItem.getRbUserId();
            String rbUserName = rbDealItem.getRbUserName();
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(LendNoUtils.getRbDealItemNo());
            transFlow.setTransType(CommonEnum.RB_REBACK.getType());
            transFlow.setMemo(CommonEnum.All_FINISH.getMsg());

            //增加用户积分
            QueryWrapper<UserIntegral> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",rbUserId);
            UserIntegral userIntegral = userIntegralMapper.selectOne(wrapper);
            int addIntegral1 = userIntegral.getIntegral() + rbDealItem.getExpectIntegral();
            userIntegral.setIntegral(addIntegral1);
            UserInfo userInfo = userInfoMapper.selectById(rbUserId);
            int addIntegral2 = userInfo.getIntegral() + rbDealItem.getExpectIntegral();
            Assert.isTrue(addIntegral1==addIntegral2,ResponseEnum.INTEGRAL_ERROR);
            userInfo.setIntegral(addIntegral2);

            transFlowMapper.insert(transFlow);
            baseMapper.updateById(rbDealItem);
            userInfoMapper.updateById(userInfo);
            userIntegralMapper.updateById(userIntegral);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public boolean tryDeleteRbDeal(Integer rbDealId) {
        try {
            RbDealItem rbDealItem = rbDealItemMapper.selectById(rbDealId);
            //状态修改
            rbDealItem.setStatus(CommonEnum.IN_REVIEW.getType().toString());
            //用户申请取消
            rbDealItem.setCancleCycle(true);

            //添加流水线
            Long rbUserId = rbDealItem.getRbUserId();
            String rbUserName = rbDealItem.getRbUserName();
            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(LendNoUtils.getRbDealItemNo());
            transFlow.setTransType(CommonEnum.RB_REBACK.getType());
            transFlow.setMemo(CommonEnum.CANCEL_THE_RECYCLING.getMsg());



            transFlowMapper.insert(transFlow);
            baseMapper.updateById(rbDealItem);
        }catch (Exception e){
            return false;
        }
        return true;

    }

    @Override
    public boolean removeRbDealById(long id) {
        try {
            QueryWrapper<RbDealItem> wrapper = new QueryWrapper<>();
            wrapper.eq("id",id);
            RbDealItem rbDealItem = baseMapper.selectOne(wrapper);
            //用户申请取消2 管理员同意则直接删除
            baseMapper.deleteById(id);
            //查询数据拼接给TransFlow对象
            Long rbUserId = rbDealItem.getRbUserId();
            String rbUserName = rbDealItem.getRbUserName();
            String dealItemNo = rbDealItem.getDealItemNo();

            TransFlow transFlow = new TransFlow();
            transFlow.setUserId(rbUserId);
            transFlow.setUserName(rbUserName);
            transFlow.setDealItemNo(dealItemNo);
            transFlow.setTransType(CommonEnum.RB_REBACK.getType());
            transFlow.setMemo(CommonEnum.CANCEL_THE_RECYCLING_ADMIN.getMsg());

            transFlowMapper.insert(transFlow);
            rbDealItemMapper.updateById(rbDealItem);
        }catch (Exception e){
            return  false;
        }
        return true;

    }


}