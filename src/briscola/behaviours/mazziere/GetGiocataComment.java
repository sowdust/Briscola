package briscola.behaviours.mazziere;

import briscola.MazziereAgent;
import briscola.messages.GiocataCommentMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class GetGiocataComment extends CyclicBehaviour {

    private static final long serialVersionUID = 1L;

    private final MazziereAgent mazziere;

    public GetGiocataComment(MazziereAgent mazziere) {
        this.mazziere = mazziere;
    }

    @Override
    public void action() {

        MessageTemplate info = MessageTemplate.MatchPerformative(
            briscola.common.ACLCodes.ACL_COMMENT_GIOCATA);

        ACLMessage chatMsg = myAgent.receive(info);
        if (chatMsg != null) {
            mazziere.say(
                "Ricevuto commento alla giocata da parte di " + chatMsg.getSender());
            try {
                GiocataCommentMessage m = (GiocataCommentMessage) chatMsg.getContentObject();

                mazziere.getMemory().addGiocataComment(m.giocata,
                                                       m.from + ": " + m.commento);
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        } else {
            block();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

}
