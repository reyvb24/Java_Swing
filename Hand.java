/**
 * The Hand class is a subclass of the CardList class, and is used 
 * to model a hand of cards. It has a private instance variable for 
 * storing the player who plays this hand. It also has methods for 
 * getting the player of this hand, checking if it is a valid hand,
 * getting the type of this hand, getting the top card of this hand, 
 * and checking if it beats a specified hand. 
 * 
 * @author reynardverill
 *
 */
abstract class Hand extends CardList{
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player
	 * @param cards
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i<cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * the player who plays this hand.
	 */
	private CardGamePlayer player;
	
	/**
	 * a method for retrieving the player of this hand.
	 * 
	 * @return CardGamePlayer the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return Card the top card of this hand
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	
	/**
	 * a dummy method for checking if this hand beats a specified hand.
	 * To be implemented in the subclasses after getting overridden in the respective subclasses.
	 * 
	 * @param hand the handsOnTable in BigTwo class.
	 * @return boolean true or false
	 */
	public boolean beats(Hand hand) {
		return true;
	}
	
	/**
	 * abstract method of isValid inside abstract class.
	 * 
	 * @return boolean
	 */
	abstract boolean isValid();
	
	/**
	 * abstract method of getString inside abstract class.
	 * 
	 * @return string 
	 */
	abstract String getType();
}
