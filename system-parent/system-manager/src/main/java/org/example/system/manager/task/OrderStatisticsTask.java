package org.example.system.manager.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.OrderInfoMapper;
import org.example.system.manager.mapper.OrderStatisticsMapper;
import org.example.system.model.entity.order.OrderStatistics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderStatisticsTask {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderStatisticsMapper orderStatisticsMapper;

//    //测试定时任务
//    //每隔5s，方法执行一次
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void testHello(){
//        System.out.println(new Date().toInstant());
//    }

    //每天凌晨两点，查询前一天统计数据，把统计之后数据添加统计结果里面
    @Scheduled(cron="0 0 2 * * ? ")
    public void orderTotalAmountStatistics() {
        //获取前一天日期
        String createDate = DateUtil.offsetDay(new Date(), -1)
                .toString("yyyy-MM-dd");

        //根据前一天日期进行统计功能
        //统计前一天交易金额
        OrderStatistics orderStatistics=orderInfoMapper.selectStatisticsByDate(createDate);

        //把统计之后的数据，添加统计结果表里面
        if(orderStatistics!=null){
            orderStatisticsMapper.insert(orderStatistics);
        }
    }
}
