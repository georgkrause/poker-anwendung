package core;

public class Player {
	public String name; // Name 
	private int credit; // Kontostand
	private int id; // ID
	public boolean folded = false; // ob Spieler das Spiel verlassen hat
	public int debt=0; // Schulden (aktueller Einsatz - bereits gezahlter Einsatz)
	public int [] pairWorth= new int [4]; //wird f�r Vierling,Drilling,Pair,ZweiPairs und FullHouse verwendet
	public int highCard=0;
	public int handWorth=0;

	private Card[] cards = new Card[2]; // Handkarten
	
	/**
	 * erzeugt neuen Spieler
	 * @param credit
	 */
	Player(int credit) {
		this.credit = credit;
	}

	/**
	 * Spieler checkt
	 */
	public void check() {
		return;
	}

	/**
	 * Spieler verl�sst die aktuelle Runde
	 */
	public void fold() {
		this.setCards(null); // Karten werden gel�scht
		this.folded = true;
		return;
	}

	/**
	 * Spieler bezahlt den aktuellen Einsatz
	 * 
	 * @return Gibt zur�ck ob m�glich oder nicht
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
	 * Spieler erh�ht den aktuellen Einsatz
	 * 
	 * @return Gibt zur�ck, ob die Aktion m�glich ist oder nicht
	 */
	public boolean raise(int cue) {
		if (this.credit >= debt) {
			this.debt=cue-debt;
			this.credit -= debt;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return aktueller Kontostand
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * �ndert den Kontostand
	 * @param value
	 */
	public void changeCredit(int value) {
		this.credit += value;
		return;
	}

	/**
	 * @return Spieler-ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Handkarten
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * @param Setzt Handkarten
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * @return ob Spieler die Runde verlassen hat
	 */
	public boolean isFolded() {
		return folded;
	}
	
	
}
