import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. It is used to model a
 *  Big Two card game that supports 4 players playing over the internet.
 * 
 * @author reynardverill
 */
public class BigTwoClient implements CardGame , NetworkGame {
	
	/**
	 * a constructor for creating a Big Two client. 
	 * You should (i) create 4 players and add them to the list of players; 
	 * (ii) create a Big Two table which builds the GUI for the game and handles user actions; 
	 * and (iii) make a connection to the game server by calling the makeConnection() method from the NetworkGame interface.
	 */
	BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		this.numOfPlayers = playerList.size();
		
		handsOnTable = new ArrayList<Hand>();
		this.setServerIP("127.0.0.1");
		this.setServerPort(2396);
		String name = JOptionPane.showInputDialog("Please enter your name:");
		while (name.isEmpty()) {
			name = JOptionPane.showInputDialog("Please enter a valid name:");
		}
		this.setPlayerName(name);
		table = new BigTwoTable(this);
		makeConnection();
	}
	
	/**
	 * a boolean value of isConnected.
	 */
	private boolean isConnected;
	/**
	 * a boolean value of isStarted.
	 */
	private boolean isStarted;
	/**
	 * an integer specifying the number of players.
	 */
	private int numOfPlayers;
	/**
	 * a deck of cards.
	 */
	private Deck deck;
	/**
	 * a list of players.
	 */
	private ArrayList<CardGamePlayer> playerList;
	/**
	 * a list of hands played on the table.
	 */
	private ArrayList<Hand> handsOnTable;
	/**
	 * an integer specifying the playerID (i.e., index) of the local player.
	 */
	private int playerID;
	/**
	 *  a string specifying the name of the local player.
	 */
	private String playerName;
	/**
	 *  a string specifying the IP address of the game server.
	 */
	private String serverIP;
	/**
	 * an integer specifying the TCP port of the game server.
	 */
	private int serverPort;
	/**
	 * a socket connection to the game server.
	 */
	private Socket sock;
	/**
	 * an ObjectOutputStream for sending messages to the server.
	 */
	private ObjectOutputStream oos;
	/**
	 * an integer specifying the index of the player for the current turn.
	 */
	private int currentIdx;
	/**
	 * a Big Two table which builds the GUI for the game and handles all user actions.
	 */
	private BigTwoTable table;
	
	/**
	 * a method to set the boolean value of isConnected.
	 * 
	 * @param connected a boolean value to be set into isConnected.
	 */
	public void setIsConnected(boolean connected) {
		this.isConnected = connected;
	}
	
	/**
	 * a method to retrieve the value of isConnected of this player.
	 * 
	 * @return a boleean value of isConnected.
	 */
	public boolean getIsConnected() {
		return this.isConnected;
	}
	/**
	 * a method to set the value of isStarted
	 * 
	 * @param a a boolean to be set into isStarted.
	 */
	public void setIsStarted(boolean a) {
		isStarted = a;
	}
	
	/**
	 * a method to retrieve the value of isStarted.
	 * 
	 * @return isStarted a boolean value to check whether the game has started or not.
	 */
	public boolean getIsStarted() {
		return this.isStarted;
	}
	/**
	 * a method for getting the number of players.
	 * 
	 * @return numOfPlayers to get the number of players playing the card game.
	 */
	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return this.numOfPlayers;
	}

	/**
	 * a method for getting the deck of cards being used.
	 * 
	 * @return deck to get the deck used in this game.
	 */
	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return this.deck;
	}

	/**
	 * a method for getting the list of players.
	 * 
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return this.playerList;
	}

	/**
	 * a method for getting the list of hands played on the table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return this.handsOnTable;
	}

	/**
	 * a method for getting the index of the player for the current turn.
	 */
	@Override
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return this.currentIdx;
	}

	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards
	 * 
	 * @param deck to get the initialized and shuffeled deck from server.
	 */
	@Override
	public void start(Deck deck) {
		table.clearMsgArea();
		table.printMsg("All players are ready. Game starts." + '\n');
		/**
		 * (i) remove all the cards from the players as well as from the table.
		 */
		for (int i = 0; i<this.getNumOfPlayers(); i++) {
			this.getPlayerList().get(i).removeAllCards();
		}
		this.handsOnTable.clear();
		
		/**
		 * (ii) distribute the cards to the players and (iii) identify the player who holds the 3 of Diamonds
		 */
		
		//distribute the cards in the deck to the players using nested for loop
		for (int i = 0; i<deck.size()/this.getPlayerList().size(); i++) {
			for (int j = 0; j<this.getPlayerList().size(); j++) {
				/**
				 * (iv) set the currentIdx of the BigTwoClient instance to the playerID (i.e., index) of the player who holds the 3 of Diamonds
				 */
				if (deck.getCard(i*this.getPlayerList().size() + j).getRank() == 2 && deck.getCard(i*this.getPlayerList().size() + j).getSuit() == 0) {
					this.currentIdx = j;
				}
				this.getPlayerList().get(j).addCard(deck.getCard((i)*this.getPlayerList().size() + j));
			}
		}
	
		//sort the cards that the players have by using the sort method for each player
		for (int i = 0; i<getPlayerList().size(); i++) {
			this.getPlayerList().get(i).sortCardsInHand();
		}
		
		/**
		 * (v) set the activePlayer of the BigTwoTable instance to the playerID (i.e., index) of the local player (i.e., only shows the cards of the local player 
		 * and the local player can only select cards from his/her own hand)
		 */
		table.setActivePlayer(this.playerID);
	}

	/**
	 * a method for making a move by a player with the specified playerID using the cards specified by the list of indices. This method should be called from the 
	 * BigTwoTable when the local player presses either the “Play” or “Pass” button.
	 * 
	 * @param playerID an integer specifying the player ID which makes the move.
	 * @param cardIdx an array of integers containing the locations of the cards which are selected by the player.
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		CardGameMessage move = new CardGameMessage(6, -1, cardIdx);
		sendMessage(move);
	}

	/**
	 * a method for checking a move made by a player. This method should be called from the parseMessage() method from the NetworkGame interface when a message of 
	 * the type MOVE is received from the game server.
	 * 
	 * @param playerID an integer specifying the player ID.
	 * @param cardIdx an array of integers containing the locations of the cards which are selected by the player.
	 */
	@Override
	public synchronized void checkMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		CardList cards = new CardList();
		if (cardIdx == null) {
			if (handsOnTable.size()!=0 && handsOnTable.get(handsOnTable.size()-1).getPlayer() != this.getPlayerList().get(this.currentIdx)) {
				this.currentIdx = (this.getCurrentIdx()+1)%4;
				table.printMsg("[pass]" + '\n');
				if (this.getCurrentIdx() == table.getActivePlayer()) {
					table.printMsg("Your turn:" + '\n');
				}
				else {
					table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName() + "'s turn:" + '\n');
				}
				table.resetSelected();
				table.repaint();
			}
			else {
				table.printMsg("Not a legal move!!!" +'\n');
			}
		}
		else {
			for (int i = 0; i<cardIdx.length; i++) {
				cards.addCard(this.getPlayerList().get(playerID).getCardsInHand().getCard(cardIdx[i]));
			}
			Hand hand = BigTwoClient.composeHand(this.getPlayerList().get(playerID), this.getPlayerList().get(playerID).play(cardIdx));
			if (hand != null) {
				if (handsOnTable.size() == 0) {
					if (hand.contains(this.getPlayerList().get(currentIdx).getCardsInHand().getCard(0)) && hand.isValid()) {
						this.handsOnTable.add(hand);
						this.getPlayerList().get(playerID).removeCards(hand);
						this.currentIdx = (this.getCurrentIdx()+1)%4;
						if (this.getCurrentIdx() == table.getActivePlayer()) {
							table.printMsg(hand.getType() + " " + hand.toString() + '\n' + "Your turn:" + '\n');
						}
						else {
							table.printMsg(hand.getType() + " " + hand.toString() + '\n' + this.getPlayerList().get(this.getCurrentIdx()).getName() + "'s turn:" + '\n');
						}
						table.resetSelected();
						table.repaint();
					}
					else {
						table.printMsg("Not a legal move!!!" + '\n');
					}
				}
				else if (hand.getPlayer() == handsOnTable.get(handsOnTable.size()-1).getPlayer()) {
					if (hand.isValid()) {
						this.handsOnTable.add(hand);
						this.getPlayerList().get(playerID).removeCards(hand);
						if (this.endOfGame()) {
							table.printMsg( '\n' + "Game has ended!!! Thanks for playing!!!");
							table.resetSelected();
							table.repaint();
						}
						else {
							table.printMsg(hand.getType() + " " + hand.toString() + '\n');
							this.currentIdx = (this.getCurrentIdx()+1)%4;
							table.resetSelected();
							if (this.getCurrentIdx() == table.getActivePlayer()) {
								table.printMsg("Your turn:" + '\n');
							}
							else {
								table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName() + "'s turn:" + '\n');
							}
							table.repaint();
						}
					}
					else {
						table.printMsg(" Not a legal move!!!" + '\n');
					}
				}
				else if (hand.size() == handsOnTable.get(handsOnTable.size()-1).size() && hand.isValid() && hand.beats(handsOnTable.get(handsOnTable.size()-1))) {
					this.handsOnTable.add(hand);
					this.getPlayerList().get(playerID).removeCards(hand);
					if (this.endOfGame()) {
						table.printMsg("Game has ended!!! Thanks for playing!!!");
						table.resetSelected();
						table.repaint();
					}
					else {
						this.currentIdx = (this.getCurrentIdx()+1)%4;
						if (this.getCurrentIdx() == table.getActivePlayer()) {
							table.printMsg(hand.getType() + " " + hand.toString() + '\n' + "Your turn:" + '\n');
						}
						else {
							table.printMsg(hand.getType() + " " + hand.toString() + '\n' + this.getPlayerList().get(this.getCurrentIdx()).getName() + "'s turn:" + '\n');
						}
						table.resetSelected();
						table.repaint();
					}
				}
				else {
					table.printMsg("Not a legal move!!!" + '\n');
				}
			}
			else {
				table.printMsg("Not a legal move!!!" + '\n');
			}
		}
	}

	/**
	 * a method for checking if the game ends.
	 */
	@Override
	public boolean endOfGame() {
		if (this.getIsStarted()) {
			// TODO Auto-generated method stub
			for (int i = 0; i<this.getNumOfPlayers(); i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 * 
	 * @return playerID the playerID of the player.
	 */
	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return this.playerID;
	}

	/**
	 * a method for setting the playerID (i.e., index) of the local player. This method should be called from the parseMessage() method when a message of 
	 * the type PLAYER_LIST is received from the game server.
	 * 
	 * @param playerID integer specifying the player ID.
	 */
	@Override
	public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID = playerID;
		
	}

	/**
	 * a method for getting the name of the local player.
	 * 
	 * @return playerName a string of the player name.
	 */
	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return this.playerName;
	}

	/**
	 * a method for setting the name of the local player.
	 * 
	 * @param playerName a string of the player name.
	 */
	@Override
	public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName = playerName;
		
	}

	/**
	 * a method for getting the IP address of the game server.
	 */
	@Override
	public String getServerIP() {
		// TODO Auto-generated method stub
		return this.serverIP;
	}

	/**
	 * a method for setting the IP address of the game server.
	 * 
	 * @param serverIP a string specifying the location of the IP (IP address).
	 */
	@Override
	public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP = new String(serverIP);
		
	}

	/**
	 * a method for getting the TCP port of the game server.
	 * 
	 * @return an integer of the server port.
	 */
	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return this.serverPort;
	}

	/**
	 * a method for setting the TCP port of the game server.
	 * 
	 * @param serverPort an integer specifying the server port.
	 */
	@Override
	public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort = serverPort;
		
	}

	/**
	 *  method for making a socket connection with the game server.
	 */
	@Override
	public void makeConnection() {
		// TODO Auto-generated method stub
		
		try {
			if(this.getIsConnected()) {
				table.printMsg("You are already connected!" + '\n');
			} else {
				sock = new Socket(this.getServerIP(), this.getServerPort());
				oos = new ObjectOutputStream(sock.getOutputStream());
				Thread connection = new Thread(new ServerHandler());
				connection.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * a method for parsing the messages received from the game server. This method should be called from the thread responsible for receiving messages from the game server. Based on the message type, 
	 * different actions will be carried out (please refer to the general behavior of the client described in the previous section).
	 * 
	 * @param message to parse the message broadcasted from the server.
	 *
	 */
	@Override
	public void parseMessage(GameMessage message) {
		// TODO Auto-generated method stub
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			String[] data = (String[]) message.getData();
			this.setPlayerID(message.getPlayerID());
			for (int i = 0; i<data.length; i++) {
				this.playerList.get(i).setName(data[i]);
			}
			sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
			
		}
		else if (message.getType() == CardGameMessage.JOIN) {
			if (this.getPlayerID() == message.getPlayerID()) {
				sendMessage(new CardGameMessage(4, -1, null));
			}
			else {
				table.printMsg((String)message.getData() + " joins the game." + '\n');
			}
			this.playerList.get(message.getPlayerID()).setName((String)message.getData());
			this.setIsConnected(true);
			table.setActivePlayer(playerID);
			table.repaint();
		}
		else if (message.getType() == CardGameMessage.FULL) {
			table.printMsg("The game is full!" + '\n');
			table.repaint();
		}
		else if (message.getType() == CardGameMessage.QUIT) {
			this.getPlayerList().get(message.getPlayerID()).setName("");
			this.setIsConnected(false);
			if (this.endOfGame() == false) {
				sendMessage(new CardGameMessage(4, -1, null));
				this.setIsStarted(false);
				table.repaint();
			}
		}
		else if (message.getType() == CardGameMessage.READY) {
			int numOfConnected = 0;
			for (int i = 0; i<this.getNumOfPlayers(); i++) {
				try {
					if (this.getPlayerList().get(i).getName()!=null || !this.getPlayerList().get(i).getName().isEmpty()) {
						numOfConnected++;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if (numOfConnected == 4) {
				this.setIsStarted(true);
				this.setIsConnected(true);
				table.setActivePlayer(playerID);
			}
			else {
				this.setIsStarted(false);
				table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName() + " is ready!" + '\n');
			}
		}
		else if (message.getType() == CardGameMessage.START) {
			this.start((BigTwoDeck) message.getData());
			if (this.getCurrentIdx() == table.getActivePlayer()) {
				table.printMsg("Your turn:" + '\n');
			}
			else {
				table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName() + "'s turn:" + '\n');
			}
			table.repaint();
		}
		else if (message.getType() == CardGameMessage.MOVE) {
			this.checkMove(message.getPlayerID(),(int[]) message.getData());
		}
		else if (message.getType() == CardGameMessage.MSG) {
			table.printChat((String)message.getData());
		}
	}

	/**
	 * a method for sending the specified message to the game server. This method should be called whenever the client wants to 
	 * communicate with the game server or other clients.
	 * 
	 * @param message to send message from the client.
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
		try {
			oos.writeObject(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * an inner class that implements the Runnable interface. You should implement the run() method from the Runnable interface 
	 * and create a thread with an instance of this class as its job in the makeConnection() method from the NetworkGame interface 
	 * for receiving messages from the game server. Upon receiving a message, the parseMessage() method from the NetworkGame interface
	 * should be called to parse the messages accordingly.
	 *   
	 * @author reynardverill
	 */
	class ServerHandler implements Runnable {
		@Override
		public void run() {
			CardGameMessage input;
			// TODO Auto-generated method stub
			try {
				ObjectInputStream streamReader = new ObjectInputStream(sock.getInputStream());
				while((input = (CardGameMessage)streamReader.readObject()) != null ) {
					parseMessage(input);
				}
			}
			catch(SocketException sockEx) {
				table.printMsg("Your connection to the server has been lost. Press connect to reconnect" + '\n');
				setIsConnected(false);
				sockEx.printStackTrace();
			}
			catch(Exception e) {
				table.printMsg("Your connection to the server has been lost. Press connect to reconnect" + '\n');
				setIsConnected(false);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * a method for creating an instance of BigTwoClient.
	 * 
	 * @param args an argument to start the main (if used).
	 */
	public static void main(String args[]) {
		BigTwoClient client = new BigTwoClient();
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player. Returns null 
	 * if no valid hand can be composed from the specified list of cards.
	 * 
	 * @param player a reference to the player who is trying to make the move with the selected hand.
	 * @param cards an arrayList of cards containing the cards the player is trying to make to be checked. 
	 * 
	 * @return Hand returns a subclass of hand or null.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		if (cards == null) {
			return null;
		}
		//creating new objects of the hand's subclasses
		Hand single = new Single(player, cards);
		Hand pair = new Pair(player, cards);
		Hand triple = new Triple(player, cards);
		Hand quad = new Quad(player, cards);
		Hand flush = new Flush(player, cards);
		Hand fullHouse = new FullHouse(player, cards);
		Hand straight = new Straight(player, cards);
		Hand straightFlush = new StraightFlush(player, cards);
		//checking if they are valid, and returns the hand if so
		if (cards.size() == 5) {
			if (straightFlush.isValid()) {
				straightFlush.getType();
				return straightFlush;
			}
			else if (quad.isValid()) {
				quad.getType();
				return quad;
			}
			else if (fullHouse.isValid()) {
				fullHouse.getType();
				return fullHouse;
			}
			else if (flush.isValid()) {
				flush.getType();
				return flush;
			} 
			else if (straight.isValid()) {
				straight.getType();
				return straight;
			} 
		}
		
		else if (cards.size() == 3) {
			if (triple.isValid()) {
				triple.getType();
				return triple;
			}
		} 
		else if (cards.size() == 2) {
			if (pair.isValid()) {
				pair.getType();
				return pair;
			}
 		} 
		else if (cards.size() == 1) {
			if (single.isValid()) {
				return single;
			}
 		}
 		return null;
	}

}
