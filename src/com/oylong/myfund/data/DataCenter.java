package com.oylong.myfund.data;

import com.intellij.ide.util.PropertiesComponent;

import java.util.HashMap;

/**
 * 数据中心
 *
 * @author OyLong
 * @date 2021/02/22 22:50
 **/
public class DataCenter {
    public static String[] HEADERS = {"基金代码", "基金名称", "估算涨跌幅", "更新时间", "最新净值", "持有份额", "预计盈亏"};

    public static final String MY_FUND_IDS = "MY_FUND_IDS";
    public static final String MY_FUND_COUNT_KEY = "MY_FUND_COUNT_";

    public static final String FUND_URL = "http://fundgz.1234567.com.cn/js/";

    public static double ALL_MONEY = 0;

    public static final HashMap<String, FundData> FUND_DATA_MAP = new HashMap<>();

    public static void setFundIds(String ids) {
        PropertiesComponent.getInstance().setValue(MY_FUND_IDS, ids);
    }

    public static String getFundIds() {
        return PropertiesComponent.getInstance().getValue(MY_FUND_IDS);
    }

    public static void removeFundCount(String id) {
        PropertiesComponent.getInstance().setValue(MY_FUND_COUNT_KEY+id, null);
    }

    public static void setFundCount(String id, double count) {
        PropertiesComponent.getInstance().setValue(MY_FUND_COUNT_KEY+id, String.valueOf(count));
    }

    public static double getFundCount(String id) {
        if(PropertiesComponent.getInstance().getValue(MY_FUND_COUNT_KEY+id) != null) {
            return Double.parseDouble(PropertiesComponent.getInstance().getValue(MY_FUND_COUNT_KEY+id));
        }
        return 0;
    }
}
