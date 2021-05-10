package it.unibo.ai.didattica.competition.tablut.client;

//file management for genetic algorithm
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.solve.TablutMinimax;
import it.unibo.ai.didattica.competition.tablut.solve.Heuristic;
import it.unibo.ai.didattica.competition.tablut.solve.WhiteHeuristic;
import it.unibo.ai.didattica.competition.tablut.solve.BlackHeuristic;

/**
 * 
 * @author A. Piretti, Andrea Galassi
 *
 */
public class TablutNeuroClient extends TablutClient {

	//file management for genetic algorithm
	//TODO: Comment when deliver the application
	protected static final String outFilePath = "/Users/kmfrick/Documents/Code/Tablut2021_Neuromancer/evolution_out.txt";
	private static FileWriter myWriter;
	private final static int WIN = 0, LOSE = 1, DRAW = 2;
	private static int matchResult;
	static double[] whiteHeuristicWeights;
	static double[] blackHeuristicWeights;
	/*
		try {
		  FileWriter myWriter = new FileWriter("filename.txt");
		  myWriter.write("Files in Java might be tricky, but it is fun enough!");
		  myWriter.close();
		  System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
		  System.out.println("An error occurred.");
		  e.printStackTrace();
		}
	 */


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

		//file management for genetic algorithm
		try {
			myWriter = new FileWriter(outFilePath, true);
			matchResult = -1;
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		whiteHeuristicWeights = WhiteHeuristic.getWhiteWeights();
		blackHeuristicWeights = BlackHeuristic.getBlackWeights();

		client.run();

	}


	@Override
	public void run() {
		System.out.println("You are player " + this.getPlayer().toString() + "!");

		if(this.getPlayer().toString().equals("B")){
			BlackHeuristic.setWeightsAfterGenetic();
		} else{
			WhiteHeuristic.setWeightsAfterGenetic();
		}

		//Heuristic.setWeightsAfterGenetic();
		String actionStringFrom = "";
		String actionStringTo = "";
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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

		// still alive until you are playing
		while (true) {

			// update the current state from the server
			try {
				this.read();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			}

			// print current state
			System.out.println("Current state:");
			state = this.getCurrentState();
			System.out.println(state.toString());



			// if i'm WHITE
			if (this.getPlayer().equals(State.Turn.WHITE)) {

				// if is my turn (WHITE)
				if (state.getTurn().equals(StateTablut.Turn.WHITE)) {

					System.out.println("\nSearching a suitable move... ");

					// search the best move in search tree
					Action a = search.makeDecision(state);

					System.out.println("\nAction selected: " + a.toString());
					try {
						this.write(a);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}

				}

				// if is turn of oppenent (BLACK)
				else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
					System.out.println("Waiting for your opponent move...\n");
				}
				// if I WIN
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("YOU WIN!");

					try {
						myWriter.write("WIN;");
						for(double peso : whiteHeuristicWeights){
							myWriter.write(peso + ";");
						}
						myWriter.write("\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file. " + matchResult);
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}

					System.exit(0);
				}
				// if I LOSE
				else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("YOU LOSE!");

					try {
						myWriter.write("LOSE;");
						for(double peso : whiteHeuristicWeights){
							myWriter.write(peso + ";");
						}
						myWriter.write("\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file. " + matchResult);
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}

					System.exit(0);
				}
				// if DRAW
				else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");

					try {
						myWriter.write("DRAW;");
						for(double peso : whiteHeuristicWeights){
							myWriter.write(peso + ";");
						}
						myWriter.write("\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file. " + matchResult);
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}

					System.exit(0);
				}

			}
			// if i'm BLACK
			else {

				// if is my turn (BLACK)
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {

					System.out.println("\nSearching a suitable move... ");

					// search the best move in search tree
					
					Action a = search.makeDecision(state);

					System.out.println("\nAction selected: " + a.toString());
					try {
						this.write(a);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}

				}

				// if is turn of oppenent (WHITE)
				else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
					System.out.println("Waiting for your opponent move...\n");
				}

				// if I LOSE
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					matchResult = LOSE; //genetic
					System.out.println("YOU LOSE!");
					try {
						myWriter.write("LOSE;");
						for(double peso : blackHeuristicWeights){
							myWriter.write(peso + ";");
						}
						myWriter.write("\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file. " + matchResult);
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					System.exit(0);
				}

				// if I WIN
				else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("YOU WIN!");

					try {
						myWriter.write("WIN;");
						for(double peso : blackHeuristicWeights){
							myWriter.write(peso + ";");
						}
						myWriter.write("\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file. " + matchResult);
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					System.exit(0);
				}

				// if DRAW
				else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
					matchResult = DRAW; //genetic
					System.out.println("DRAW!");
					try {
						myWriter.write("DRAW;");
						for(double peso : blackHeuristicWeights){
							myWriter.write(peso + ";");
						}
						myWriter.write("\n");
						myWriter.close();
						System.out.println("Successfully wrote to the file. " + matchResult);
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					System.exit(0);
				}


			}
		}

	}
}
