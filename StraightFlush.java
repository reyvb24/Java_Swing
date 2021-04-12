
/**
 * A subclass of the abstract class Hand. This class is used to examine whether the hand that is received by the static method composeHand 
 * satisfy the requirements to be a straight flush or not, and if it beats the last hand on table.
 * 
 * @author reynardverill
 * @version 1.0
 */
public class StraightFlush extends Hand{
	
	/**
	 * A constructor to create the single object.
	 * 
	 * @param player the player playing this hand.
	 * @param cards the set of card(s) in hand.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		this.sort();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * An overridden method of an abstract method derived from hand to test if the object is valid or not with the given set of cards in hand.
	 * 
	 * @return a boolean of true or false depending if the type of the set of card(s) match(es) the subclass
	 */
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		Flush flush = new Flush(this.getPlayer(), this);
		Straight straight = new Straight(this.getPlayer(), this);
		if (flush.isValid() && straight.isValid()) {
			return true;
		}
		return false;
	}
	
	/**
	 * An overridden method derived from hand to test whether the given hand of cards can beat the last set of card(s) played on the table.
	 * 
	 * @return a boolean of true or false depending whether the player can successfully
	 *  beat the card on the table or not with the chosen set of card(s)
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Flush" || hand.getType() == "Straight" || hand.getType() == "FullHouse" || hand.getType() == "Quad") {
			return true;
		}
		else if (hand.getType() == this.getType()) {
			if (this.getTopCard().compareTo(hand.getTopCard()) == 1 ) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * An overridden method of an abstract method derived from hand to return the type of the hand.
	 * 
	 * @return StraightFlush or null	a string of the type of the given set of card(s) in hand (e.g. single, pair, triple, etc.)
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "StraightFlush";
	}
}
