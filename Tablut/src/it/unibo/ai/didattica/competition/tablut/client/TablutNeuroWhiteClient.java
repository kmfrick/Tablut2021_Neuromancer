package it.unibo.ai.didattica.competition.tablut.client;

import aima.core.search.adversarial.*;

import java.io.IOException;
import java.net.UnknownHostException;
import it.unibo.ai.didattica.competition.tablut.domain.*;

public class TablutNeuroWhiteClient {

	private final double UTIL_MAX = 1000;
	private final int TIME_SEC = 10;

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
		
		var search = createFor(GameAshtonTablut, 0, UTIL_MAX, TIME_SEC);
		System.out.println("NeuroWhiteClient::main() called");
		String[] array = new String[]{"WHITE"};
		TablutNeuroClient.main(array);
	}
	
}
