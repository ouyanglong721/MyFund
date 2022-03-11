package com.oylong.myfund.data;

import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 数据中心
 *
 * @author OyLong
 * @date 2021/02/22 22:50
 **/
public class DataCenter {
    public static String[] HEADERS = {"基金代码", "基金名称", "估算涨跌幅", "更新时间", "最新净值", "持有份额", "预计盈亏"};
    public static String[] HEADERS_ENGLISH = {"CODE", "NAME", "ABOUT", "UPDATE", "NEWEST", "HAD", "GET"};

    public static final String MY_FUND_IDS = "MY_FUND_IDS";
    public static final String MY_FUND_COUNT_KEY = "MY_FUND_COUNT_";
    public static final String MY_FUND_CBX_STATUS = "MY_FUND_CBX_STATUS";

    public static final String FUND_URL = "http://fundgz.1234567.com.cn/js/";

    public static double ALL_MONEY = 0;

    public static volatile boolean CBX_STATUS = false;
    public static volatile boolean CBX_STATUS_TRUE = true;

    public static final ExecutorService UPDATE_THREAD_POOL_EXECUTOR;

    public static final HashMap<String, FundData> FUND_DATA_MAP = new HashMap<>();

    public static final ConcurrentHashMap<String, Double> FUND_MONEY_MAP = new ConcurrentHashMap<>();

    static {
        // 更新线程池
        UPDATE_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(2, 4, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), r -> new Thread(r, "update thread-" + r.hashCode()), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static void setFundIds(String ids) {
        PropertiesComponent.getInstance().setValue(MY_FUND_IDS, ids);
    }

    public static String getFundIds() {
        return PropertiesComponent.getInstance().getValue(MY_FUND_IDS);
    }

    public static void setCbxStatus(boolean b) {
        CBX_STATUS = b;
        PropertiesComponent.getInstance().setValue(MY_FUND_CBX_STATUS, b);
    }

    public static boolean getCbxStatus() {
        String value = PropertiesComponent.getInstance().getValue(MY_FUND_CBX_STATUS);
        if (StringUtils.isEmpty(value) || value.equals("INVALIDVALUE")) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }


    public static void removeFundCount(String id) {
        PropertiesComponent.getInstance().setValue(MY_FUND_COUNT_KEY + id, null);
    }

    public static void setFundCount(String id, double count) {
        PropertiesComponent.getInstance().setValue(MY_FUND_COUNT_KEY + id, String.valueOf(count));
    }

    public static double getFundCount(String id) {
        if (PropertiesComponent.getInstance().getValue(MY_FUND_COUNT_KEY + id) != null) {
            return Double.parseDouble(PropertiesComponent.getInstance().getValue(MY_FUND_COUNT_KEY + id));
        }
        return 0;
    }

    public static void saveMoney(String id, double money) {
        if(id == null) {
            return;
        }
        FUND_MONEY_MAP.put(id, money);
    }

    public static Double getFundMoney(String id) {
        Double money = FUND_MONEY_MAP.get(id);
        if(money == null) {
            return 0.0;
        }
        return money;
    }
}
