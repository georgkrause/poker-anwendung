package core;

public class Player {
	public String name;
	private int credit;
	private int id;
	public boolean folded = false;
	public int debt=0;

	private Card[] cards = new Card[2];
	
	Player(int credit) {
		this.credit = credit;
	}

	/**
	 * player check the current round
	 */
	public void check() {
		return;
	}

	/**
	 * player leave the current round 
	 */
	public void fold() {
		this.setCards(null);
		this.folded = true;
		return;
	}

	/**
	 * player pays the current cue
	 * 
	 * @return whether action is possible or not
	 */
	public boolean call(int cue) {
		this.debt=cue-debt;
		if (this.credit >= debt) {
			this.credit -= debt;
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
	public boolean raise(int cue) {
		this.debt=cue-debt;
		if (this.credit >= debt) {
			this.credit -= debt;
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
	public void changeCredit(int value) {
		this.credit += value;
		return;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the cards
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * @param cards the cards to set
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * @return the folded
	 */
	public boolean isFolded() {
		return folded;
	}
	
	
}
