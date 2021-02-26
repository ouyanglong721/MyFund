package com.oylong.myfund.window;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import com.oylong.myfund.data.DataCenter;
import com.oylong.myfund.data.FundData;
import com.oylong.myfund.data.FundDataConvert;
import com.oylong.myfund.util.TableUtilities;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
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

    private Thread thread;

    private int[] sizes = new int[]{0,0,0,0,0,0,0};

    int selectedRow = -1;

    private void init() {

        fundTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        fundTable.setPreferredSize(new Dimension(1000, 300));

        initTable();

        updateTable();

        startSchedule();
    }

    private void initTable() {
        fundTable.setEnabled(true);

        String fundIds = DataCenter.getFundIds();
        if (fundIds == null || fundIds.split(",").length == 0) {
            return;
        }
    }

    private void updateColors() {
        fundTable.getColumn("估算涨跌幅").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    String s = value.toString().substring(0,value.toString().length()-1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                Color orgin = getForeground();
                if (temp > 0) {
                    setForeground(new JBColor(0XFF4040, 0XFF4040));
                } else if (temp < 0) {
                    setForeground(new JBColor(0X90EE90, 0X90EE90));
                } else if (temp == 0) {
                    setForeground(orgin);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        fundTable.getColumn("预计盈亏").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    String s = value.toString().substring(0,value.toString().length()-1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                Color orgin = getForeground();
                if (temp > 0) {
                    setForeground(new JBColor(0XFF4040, 0XFF4040));
                } else if (temp < 0) {
                    setForeground(new JBColor(0X90EE90, 0X90EE90));
                } else if (temp == 0) {
                    setForeground(orgin);
                }
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

        DataCenter.ALL_MONEY = 0;
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            HttpResponse response = HttpUtil.createGet(DataCenter.FUND_URL + id + ".js").execute();
            if (response.getStatus() == 200) {
                String body = response.body();
                if (body.length() < 10) {
                    continue;
                }
                FundData fundData = null;
                try {
                    fundData = JSONUtil.toBean(body.substring(8, body.length() - 2), FundData.class);
                } catch (Exception e) {
                    new NotificationGroup("解析异常", NotificationDisplayType.NONE, true).createNotification("基金"+id+"解析异常", MessageType.INFO).notify();
                }
                DataCenter.FUND_DATA_MAP.put(fundData.getFundcode(), fundData);
                newData[i] = FundDataConvert.toTableData(fundData);
            }
        }
        String sign = DataCenter.ALL_MONEY>0?"+":"";
        label_money.setText(sign+String.format("%.3f", DataCenter.ALL_MONEY));
        if(DataCenter.ALL_MONEY > 0) {
            label_money.setForeground(new JBColor(0XFF4040, 0XFF4040));
        } else if(DataCenter.ALL_MONEY < 0) {
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
        if(selectedRow < 0) {
            return;
        }
        fundTable.setRowSelectionInterval(selectedRow, selectedRow);
    }

    private void updateTable() {
        String[][] newData = getNewData();
        DefaultTableModel model = new DefaultTableModel(newData, DataCenter.HEADERS){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recordSelectedColumn();
        fundTable.setModel(model);
        recoverSelectedColumn();
        updateColors();
        TableUtilities.fixTableColumnWidth(fundTable);

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

        if (thread!=null){
            thread.interrupt();
            thread.stop();
        }

        thread = new Thread(() -> {
            while (thread!=null && thread.hashCode() == Thread.currentThread().hashCode() && !thread.isInterrupted()) {
                updateTable();
                try {
                    Thread.sleep(60*1000);
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
                if(!inputDialog.isOK()) {
                    return;
                }
                String newString = inputDialog.getInputString();
                String oldString = DataCenter.getFundIds();

                DataCenter.setFundIds(newString);

                if(StringUtils.isEmpty(newString)) {
                    newString = "";
                }
                String[] newIds = newString.split(",");

                if(StringUtils.isEmpty(oldString)) {
                    updateTable();
                    return;
                }

                String[] oldIds = oldString.split(",");


                Set<String> oldSet = new HashSet<>(Arrays.asList(oldIds));

                Set<String> newSet = new HashSet<>(Arrays.asList(newIds));

                oldSet.removeAll(newSet);

                for (String s : oldSet) {
                    DataCenter.FUND_DATA_MAP.remove("s");
                    DataCenter.removeFundCount(s);
                }
                updateTable();
            }
        });


        btnEditCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹出对话框,用于添加或删除
                recordSelectedColumn();
                if(selectedRow < 0) {
                    return;
                }

                String fundId = String.valueOf(fundTable.getValueAt(selectedRow, 0));

                Messages.InputDialog inputDialog = new Messages.InputDialog("请输入基金 "+fundTable.getValueAt(selectedRow, 1)+" 的份额", "修改持有份额", null, String.valueOf(DataCenter.getFundCount(fundId)), null);
                inputDialog.show();
                if(!inputDialog.isOK()) {
                    return;
                }

                String inputString = inputDialog.getInputString();
                if (inputString != null) {
                    DataCenter.setFundCount((String) fundTable.getValueAt(selectedRow, 0), Double.parseDouble(inputString));
                    updateTable();
                }

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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JPanel getContent() {
        return content;
    }

}
