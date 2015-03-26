/*
 */
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class OfferAChair extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    AID requester;
    ACLMessage originalMsg;
    MazziereAgent mazziere;
    OpenTable behav;

    public OfferAChair(OpenTable behav, MazziereAgent mazziere,
                       ACLMessage originalMsg, AID agent) {
        this.requester = agent;
        this.behav = behav;
        this.originalMsg = originalMsg;
        this.mazziere = mazziere;
    }

    @Override
    public void action() {
        mazziere.say("Posizione offerta a " + requester.getName());
        behav.augmentReq();
        ACLMessage proposal = originalMsg.createReply();
        proposal.setContent(briscola.common.Messages.YOU_CAN_PLAY);
        myAgent.send(proposal);
        myAgent.addBehaviour(
            new WaitForSubscriptionConfirmation(behav, mazziere,
                                                requester));
    }

}
