package it.unibo.ai.didattica.competition.tablut.client;

import aima.core.search.adversarial.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class TablutNeuroWhiteClient {

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
		System.out.println("NeuroWhiteClient::main() called");
		String[] array = new String[]{"WHITE"};
		if (args.length>0){
      array = new String[]{"WHITE", args[0], args[1]};
    }
		TablutNeuroClient.main(array);
	}

}
