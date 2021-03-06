package com.oylong.myfund.window;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import com.oylong.myfund.data.DataCenter;
import com.oylong.myfund.data.FundData;
import com.oylong.myfund.data.FundDataConvert;
import com.oylong.myfund.util.TableUtilities;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author OyLong
 * @date 2021/02/22 22:01
 **/
public class FundWindow {
    private JButton btnAdd;
    private JTable fundTable;
    private JPanel content;
    private JLabel label_money;
    private JButton btnEditCount;
    private JButton selectedCancle;
    private JButton btnUpdate;
    private JPanel btn_content;
    private JCheckBox cbx_week;

    private Thread thread;


    int selectedRow = -1;

    private void init() {

        btn_content.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        cbx_week.setSelected(DataCenter.getCbxStatus());

        initTable();

        updateTable();

        startSchedule();
    }

    private void initTable() {

        fundTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        fundTable.setEnabled(true);

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();

        tcr.setHorizontalAlignment(SwingConstants.CENTER);

        fundTable.setDefaultRenderer(Object.class, tcr);

        fundTable.setShowGrid(false);

        String fundIds = DataCenter.getFundIds();
        if (fundIds == null || fundIds.split(",").length == 0) {
            return;
        }
    }

    private void updateColors() {
        fundTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    String s = value.toString().substring(0, value.toString().length() - 1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                if (temp > 0) {
                    setForeground(new JBColor(0XFF4040, 0XFF4040));
                } else if (temp < 0) {
                    setForeground(new JBColor(0X90EE90, 0X90EE90));
                } else {
                    setForeground(Color.LIGHT_GRAY);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        fundTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    String s = value.toString().substring(0, value.toString().length() - 1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                if (temp > 0) {
                    setForeground(new JBColor(0XFF4040, 0XFF4040));
                } else if (temp < 0) {
                    setForeground(new JBColor(0X90EE90, 0X90EE90));
                } else {
                    setForeground(Color.LIGHT_GRAY);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

    private String[][] getNewData() {
        if (DataCenter.getFundIds() == null) {
            return null;
        }
        String[] ids = DataCenter.getFundIds().split(",");
        String[][] newData = new String[ids.length][];

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            String url = DataCenter.FUND_URL + id + ".js?rt=" + System.currentTimeMillis();
            HttpResponse response = HttpUtil.createGet(url).timeout(2000).execute();
            if (response.getStatus() == 200) {
                String body = response.body();
                if (body.length() < 10) {
                    continue;
                }
                FundData fundData = null;
                try {
                    fundData = JSONUtil.toBean(body.substring(8, body.length() - 2), FundData.class);
                } catch (Exception e) {
                    NotificationGroup notificationGroup = new NotificationGroup("MyFund", NotificationDisplayType.BALLOON, true);
                    Notification notification = notificationGroup.createNotification("基金代码" + id + "信息获取失败,请检查", NotificationType.ERROR);
                    Notifications.Bus.notify(notification);
                    continue;
                }
                DataCenter.FUND_DATA_MAP.put(fundData.getFundcode(), fundData);
                String[] strings = FundDataConvert.toTableData(fundData);
                if (strings != null) {
                    newData[i] = strings;
                }
            } else {
                NotificationGroup notificationGroup = new NotificationGroup("MyFund", NotificationDisplayType.BALLOON, true);
                Notification notification = notificationGroup.createNotification("基金代码" + id + "信息获取失败,请检查", NotificationType.ERROR);
                Notifications.Bus.notify(notification);
            }
        }

       synchronized (this) {
           DataCenter.ALL_MONEY = 0;
           for (int i = 0; i < ids.length; i++) {
               String id = ids[i];
               DataCenter.ALL_MONEY += DataCenter.getFundMoney(id);
           }
       }

        String sign = DataCenter.ALL_MONEY > 0 ? "+" : "";
        String labelValue = DataCenter.ALL_MONEY != 0 ? sign + String.format("%.3f", DataCenter.ALL_MONEY) : "0";
        label_money.setText(labelValue);
        if (DataCenter.ALL_MONEY > 0) {
            label_money.setForeground(new JBColor(0XFF4040, 0XFF4040));
        } else if (DataCenter.ALL_MONEY < 0) {
            label_money.setForeground(new JBColor(0X90EE90, 0X90EE90));
        } else {
            label_money.setForeground(JBColor.GRAY);
        }
        return newData;
    }

    private void recordSelectedColumn() {
        selectedRow = fundTable.getSelectedRow();
    }

    private void recoverSelectedColumn() {
        if (selectedRow < 0) {
            return;
        }
        fundTable.setRowSelectionInterval(selectedRow, selectedRow);
    }

    private void updateTable() {
        DataCenter.ALL_MONEY = 0;
        String[][] newData = getNewData();
        if (newData == null) {
            return;
        }
        DefaultTableModel model = new DefaultTableModel(newData, DataCenter.HEADERS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recordSelectedColumn();
        fundTable.setModel(model);
        recoverSelectedColumn();

        updateColors();

        TableUtilities.FitTableColumns(fundTable);
    }

    private void startSchedule() {
//        CronUtil.schedule("0 */1 * * * ?", new Task() {
//            @Override
//            public void execute() {
//                updateTable();
//            }
//        });
//        CronUtil.setMatchSecond(true);
//        CronUtil.start();

        if (thread != null) {
            thread.interrupt();
            thread.stop();
        }

        thread = new Thread(() -> {
            while (thread != null && thread.hashCode() == Thread.currentThread().hashCode() && !thread.isInterrupted()) {
                try {
                    updateTable();
                } catch (Exception e) {
                    NotificationGroup notificationGroup = new NotificationGroup("MyFund", NotificationDisplayType.BALLOON, true);
                    Notification notification = notificationGroup.createNotification("基金更新时遇到未知错误:" + e.getLocalizedMessage(), NotificationType.ERROR);
                    Notifications.Bus.notify(notification);
                }
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


    public FundWindow(Project project, ToolWindow toolWindow) {
        init();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹出对话框,用于添加
                Messages.InputDialog inputDialog = new Messages.InputDialog("请输入基金代码,多个请用小写逗号(,)隔开", "修改基金代码", null, DataCenter.getFundIds(), null);
                inputDialog.show();
                if (!inputDialog.isOK()) {
                    return;
                }
                String newString = inputDialog.getInputString();
                String oldString = DataCenter.getFundIds();

                DataCenter.setFundIds(newString);


                if (StringUtils.isEmpty(newString)) {
                    newString = "";
                }
                String[] newIds = newString.split(",");

                if (StringUtils.isEmpty(oldString)) {
                    updateTable();
                    return;
                }

                String[] oldIds = oldString.split(",");


                Set<String> oldSet = new HashSet<>(Arrays.asList(oldIds));

                Set<String> newSet = new HashSet<>(Arrays.asList(newIds));

                oldSet.removeAll(newSet);

                for (String s : oldSet) {
                    DataCenter.FUND_DATA_MAP.remove(s);
                    DataCenter.removeFundCount(s);
                    DataCenter.FUND_MONEY_MAP.remove(s);
                }
                updateTable();
            }
        });


        btnEditCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹出对话框,用于添加或删除
                recordSelectedColumn();
                if (selectedRow < 0) {
                    return;
                }

                String fundId = String.valueOf(fundTable.getValueAt(selectedRow, 0));

                Messages.InputDialog inputDialog = new Messages.InputDialog("请输入基金 " + fundTable.getValueAt(selectedRow, 1) + " 的份额", "修改持有份额", null, String.valueOf(DataCenter.getFundCount(fundId)), null);
                inputDialog.show();
                if (!inputDialog.isOK()) {
                    return;
                }

                String inputString = inputDialog.getInputString();
                if (!StringUtils.isEmpty(inputString)) {
                    DataCenter.setFundCount((String) fundTable.getValueAt(selectedRow, 0), Double.parseDouble(inputString));
                } else {
                    DataCenter.setFundCount((String) fundTable.getValueAt(selectedRow, 0), 0);
                }
                updateTable();
            }
        });
        selectedCancle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fundTable.getSelectionModel().clearSelection();
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });


        cbx_week.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                DataCenter.setCbxStatus(cbx_week.isSelected());
                updateTable();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JPanel getContent() {
        return content;
    }

}
