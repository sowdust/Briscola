/*
 */
package briscola.behaviours;

import briscola.Player;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendMessage extends Behaviour {

    protected final List<Player> rcp;
    protected final int type;
    protected String content = null;
    protected Object ob = null;
    protected String convId = null;

    public SendMessage(List<Player> rcp, String content)
    {
        this.rcp = rcp;
        this.type = ACLMessage.UNKNOWN;
        this.content = content;
    }

    public SendMessage(List<Player> rcp, int type, String content)
    {
        this.rcp = rcp;
        this.type = type;
        this.content = content;
    }

    public SendMessage(List<Player> rcp, int type, Serializable content)
    {
        this.rcp = rcp;
        this.type = type;
        this.ob = content;
    }

    public SendMessage(Player rcp, String content)
    {
        List<Player> l = new ArrayList<>();
        l.add(rcp);
        this.rcp = l;
        this.type = ACLMessage.UNKNOWN;
        this.content = content;
    }

    public SendMessage(Player rcp, int type, String content)
    {
        List<Player> l = new ArrayList<>();
        l.add(rcp);
        this.rcp = l;
        this.type = type;
        this.content = content;
    }

    public void setConvId(String convId)
    {
        this.convId = convId;
    }

    @Override
    public void action()
    {
        ACLMessage m = new ACLMessage(type);
        for (Player p : rcp)
        {
            m.addReceiver(p.getAID());
        }

        if (ob == null)
        {
            m.setContent(content);
        } else
        {
            try
            {
                m.setContentObject((Serializable) ob);
            } catch (Exception e)
            {
                System.out.println(e);
            }
        }
        m.setPerformative(type);
        if (convId != null)
        {
            m.setConversationId(convId);
        }
        myAgent.send(m);
    }

    @Override
    public boolean done()
    {
        return true;
    }
}
