/*
 * Copyright (C) 2014 mat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package briscola;

import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GeneralGUI extends javax.swing.JFrame {

    protected javax.swing.JTextArea logTextArea;
    protected javax.swing.JList playersList;
    protected javax.swing.JTextPane chatPanel;
    protected javax.swing.JTextArea chatTextArea;

    protected void say(String s) {
        logTextArea.append(s + "\n");
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
            ///doc.insertString(0, "Start of text\n", null);
            doc.insertString(doc.getLength(), "\n" + name + "> " + s, keyWord);
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
