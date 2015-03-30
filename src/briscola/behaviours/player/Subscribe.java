/*
 */
package briscola.behaviours.player;

import briscola.PlayerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Subscribe extends Behaviour {

    private static final long serialVersionUID = 1L;

    /**
     * STATI. 0 nessun tavolo libero. 1 trovato tavolo libero. 2 richiesta
     * spedita a tavolo libero. 3 partecipazione confermata
     *
     */
    private Short state;
    private final PlayerAgent player;

    public Subscribe(PlayerAgent player) {
        state = 0;
        this.player = player;
    }
    DFAgentDescription template = new DFAgentDescription();
    ServiceDescription sd = new ServiceDescription();
    AID[] mazzieri;
    FindTable findBeh;
    ACLMessage responseMsg = null;
    ACLMessage confirmMsg = null;

    /**
     * get list of mazzieri from yellow pages sends all a "can I join?" request
     */
    public void requestAll() {
        ACLMessage request = new ACLMessage(ACLMessage.QUERY_IF);
        request.setContent(briscola.common.Messages.CAN_I_PLAY);

        sd.setType(briscola.common.Names.MAZZIERE);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            for (int i = 0; i < result.length; ++i) {
                request.addReceiver(result[i].getName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        myAgent.send(request);
    }

    @Override
    public boolean done() {
        if (state == 3) {
            player.addBehaviour(new BeginGame(player));
            return true;
        }
        return false;
    }

    public void sendReply(ACLMessage responseMsg, ACLMessage confirmMsg) {
        try {

            confirmMsg = responseMsg.createReply();
            confirmMsg.setContent(player.getRealName());
            confirmMsg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            myAgent.send(confirmMsg);
            state = 2;
        } catch (Exception e) {
            e.printStackTrace();
            state = 0;
        }
    }

    public void getReply(ACLMessage responseMsg, ACLMessage confirmMsg) {
        String content = myAgent.getName() + briscola.common.Messages.CONFIRM_TABLE;

        MessageTemplate confirmation = MessageTemplate.MatchContent(content);
        confirmMsg = myAgent.receive(confirmation);
        if (confirmMsg != null) {
            player.say(
                "Iscritto alla partita col mazziere " + confirmMsg.getSender().getName());
            player.setMazziereAID(confirmMsg.getSender());
            // todo: remove
            player.clearMessageQueue();
            state = 3;
        } else {
            player.say("Messaggio conferma non trovato");
            state = 2;
            block();
        }
    }

    @Override
    public void action() {
        switch (state) {
            case 0:
                if (findBeh == null) {
                    findBeh = new FindTable(myAgent,
                                            briscola.common.Names.RETRY_EVERY,
                                            state);
                    player.addBehaviour(findBeh);
                }
                break;
            case 1:
                findBeh.stop();
                sendReply(responseMsg, confirmMsg);
                break;
            case 2:
                getReply(responseMsg, confirmMsg);
                break;
            default:
                break;
        }
    }

    private class FindTable extends TickerBehaviour {

        private static final long serialVersionUID = 1L;

        Short state;
        ACLMessage responseMsg;
        ACLMessage confirmMsg = null;
        MessageTemplate response;

        public FindTable(Agent a, long period, Short state) {
            super(a, period);
            this.state = state;
            response = MessageTemplate.MatchContent(
                briscola.common.Messages.YOU_CAN_PLAY);
        }

        @Override
        public void onStart() {
            requestAll();
        }

        @Override
        protected void onTick() {

            responseMsg = myAgent.receive(response);
            if (responseMsg != null) {
                player.say("Trovato un tavolo libero");
                state = 1;
                sendReply(responseMsg, confirmMsg);
                this.stop();
            } else {
                player.say("Nessun tavolo libero. Riprovo...");
                block();
            }

            requestAll();
        }

    }

}
