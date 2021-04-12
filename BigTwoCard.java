
/**
 * The BigTwoCard class is a subclass of the Card class, and is used to 
 * model a card used in a Big Two card game.
 * 
 * @author reynardverill
 * @version 1.0
 */
public class BigTwoCard extends Card {
	
	/**
	 * a constructor for building a card with the specified suit and rank. 
	 * suit is an integer between 0 and 3, and rank is an integer between 
	 * 0 and 12.
	 * 
	 * @param suit   the suit of the card
	 * @param rank   the rank of the card
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * a method for comparing the order of this card with the specified card. 
	 * Returns a negative integer, zero, or a positive integer as this card is 
	 * less than, equal to, or greater than the specified card.
	 * 
	 * @return either positive or negative integer or zero
	 */
	public int compareTo(Card card) {
		// check if the rank of the cards are the same.
		if (card.getRank() == this.getRank()) {
			if (this.getSuit() == card.getSuit()) {
				return 0;
			}
			else if (this.getSuit() > card.getSuit()) {
				return 1;
			}
			else {
				return -1;
			}
		}// compare the power of the rank in both cards if the value of the rank is above 2.
		else if (card.getRank()>1 && this.getRank()>1) {
			if (this.getRank()>card.getRank()) {
				return 1;
			}
			else {
				return -1;
			}
		}// compare the the power of the rank in both cards if either one is less or equals to 2 while the other is larger than 2. 
		else if (card.getRank()<=1 && this.getRank()>1){
			return -1;
		}
		else if (card.getRank()>1 && this.getRank()<=1) {
			return 1;
		} // compare the power of the rank in both cards if both have values of less than or equal to 2.
		else if (card.getRank()<=1 && this.getRank()<=1) {
			if (this.getRank() > card.getRank()) {
				return 1;
			}
			else {
				return -1;
			}
		}
		return 0;
	}
	
	
}
