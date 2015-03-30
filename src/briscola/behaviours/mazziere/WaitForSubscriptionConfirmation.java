/*
 */
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.MatchPerformative;

/**
 * Behaviours used by Mazziere.
 *
 * @author mat
 */
public class WaitForSubscriptionConfirmation extends Behaviour {

    private static final long serialVersionUID = 1L;

    private final long start;
    private boolean done;
    private final AID agent;
    private final MazziereAgent mazziere;
    private final OpenTable behav;

    public WaitForSubscriptionConfirmation(OpenTable behav,
                                           MazziereAgent mazziere,
                                           AID agent) {
        this.behav = behav;
        this.start = System.currentTimeMillis();
        this.mazziere = mazziere;
        this.agent = agent;
    }

    @Override
    public void action() {

        MessageTemplate confirmation = MessageTemplate.and(
            MessageTemplate.MatchSender(agent), MatchPerformative(
                ACLMessage.ACCEPT_PROPOSAL));

        ACLMessage confirmationMsg = myAgent.receive(confirmation);
        if (confirmationMsg != null) {
            behav.diminishReq();
            ((MazziereAgent) myAgent).addPlayer(agent,
                                                confirmationMsg.getContent());
            ACLMessage confirmTable = confirmationMsg.createReply();
            String content = agent.getName() + briscola.common.Messages.CONFIRM_TABLE;
            confirmTable.setContent(content);
            myAgent.send(confirmTable);
            mazziere.say(
                "Aggiunto giocatore # " + ((MazziereAgent) myAgent).getPlayersAID().size() + ": " + agent.getName());
            done = true;
            done();
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        if (done) {
            ((MazziereAgent) myAgent).getMemory().setPlayers(
                ((MazziereAgent) myAgent).getPlayers());
            return true;
        }
        if ((System.currentTimeMillis() - start) > briscola.common.Names.WAIT_FOR_CONFIRMATION) {
            behav.diminishReq();
            return true;
        }
        return false;
    }

}
