import java.util.*;

/**
 * A subclass of the abstract class Hand. This class is used to examine whether the hand that is received by the static method composeHand 
 * satisfy the requirements to be a full house or not, and if it beats the last hand on table.
 * 
 * @author reynardverill
 * @version 1.0
 */
public class FullHouse extends Hand{
	
	/**
	 * A constructor to create the single object.
	 * 
	 * @param player the player playing this hand.
	 * @param cards the set of card(s) in hand.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
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
		boolean check = false;
		this.sort();
		if (this.size() != 5) {
			return check;
		}
		else {
			if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() != this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank()) {
				check = true;
			}
			else if (this.getCard(0).getRank() == this.getCard(1).getRank() &&  this.getCard(1).getRank() != this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank()) {
				check = true;
			}
		}
		return check;
	}
	
	/**
	 * An overridden method derived from hand to test whether the given hand of cards can beat the last set of card(s) played on the table.
	 * 
	 * @return a boolean of true or false depending whether the player can successfully
	 *  beat the card on the table or not with the chosen set of card(s)
	 */
	@Override
	public boolean beats(Hand hand) {
		hand.sort();
		if (hand.getType() == "Flush" || hand.getType() == "Straight") {
			return true;
		}
		else if (hand.getType() == "FullHouse") {
			Card rankThis = null;
			Card rankTwo = null;
			int count = 0;
			
			for (int i = 0; i<this.size(); i++) {
				count = 0;
				for (int j = i; j<this.size(); j++) {
					if (this.getCard(j).getRank() == this.getCard(i).getRank()) {
						count++;
					}
				}
				if (count == 3) {
					rankThis = this.getCard(i);
					break;
				}
			}
			for (int i = 0; i<hand.size(); i++) {
				count = 0;
				for (int j = i; j<hand.size(); j++) {
					if (hand.getCard(j).getRank() == hand.getCard(i).getRank()) {
						count++;
					}
				}
				if (count == 3) {
					rankTwo = hand.getCard(i);
					break;
				}
			}
			if (rankThis.compareTo(rankTwo) == 1) {
				return true;
			}
		}
		return false;
	}
	/**
	 * An overridden method of an abstract method derived from hand to return the type of the hand.
	 * 
	 * @return FullHouse or null	a string of the type of the given set of card(s) in hand (e.g. single, pair, triple, etc.)
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		if (this.isValid() == true) {
			return "FullHouse";
		}
		return null;
	}
}
