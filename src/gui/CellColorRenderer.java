package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * @author Stella THAMMAVONG
 * This class is used to color elements in the list of contacts.
 *
 */
public class CellColorRenderer extends DefaultListCellRenderer { 

    /**
	 * Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This method returns a component that has been configured to display the specified value.
     */
    public Component getListCellRendererComponent( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {  
        
    	Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );  
        if(GUI.colorToChangeList.size()!=0) {//If there are elements in the colorToChangeList...
        	for(int i = 0; i<GUI.colorToChangeList.size();i++) {
        		if ( index == GUI.colorToChangeList.get(i) ) {  
        			c.setBackground( Color.green );//the items of the corresponding indices will become green in the list
        		}  
        	}
        }
        return c;  
    }  
}  