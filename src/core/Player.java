package core;

public class Player {
	public String name;
	private int credit;
	private int id;

	private Card[] cards = new Card[2];
	
	Player(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * player check the current round
	 */
	void check() {
		return;
	}

	/**
	 * player leave the current round TODO #9
	 */
	void fold() {
		this.cards = null;
		return;
	}

	/**
	 * player pays the current cue
	 * 
	 * @return whether action is possible or not
	 */
	boolean call(int cue) {
		if (this.credit > cue) {
			this.credit -= cue;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * player raise the current cue
	 * 
	 * @return whether action is possible or not
	 */
	boolean raise(int cue) {
		if (this.credit > cue) {
			this.credit -= cue;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the credit
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * 
	 * @param value
	 */
	public void raiseCredit(int value) {
		this.credit += value;
		return;
	}

	/**
	 * @return the cards
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
