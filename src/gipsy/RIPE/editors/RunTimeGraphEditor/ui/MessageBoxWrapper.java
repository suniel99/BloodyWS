package gipsy.RIPE.editors.RunTimeGraphEditor.ui;

import gipsy.RIPE.editors.RunTimeGraphEditor.core.AppConstants;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class MessageBoxWrapper
{

    public static void showException(JComponent frame, String strMsg,
            Exception ex)
    {
        JOptionPane.showMessageDialog(frame,
                strMsg + "\n Error: " + ex.getMessage(), "GIPSY Node-Operator",
                JOptionPane.ERROR_MESSAGE);
        System.err.println(ex.getMessage());
    }

    public static void displayErrorMsg(JComponent poComponent, String pstrMsg,
            String pstrDlgBoxTitle)
    {

        JOptionPane.showMessageDialog(poComponent, pstrMsg, pstrDlgBoxTitle, 0,
                new ImageIcon(AppConstants.ICON_ERROR));
    }
}
