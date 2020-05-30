package MVC_Pattern;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CellColorRenderer extends DefaultListCellRenderer {  
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {  
        Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );  
        if(GUI.colorToChangeList.size()!=0) {
        for(int i = 0; i<GUI.colorToChangeList.size();i++) {
            if ( index == GUI.colorToChangeList.get(i) ) {  
                c.setBackground( Color.green );
            }  
        }
        }
        return c;  
    }  
}  