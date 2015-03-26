/*
 */
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;

/**
 * Behaviour the mazziere keeps while he's waiting for 5 players to join the
 * table
 */
public class OpenTable extends Behaviour {

    private static final long serialVersionUID = 1L;

    private Integer requests;
    private final HashMap<AID, Long> requesters;
    private final MazziereAgent mazziere;

    public OpenTable(MazziereAgent mazziere) {
        this.mazziere = mazziere;
        mazziere.say("Preparo e registro subito il tavolo");
        requesters = new HashMap<>();
        requests = 0;
    }

    synchronized public int getRequests() {
        return requests;
    }

    synchronized public void augmentReq() {
        ++requests;
    }

    synchronized public void diminishReq() {
        --requests;
    }

    @Override
    public void action() {

        if (getRequests() + ((MazziereAgent) myAgent).getPlayersAID().size() < 5) {
            MessageTemplate request = MessageTemplate.MatchContent(
                briscola.common.Messages.CAN_I_PLAY);
            ACLMessage requestMsg = myAgent.receive(request);

            if (requestMsg != null) {
                AID agentName = requestMsg.getSender();
                if (((MazziereAgent) myAgent).getPlayersAID().contains(agentName)) {
                    mazziere.say(
                        "Agente " + agentName.getName() + " già iscritto");
                    return;
                }
                Long reqTime = requesters.get(agentName);
                //  se agente sconosciuto
                if (reqTime == null || ((System.currentTimeMillis() - reqTime) > briscola.common.Names.WAIT_FOR_CONFIRMATION)) {
                    requesters.put(agentName, System.currentTimeMillis());
                    myAgent.addBehaviour(new OfferAChair(this, mazziere,
                                                         requestMsg,
                                                         agentName));
                }

            } else {
                block();
            }
        } else {
            mazziere.say("Siamo già a 5 richieste..");
            block();
        }
    }

    @Override
    public boolean done() {
        if (((MazziereAgent) myAgent).getPlayersAID().size() == 5) {
            mazziere.getDFA().removeServices(mazziere.getServiceDesc());
            mazziere.say("Tavolo al completo");
            myAgent.addBehaviour(new BeginGame(mazziere));
            return true;
        }
        return false;
    }

}
