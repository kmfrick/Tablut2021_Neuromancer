package it.unibo.ai.didattica.competition.tablut.client;

import aima.core.search.adversarial.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class TablutNeuroBlackClient {

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        System.out.println("NeuroBlackClient::main() called");
        String[] array = new String[]{"BLACK"};
        if (args.length > 3){
            array = new String[]{args[0], "BLACK", args[1], args[2], args[3]};
        } else {
            System.err.println("Not enought arguments!");
            System.exit(1);
        }
        TablutNeuroClient.main(array);
    }

}
