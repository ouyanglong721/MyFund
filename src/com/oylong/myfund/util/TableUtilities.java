package com.oylong.myfund.util;

import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Enumeration;

public class  TableUtilities  {

    public static void FitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable, column.getIdentifier()
                            , false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + myTable.getIntercellSpacing().width+20);
        }

        if(myTable == null) {
            return;
        }

        myTable.getTableHeader().setReorderingAllowed(false);
        myTable.getTableHeader().setResizingAllowed(false);

        DefaultTableCellHeaderRenderer hr = new DefaultTableCellHeaderRenderer();
        hr.setHorizontalAlignment(JLabel.CENTER);
        myTable.getTableHeader().setDefaultRenderer(hr);
    }



     public   static  Dimension fixTableColumnWidth(JTable table)  {
         int  MaxWidth  =   0 ;
         int  width  =   0 ;
         for  ( int  i  =   0 ; i  <  table.getColumnCount(); i ++ )  {
            TableColumn col  =  table.getColumn(table.getColumnName(i));
            width  =  getPreferredWidthForColumn(table, col);
            width = Math.max(width, table.getTableHeader().getColumnModel().getColumn(i).getPreferredWidth());
            col.setMinWidth(width);
            col.setMaxWidth(width);
            MaxWidth  +=  width;
        }
         //  sizeColumnsToFit() must be called due to a JTable
         //  bug
         table.sizeColumnsToFit( 0 );

        Dimension tableSize  =   new  Dimension(MaxWidth, table.getPreferredSize().height);
        table.setPreferredSize(tableSize);
         return  tableSize;
    }
      public   static   int  getPreferredWidthForColumn(JTable table, TableColumn col)  {
            int  hw  =  columnHeaderWidth(table, col),    //  hw = header width
             cw  =  widestCellInColumn(table, col);   //  cw = column width

         return  hw  >  cw  ?  hw : cw;
    }
      private   static   int  columnHeaderWidth(JTable table, TableColumn col)  {
        TableCellRenderer renderer  =  col.getHeaderRenderer();
         if  (renderer  ==   null )  {
            renderer  =   new DefaultTableCellRenderer();
            ((DefaultTableCellRenderer)renderer).setText(col.getHeaderValue().toString());
        }
        Component comp  =  renderer.getTableCellRendererComponent(
                                      table, col.getHeaderValue(),
                                       false ,  false ,  0 ,  0 );

         return  comp.getPreferredSize().width  +   2 ;
    }
      private   static   int  widestCellInColumn(JTable table, TableColumn col)  {
         int  c  =  col.getModelIndex(), width = 0 , maxw = 0 ;

         for ( int  r = 0 ; r  <  table.getRowCount();  ++ r)  {
            TableCellRenderer renderer  =
                              table.getCellRenderer(r,c);

            Component comp  =
                renderer.getTableCellRendererComponent(
                                      table, table.getValueAt(r,c),
                                       false ,  false , r, c);

            width  =  comp.getPreferredSize().width  +   2 ;
            maxw  =  width  >  maxw  ?  width : maxw;
        }
         return  maxw;
    }
}
