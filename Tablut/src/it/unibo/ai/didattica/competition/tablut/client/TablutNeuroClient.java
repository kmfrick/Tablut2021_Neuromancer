package it.unibo.ai.didattica.competition.tablut.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.solve.TablutMinimax;

/**
 * 
 * @author A. Piretti, Andrea Galassi
 *
 */
public class TablutNeuroClient extends TablutClient {


	private final double UTIL_MAX = 1000;
	private final int TIME_SEC = 10;

	private TablutMinimax search;
	private int game;

	public TablutNeuroClient(String player, int game) throws UnknownHostException, IOException {
		super(player, "Neuromancer");
		this.game = game;
	}

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		int gametype = 4;
		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected client: " + args[0]);

		TablutClient client = new TablutNeuroClient(args[0], gametype);
		client.run();

	}


	@Override
	public void run() {
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		String actionStringFrom = "";
		String actionStringTo = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Action action = null;
		Game rules = null;
		State state = null;
		switch (this.game) {
			case 1:
				state = new StateTablut();
				rules = new GameTablut();
				break;
			case 2:
				state = new StateTablut();
				rules = new GameModernTablut();
				break;
			case 3:
				state = new StateBrandub();
				rules = new GameTablut();
				break;
			case 4:
				state = new StateTablut();
				state.setTurn(State.Turn.WHITE);
				rules = new GameAshtonTablut(99, 0, "logs", "fake", "fake");
				System.out.println("Ashton Tablut game");
				break;
			default:
				System.out.println("Error in game selection");
				System.exit(4);
		}
		
		search = new TablutMinimax(rules);


		if (this.getPlayer() == State.Turn.WHITE) {
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			while (true) {
				try {
					this.read();

					if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
						long startTime = System.currentTimeMillis();
						Action a = search.makeDecision(state);
						long estimatedTime = System.currentTimeMillis() - startTime;
						System.out.println("Played move: " + a.toString());
						System.out.println("Time to find move: " + estimatedTime/1000.0 + " seconds");
						this.write(action);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
						System.out.println("YOU WIN!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
						System.out.println("YOU LOSE!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
						System.out.println("DRAW!");
						System.exit(0);
					}

				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		} else {
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			while (true) {
				try {
					this.read();
					if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						long startTime = System.currentTimeMillis();
						Action a = search.makeDecision(state);
						long estimatedTime = System.currentTimeMillis() - startTime;
						System.out.println("Played move: " + a.toString());
						System.out.println("Time to find move: " + estimatedTime/1000.0 + " seconds");
						this.write(action);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
						System.out.println("YOU LOSE!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
						System.out.println("YOU WIN!");
						System.exit(0);
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
						System.out.println("DRAW!");
						System.exit(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}

}
