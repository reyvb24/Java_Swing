import java.util.*;

/**
 * A subclass of the abstract class Hand. This class is used to examine whether the hand that is received by the static method composeHand 
 * satisfy the requirements to be a quad or not, and if it beats the last hand on table.
 * 
 * @author reynardverill
 * @version 1.0
 */
public class Quad extends Hand{
	
	/**
	 * A constructor to create the single object.
	 * 
	 * @param player the player playing this hand.
	 * @param cards the set of card(s) in hand.
	 */
	public Quad(CardGamePlayer player, CardList cards) {
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
		if (this.size() == 5) {
			this.sort();
			for (int i = 0; i<2; i++) {
				int count = 0;
				for (int j = i; j<5; j++) {
					if (this.getCard(i).getRank() == this.getCard(j).getRank()) {
						count++;
					}
				}
				if (count == 4) {
					check = true;
					break;
				}
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
	public boolean beats(Hand hand) {
		Card quadOne = null;
		Card quadTwo = null;
		if (hand.getType() == "Straight" || hand.getType() == "Fullhouse" || hand.getType() == "Flush") {
			return true;
		}
		else if (hand.getType() == "Quad") {
			for (int i = 0; i<this.size(); i++) {
				int count = 0;
				for (int j = i; j<this.size(); j++) {
					if (this.getCard(i).getRank() == this.getCard(j).getRank()) {
						count++;
					}
				}
				if (count == 4) {
					quadOne = this.getCard(i);
					break;
				}
			}
			for (int i = 0; i<this.size(); i++) {
				int count = 0;
				for (int j = i; j<this.size(); j++) {
					if (hand.getCard(i).getRank() == hand.getCard(j).getRank()) {
						count++;
					}
				}
				if (count == 4) {
					quadTwo = hand.getCard(i);
					break;
				}
			}
			if (quadOne.compareTo(quadTwo) == 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * An overridden method of an abstract method derived from hand to return the type of the hand.
	 * 
	 * @return Quad or null	a string of the type of the given set of card(s) in hand (e.g. single, pair, triple, etc.)
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Quad";
	}
}
