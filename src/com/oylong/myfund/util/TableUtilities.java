package com.oylong.myfund.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class  TableUtilities  {
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
