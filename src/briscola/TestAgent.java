/*
 */
package briscola;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jess.*;

/* SULLA RICEZIONE MESSAGGI BLOCCANTI
 blockingReceive() methods actually blocks the agent thread:
 if you call blockingReceive() from within a behaviour, this prevents all other behaviours
 A good  programming practice: use blockingReceive() in the setup() and takeDown()
 use receive() in combination with Behaviour.block() within behaviours.
 */
public class TestAgent extends Agent {

    private String argomento;
    private String fileName = "/home/mat/school/Tesi/src/briscola/reasoner/test.clp";

    //  inizializzazione
    @Override
    protected void setup() {
        //  my name
        System.out.println("Agente " + getAID().getName() + " attivo!");

        //  get arguments via command line
        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            argomento = (String) args[0];
            System.out.println("Argomento passato: " + argomento);
            System.out.println("Rimango attivo");
        } else {
            System.out.println("Nessun argomento da linea di comando");
            System.out.println("Me ne dovrei andare.");
            doDelete();
        }

        System.out.println("Eseguendo JESS file...");
        Rete r = new Rete();
        try {
            r.batch(fileName);
            r.reset();
            r.run();
        } catch (JessException ex) {
            ex.printStackTrace();
        }

    }

    //  in uscita
    @Override
    protected void takeDown() {
        System.out.println("Agente " + getAID().getName() + " se ne sta andando");

    }

    private class TestBehaviour extends CyclicBehaviour {

        //  Sintassi da usare per ricevere messaggi
        //  il behaviour è riattivato dal "block" non appena
        //  l'agente riceve un nuovo messaggio
        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                // Message received. Process it
            } else {
                block();
            }
        }
    }

    private class ReadOnlyCertainMessagesBehaviour extends Behaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(
                ACLMessage.CFP);
            // riceve solo il primo messaggio (se esiste) che rispetta il template mt.
            // Ignora gli eventuali altri
            ACLMessage msg = myAgent.receive(mt);
            //             = myAgent.blockingReceive(mt);
            if (msg != null) {
                // CFP Message received. Process it
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

}
