package it.unibo.ai.didattica.competition.tablut.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class TablutNeuroWhiteClient {

	public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
		System.out.println("NeuroWhiteClient::main() called");
		String[] array = new String[]{"WHITE"};
		TablutNeuroClient.main(array);
	}
	
}
