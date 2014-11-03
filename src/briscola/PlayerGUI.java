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

/**
 *
 * @author mat
 */
public class PlayerGUI extends GeneralGUI {

    private final PlayerAgent agent;

    /**
     * Creates new form PlayerGUI
     */
    public PlayerGUI(PlayerAgent agent) {
        this.agent = agent;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        gamePane = new javax.swing.JPanel();
        playersPane = new javax.swing.JScrollPane();
        playersList = new javax.swing.JList();
        chatContainerPane = new javax.swing.JScrollPane();
        chatPanel = new javax.swing.JTextPane();
        chatTextAreaPane = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        chatButton = new javax.swing.JButton();
        logPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        nameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Briscola in 5");

        playersList.setModel(new javax.swing.DefaultListModel<Player>());
        playersPane.setViewportView(playersList);

        chatContainerPane.setViewportView(chatPanel);

        chatTextArea.setColumns(20);
        chatTextArea.setRows(5);
        chatTextAreaPane.setViewportView(chatTextArea);

        chatButton.setText("Invia");
        chatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gamePaneLayout = new javax.swing.GroupLayout(gamePane);
        gamePane.setLayout(gamePaneLayout);
        gamePaneLayout.setHorizontalGroup(
            gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePaneLayout.createSequentialGroup()
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(chatTextAreaPane)
                        .addComponent(playersPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                        .addComponent(chatContainerPane, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(chatButton)))
                .addContainerGap(569, Short.MAX_VALUE))
        );
        gamePaneLayout.setVerticalGroup(
            gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePaneLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(playersPane, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chatContainerPane, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chatTextAreaPane, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(chatButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Game", gamePane);

        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        logPane.setViewportView(logTextArea);

        jTabbedPane1.addTab("Log", logPane);

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nameLabel.setText(agent.getRealName());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(278, 278, 278)
                .addComponent(nameLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatButtonActionPerformed
        String text = chatTextArea.getText();
        chatTextArea.setText("");
        agent.sendChat(text);
    }//GEN-LAST:event_chatButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton chatButton;
    private javax.swing.JScrollPane chatContainerPane;
    //protected javax.swing.JTextPane chatPanel;
    //protected javax.swing.JTextArea chatTextArea;
    private javax.swing.JScrollPane chatTextAreaPane;
    private javax.swing.JPanel gamePane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane logPane;
    //protected javax.swing.JTextArea logTextArea;
    private javax.swing.JLabel nameLabel;
    //protected javax.swing.JList playersList;
    private javax.swing.JScrollPane playersPane;
    // End of variables declaration//GEN-END:variables

}
