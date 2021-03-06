    /*
 */
package briscola;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GeneralGUI extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    protected javax.swing.JTextArea logTextArea;
    protected javax.swing.JList playersList;
    protected javax.swing.JTextPane chatPanel;
    protected javax.swing.JTextArea chatTextArea;

    protected void clean() {
        logTextArea.append("\n\n\n\n\n");
        DefaultListModel listModel = (DefaultListModel) playersList.getModel();
        listModel.removeAllElements();
    }

    protected void say(String s) {
        logTextArea.append(s + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }

    public void appendChat(String name, String s, int color) {
        StyledDocument doc = chatPanel.getStyledDocument();
        Color[] colors = new Color[7];
        colors[0] = Color.BLUE;
        colors[1] = Color.MAGENTA;
        colors[2] = Color.DARK_GRAY;
        colors[3] = Color.GREEN;
        colors[4] = Color.RED;
        colors[5] = Color.BLACK;
        colors[6] = Color.LIGHT_GRAY;

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, colors[color]);
        //StyleConstants.setBackground(keyWord, Color.YELLOW);
        StyleConstants.setBold(keyWord, true);
//  Add some text
        try {
            doc.insertString(doc.getLength(), "" + name + ">\t" + s + "\n",
                             keyWord);
            // Scroll the text
            final int length = chatPanel.getText().length();
            chatPanel.setCaretPosition(length);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected void addPlayer(Player player) {
        DefaultListModel<Player> l
            = (DefaultListModel<Player>) playersList.getModel();
        l.addElement(player);
    }
}
