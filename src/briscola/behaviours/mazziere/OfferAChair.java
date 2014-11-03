/*
 */
package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class OfferAChair extends OneShotBehaviour {

    AID requester;
    Integer requests;
    ACLMessage originalMsg;
    MazziereAgent mazziere;

    public OfferAChair(MazziereAgent mazziere, ACLMessage originalMsg, AID agent, Integer requests) {
        this.requester = agent;
        this.requests = requests;
        this.originalMsg = originalMsg;
        this.mazziere = mazziere;
    }

    @Override
    public void action() {
        mazziere.say("Posizione offerta a " + requester.getName());
        ++requests;
        ACLMessage proposal = originalMsg.createReply();
        proposal.setContent(briscola.common.Messages.YOU_CAN_PLAY);
        myAgent.send(proposal);
        myAgent.addBehaviour(new WaitForSubscriptionConfirmation(mazziere, requests, requester));
    }

}
