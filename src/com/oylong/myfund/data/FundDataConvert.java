package com.oylong.myfund.data;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换工具类
 *
 * @author OyLong
 * @date 2021/02/25 12:19
 **/
public class FundDataConvert {
    public static String[] toTableData (FundData fundData) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String today = simpleDateFormat.format(new Date());
        String jzDate = simpleDateFormat.format(fundData.getJzrq());

        String gzDate = today.equals(jzDate)?"":"("+jzDate+")";

        double money = fundData.getDwjz()*DataCenter.getFundCount(fundData.getFundcode());


        if(!StringUtils.isEmpty(gzDate)) {
            money = money*fundData.getGszzl()/100.0;
        }
        DataCenter.ALL_MONEY+=money;

        String sign =money>0?"+":"";

        return new String[] {
                fundData.getFundcode(),
                fundData.getName(),
                fundData.getGszzl()+"%",
                new SimpleDateFormat("HH:mm").format(fundData.getGztime()),
                fundData.getDwjz() + gzDate,
                String.valueOf(DataCenter.getFundCount(fundData.getFundcode())),
                sign+String.format("%.3f", money)
        };
    }
}
