package it.unibo.ai.didattica.competition.tablut.client;

import aima.core.search.adversarial.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class TablutNeuroBlackClient {

    public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
        System.out.println("NeuroBlackClient::main() called");
        String[] array = new String[]{"BLACK"};
        if (args.length>0){
            array = new String[]{"BLACK", args[0]};
        }
        TablutNeuroClient.main(array);
    }

}