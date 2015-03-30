package briscola;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import jess.Fact;
import jess.JessException;
import jess.Rete;

public class GeneralAgent extends Agent {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected List<Player> players;
    protected GeneralGUI gui;
    protected AID mazziereAID;
    protected Rete rete;
    protected boolean graphic = true;
    protected List<Behaviour> behaviours;

    private String chatID;

    public void setChatID(String uniqueKey) {
        this.chatID = uniqueKey;
    }

    public String getChatID() {
        return chatID;
    }

    public void reason(String s) {
        if (graphic) {
            gui.say("** " + s);
        }
    }

    public void say(String s) {
        if (graphic) {
            gui.say(s);
        } else {
            System.out.println("[" + name + "] " + s);
        }
    }

    public void say(String s, boolean t) {
        if (graphic) {
            gui.say(s);
            if (t) {
                System.out.println("[" + name + "] " + s);
            }
        } else {
            System.out.println("[" + name + "] " + s);
        }
    }

    public void printChat(AID player, String s) {
        if (graphic) {
            int i = 0;
            for (Player p : players) {
                if (p.getAID().equals(player)) {
                    gui.appendChat(p.getName(), s, i);
                    return;
                }
                ++i;
            }
            if (player.equals(mazziereAID)) {
                gui.appendChat(briscola.common.Names.MAZZIERE, s, i);

                return;
            }
            gui.appendChat(briscola.common.Names.UNKNOWN, s, ++i);
        }
    }

    /**
     * sends to all players a message with CFP: ACL_CHAT CONV.ID: As sent by
     * mazziere
     *
     * @param s message to be sent
     */
    public void sendChat(String s) {
        ACLMessage m = new ACLMessage(briscola.common.ACLCodes.ACL_CHAT);
        m.setConversationId(getChatID());
        m.setContent(s);
        if (players == null) {
            say("Nessun giocatore in lista");
        } else {
            for (Player p : players) {
                m.addReceiver(p.getAID());
            }
            send(m);
        }
    }

//    public void sendMessage(List<Player> rcp, int type, String content) {
//        ACLMessage m = new ACLMessage(type);
//        for (Player p : rcp) {
//            m.addReceiver(p.getAID());
//        }
//        m.setContent(content);
//        m.setPerformative(type);
//        send(m);
//    }
//    public void sendMessage(Player rcp, int type, String content) {
//        ACLMessage m = new ACLMessage(type);
//        m.addReceiver(rcp.getAID());
//        m.setContent(content);
//        m.setPerformative(type);
//        send(m);
//    }
    public void sendMessage(List<Player> rcp, int type, String content)
        throws IOException {
        ACLMessage m = new ACLMessage(type);
        for (Player p : rcp) {
            m.addReceiver(p.getAID());
        }
        m.setContent(content);
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(AID rcp, int type, String content) {
        ACLMessage m = new ACLMessage(type);
        m.addReceiver(rcp);
        m.setContent(content);
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(AID rcp, int type, Serializable content) {
        ACLMessage m = new ACLMessage(type);
        m.addReceiver(rcp);
        try {
            m.setContentObject(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        m.setPerformative(type);
        send(m);
    }

    public void sendMessage(List<Player> rcp, int type, Serializable content)
        throws IOException {
        ACLMessage m = new ACLMessage(type);
        for (Player p : rcp) {
            m.addReceiver(p.getAID());
        }
        m.setContentObject(content);
        m.setPerformative(type);
        send(m);
    }

    synchronized public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void assertFact(Fact f) throws JessException {
        rete.assertFact(f);
    }

//    public void sendMessage(Player rcp, int type, Serializable content) throws
//        IOException {
//        ACLMessage m = new ACLMessage(type);
//        m.addReceiver(rcp.getAID());
//        m.setContentObject(content);
//        m.setPerformative(type);
//        send(m);
//    }
    @Override
    public String toString() {
        return name + "[" + getAID() + "]";
    }

    public Rete getRete() {
        return rete;
    }

    @Override
    public void addBehaviour(Behaviour b) {
        super.addBehaviour(b);
        this.behaviours.add(b);
    }

    synchronized public void removeAllBehaviours() {
        for (Behaviour b : behaviours) {
            removeBehaviour(b);
        }
    }

    synchronized public void clearMessageQueue() {
        int c = 0;
        say("Clearing message queue");
        while (receive() != null)
            ++c;
        say(c + " messages ignored ");
    }
}
