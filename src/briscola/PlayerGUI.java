/*
 */
package briscola;

import static briscola.common.ACLCodes.ACL_COMMENT_GIOCATA;
import briscola.memory.Giocata;
import briscola.messages.GiocataCommentMessage;
import briscola.objects.Bid;
import briscola.objects.Card;
import briscola.objects.Hand;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;

/**
 *
 * @author mat
 */
public class PlayerGUI extends GeneralGUI {

    private static final long serialVersionUID = 1L;

    private final PlayerAgent agent;
    private final Map<String, Giocata> giocate = new HashMap<>();

    /**
     * Creates new form PlayerGUI
     */
    public PlayerGUI(PlayerAgent agent) {
        this.agent = agent;
        initComponents();
        commentButton.setEnabled(false);
        newGameButton.setVisible(false);
        endGameButton.setVisible(false);

    }

    protected void addBid(Bid bid) {
        DefaultListModel<Bid> l
            = (DefaultListModel<Bid>) bidsList.getModel();
        l.addElement(bid);
    }

    public void setHand(Hand h) {
        DefaultListModel<Card> l
            = (DefaultListModel<Card>) cardList.getModel();
        l.removeAllElements();
        for (int i = 0; i < h.size(); ++i) {
            addCard(h.get(i));
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
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
        jScrollPane1 = new javax.swing.JScrollPane();
        bidsList = new javax.swing.JList();
        giocatePane = new javax.swing.JScrollPane();
        listGiocate = new javax.swing.JList();
        CardsPane = new javax.swing.JScrollPane();
        cardList = new javax.swing.JList();
        giocaButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        commentButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        actionsTextArea = new javax.swing.JTextArea();
        newGameButton = new javax.swing.JButton();
        endGameButton = new javax.swing.JButton();
        logPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        nameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Briscola in 5");

        gamePane.setBackground(new java.awt.Color(51, 51, 51));

        playersList.setBackground(new java.awt.Color(51, 51, 51));
        playersList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        playersList.setForeground(new java.awt.Color(204, 204, 204));
        playersList.setModel(new javax.swing.DefaultListModel<Player>());
        playersPane.setViewportView(playersList);

        chatPanel.setEditable(false);
        chatPanel.setBackground(new java.awt.Color(51, 51, 51));
        chatPanel.setBorder(null);
        chatPanel.setForeground(new java.awt.Color(204, 204, 204));
        chatPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        chatContainerPane.setViewportView(chatPanel);

        chatTextArea.setBackground(new java.awt.Color(51, 51, 51));
        chatTextArea.setColumns(20);
        chatTextArea.setForeground(new java.awt.Color(204, 204, 204));
        chatTextArea.setRows(5);
        chatTextArea.setBorder(null);
        chatTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chatTextAreaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                chatTextAreaKeyReleased(evt);
            }
        });
        chatTextAreaPane.setViewportView(chatTextArea);

        bidsList.setBackground(new java.awt.Color(51, 51, 51));
        bidsList.setForeground(new java.awt.Color(204, 204, 204));
        bidsList.setModel(new javax.swing.DefaultListModel<Bid>()
        );
        jScrollPane1.setViewportView(bidsList);

        listGiocate.setModel(new javax.swing.DefaultListModel<String>());
        listGiocate.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listGiocateValueChanged(evt);
            }
        });
        giocatePane.setViewportView(listGiocate);

        cardList.setModel(new javax.swing.DefaultListModel<Card>()
        );
        CardsPane.setViewportView(cardList);

        giocaButton.setText("Gioca");
        giocaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                giocaButtonActionPerformed(evt);
            }
        });

        commentTextArea.setColumns(20);
        commentTextArea.setRows(5);
        jScrollPane2.setViewportView(commentTextArea);

        commentButton.setText("Commenta");
        commentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commentButtonActionPerformed(evt);
            }
        });

        actionsTextArea.setColumns(20);
        actionsTextArea.setRows(5);
        jScrollPane3.setViewportView(actionsTextArea);

        newGameButton.setText("Nuova partita");
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameButtonActionPerformed(evt);
            }
        });

        endGameButton.setText("Partita Conclusa");
        endGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endGameButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gamePaneLayout = new javax.swing.GroupLayout(gamePane);
        gamePane.setLayout(gamePaneLayout);
        gamePaneLayout.setHorizontalGroup(
            gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                            .addComponent(playersPane))
                        .addGap(18, 18, 18)
                        .addComponent(giocatePane, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(chatContainerPane)
                            .addComponent(chatTextAreaPane, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(CardsPane))
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGap(119, 119, 119)
                                .addComponent(giocaButton)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(commentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePaneLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(endGameButton)
                                .addGap(18, 18, 18)
                                .addComponent(newGameButton)))
                        .addContainerGap())))
        );
        gamePaneLayout.setVerticalGroup(
            gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePaneLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addComponent(playersPane, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(giocatePane)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(commentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addComponent(chatContainerPane, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chatTextAreaPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gamePaneLayout.createSequentialGroup()
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3)
                            .addComponent(CardsPane, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                        .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(giocaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(gamePaneLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(gamePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(newGameButton)
                                    .addComponent(endGameButton))))))
                .addContainerGap())
        );

        giocaButton.setEnabled(false);

        jTabbedPane1.addTab("Game", gamePane);

        logTextArea.setBackground(new java.awt.Color(0, 0, 0));
        logTextArea.setColumns(20);
        logTextArea.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        logTextArea.setForeground(new java.awt.Color(204, 204, 204));
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

    private void chatTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chatTextAreaKeyPressed

    }//GEN-LAST:event_chatTextAreaKeyPressed

    private void chatTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chatTextAreaKeyReleased
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            String text = chatTextArea.getText();
            agent.sendChat(text);
            chatTextArea.setCaretPosition(0);
            chatTextArea.setText("");
        }
    }//GEN-LAST:event_chatTextAreaKeyReleased

    private void giocaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giocaButtonActionPerformed

        Card daGiocare = (Card) cardList.getSelectedValue();
        agent.say("Gioco la carta " + daGiocare, true);
        agent.setCardToPlay(daGiocare);
        DefaultListModel<Card> dlm = (DefaultListModel<Card>) cardList.getModel();
        dlm.removeElement(daGiocare);
        giocaButton.setEnabled(false);

    }//GEN-LAST:event_giocaButtonActionPerformed

    private void commentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commentButtonActionPerformed
        String s = listGiocate.getSelectedValue().toString();
        Giocata g = giocate.get(s);
        String comment = commentTextArea.getText();
        commentTextArea.setText("");
        agent.say("Commento " + comment + " alla giocata" + s);
        GiocataCommentMessage m = new GiocataCommentMessage(agent.getPlayer(), g,
                                                            comment);
        agent.sendMessage(agent.getMazziereAID(), ACL_COMMENT_GIOCATA, m);

        commentButton.setEnabled(false);
    }//GEN-LAST:event_commentButtonActionPerformed

    private void listGiocateValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listGiocateValueChanged
        if (listGiocate.getSelectedIndex() == -1) {
            commentButton.setEnabled(false);
        } else {
            commentButton.setEnabled(true);
        }
    }//GEN-LAST:event_listGiocateValueChanged

    private void newGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameButtonActionPerformed
        agent.newGame();
    }//GEN-LAST:event_newGameButtonActionPerformed

    private void endGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endGameButtonActionPerformed
        agent.setGameOver(true);
        endGameButton.setVisible(false);
    }//GEN-LAST:event_endGameButtonActionPerformed

    protected JLabel[] cardLabels;

    void addGiocata(int mano, int counter, Player justPlayer,
                    Card justCard) {
        String s = "[" + counter + " ] " + justPlayer.getName() + ": " + justCard;
        Giocata g = new Giocata(mano, counter, justPlayer, null, justCard);
        giocate.put(s, g);
        DefaultListModel<String> l
            = (DefaultListModel<String>) listGiocate.getModel();
        l.addElement(s);
        int lastIndex = l.getSize() - 1;
        try {
            if (lastIndex >= 0 && listGiocate != null) {
                listGiocate.ensureIndexIsVisible(lastIndex);
            }
        } catch (Exception e) {
            System.out.println(
                "Qualcosa è andato storto nel tentare di scorrere in fondo alla lista delle giocate");
            System.out.println("Tutta colpa di Swing");
//            e.printStackTrace();
        }
    }

    public void initMano(int counter, Player next) {
        DefaultListModel<String> l
            = (DefaultListModel<String>) listGiocate.getModel();
        l.addElement("Mano #" + (counter + 1));
    }

    protected void addCard(Card card) {
        DefaultListModel<Card> l
            = (DefaultListModel<Card>) cardList.getModel();
        l.addElement(card);
    }

    void beginGiocata() {
        giocaButton.setEnabled(true);
    }

    void newGame() {
        newGameButton.setVisible(false);
        clean();
    }

    public void enableNewGame() {
        newGameButton.setVisible(true);
    }

    @Override
    public void clean() {
        super.clean();
        DefaultListModel listModel = (DefaultListModel) cardList.getModel();
        listModel.removeAllElements();
        listModel = (DefaultListModel) bidsList.getModel();
        listModel.removeAllElements();
        listModel = (DefaultListModel) listGiocate.getModel();
        listModel.removeAllElements();
        actionsTextArea.setText("");
        chatTextArea.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane CardsPane;
    private javax.swing.JTextArea actionsTextArea;
    private javax.swing.JList bidsList;
    private javax.swing.JList cardList;
    private javax.swing.JScrollPane chatContainerPane;
//    protected javax.swing.JTextPane chatPanel;
//    protected javax.swing.JTextArea chatTextArea;
    private javax.swing.JScrollPane chatTextAreaPane;
    private javax.swing.JButton commentButton;
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JButton endGameButton;
    private javax.swing.JPanel gamePane;
    private javax.swing.JButton giocaButton;
    private javax.swing.JScrollPane giocatePane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList listGiocate;
    private javax.swing.JScrollPane logPane;
//    protected javax.swing.JTextArea logTextArea;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton newGameButton;
//   protected javax.swing.JList playersList;
    private javax.swing.JScrollPane playersPane;
    // End of variables declaration//GEN-END:variables

    void toggleEndGameButton() {
        endGameButton.setVisible(!endGameButton.isVisible());
    }

    void setEndGameButton(boolean b) {
        endGameButton.setVisible(b);
    }

    void addAction(String s) {
        actionsTextArea.append(s + "\n");
    }

}
