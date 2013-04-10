package core;

import gui.Window;

public class Player {
	public String name; // Name
	private int credit; // Kontostand
	private int id; // ID
	int raiseWorth = 100;
	public boolean folded = false; // ob Spieler das Spiel verlassen hat
	public int debt = 0; // Schulden (aktueller Einsatz - bereits gezahlter
							// Einsatz)
	public int[] pairWorth = new int[4]; // wird für
											// Vierling,Drilling,Pair,ZweiPairs
											// und FullHouse verwendet
	public int highCard = 0;
	public int handWorth = 0;

	private Card[] cards = new Card[2]; // Handkarten

	/**
	 * erzeugt neuen Spieler
	 * 
	 * @param credit
	 */
	Player(int credit) {
		this.credit = credit;
	}

	/**
	 * Fragt den Spielzug des Spielers ab
	 * 
	 * @param window
	 * @return
	 */
	public int[] decide(Window window) {
		int[] choice = new int[2];
		boolean valid = false;
		do {
			// Lässt Spieler entscheiden was getan werden soll
			choice[0] = window.DialogBox();
			if (choice[0] >= 0) {
				valid = true;
			}
		} while (!valid);

		// Wenn Spieler erhöhen möchte, frage neuen Wert ab
		if (choice[0] == 0) {
			do {
				choice[1] = window.RaiseDialogBox();
			} while (choice[1] == 0 || (choice[1] % 50 != 0));

		}

		return choice;
	}

	/**
	 * Spieler checkt
	 */
	public void check() {
		return;
	}

	/**
	 * Spieler verlässt die aktuelle Runde
	 */
	public void fold() {
		this.setCards(null); // Karten werden gelöscht
		this.folded = true;
		return;
	}

	/**
	 * Spieler bezahlt den aktuellen Einsatz
	 * 
	 * @return Gibt zurück ob möglich oder nicht
	 */
	public boolean call(int cue) {
		this.debt = cue - debt;
		if (this.credit >= debt) {
			this.credit -= debt;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Spieler erhöht den aktuellen Einsatz
	 * 
	 * @return Gibt zurück, ob die Aktion möglich ist oder nicht
	 */
	public boolean raise(int cue) {
		this.debt = cue - debt;
		if (this.credit >= debt) {
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
	 * ändert den Kontostand
	 * 
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
	 * @param Setzt
	 *            Handkarten
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

	public int[] sortWorth(int r, Card[] tableCards, Card[] cards) {
		// TODO Auto-generated method stub

		int[] allCardsWorth = new int[7];
		allCardsWorth[0] = cards[0].getWorthID();
		allCardsWorth[1] = cards[1].getWorthID();
		allCardsWorth[2] = tableCards[0].getWorthID();
		allCardsWorth[3] = tableCards[1].getWorthID();
		allCardsWorth[4] = tableCards[2].getWorthID();
		if (r > 5)
			allCardsWorth[5] = tableCards[3].getWorthID();
		else
			allCardsWorth[5] = -10;
		if (r > 6)
			allCardsWorth[6] = tableCards[4].getWorthID();
		else
			allCardsWorth[6] = -20;
		for (int a = 0; a < 7; a++) {
			int z = a;
			while ((z > 0) && (allCardsWorth[z] < allCardsWorth[z - 1])) {
				int hilf = allCardsWorth[z];
				allCardsWorth[z] = allCardsWorth[z - 1];
				allCardsWorth[z - 1] = hilf;
				z = z - 1;
			}
		}
		// for (int a = 0; a < 7; a++) {
		// System.out.print(allCardsWorth[a]);
		// }
		// System.out.println();
		return allCardsWorth;

	}

	/**
	 * returns the color id where are 5 cards in the cards or 30000
	 * 
	 * @param ccard0
	 * @param ccard1
	 * @param ccard2
	 * @param ccard3
	 * @param ccard4
	 * @param pcard0
	 * @param pcard1
	 * @return
	 */
	// public int fivecolor(int ccard0, int ccard1, int ccard2, int ccard3,
	// int ccard4, int pcard0, int pcard1) {
	public int fiveColor(int r, Card[] tableCards, Card[] playerCards) {
		Card[] allCards = new Card[7];

		allCards[0] = playerCards[0];
		allCards[1] = playerCards[1];
		allCards[2] = tableCards[0];
		allCards[3] = tableCards[1];
		allCards[4] = tableCards[2];
		allCards[5] = tableCards[3];
		allCards[6] = tableCards[4];

		int[] colors = { 0, 0, 0, 0 };

		for (int i = 0; i < r; i++) {
			colors[allCards[i].getColorID()]++;
		}

		for (int i = 0; i < 4; i++) {
			if (colors[i] >= 5) {
				return i;
			}
		}
		return -30000;
	}

	/**
	 * counts pairs, threelings and fourlings
	 * 
	 * @param cardWorth
	 * @return
	 */
	public int[] sameWorth(int r, int[] cardWorth) {
		int same = 1;
		int same2 = 1;
		int twins = 0;
		int[] sameWorth = new int[4];

		// Durchläuft alle Karten
		for (int a = 0; a < r; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]) {
				same = same + 1;
			}
			if (same >= 4 && cardWorth[a] == cardWorth[a - 3]) {
				sameWorth[0] = same;
				sameWorth[1] = cardWorth[a - 1];
				return sameWorth;
			} else {
				if (same == 3 && a != r - 1 && cardWorth[a] != cardWorth[a + 1]
						&& cardWorth[a] == cardWorth[a - 2]) {
					sameWorth[0] = same;
					sameWorth[1] = cardWorth[a - 1];
				} else {
					if ((same == 2) && (a != r - 1) && (twins == 0)
							&& (cardWorth[a] != cardWorth[a + 1])) {
						twins = 1;
						sameWorth[0] = same;
						sameWorth[1] = cardWorth[a - 1];
					}

				}
			}
		}

		for (int a = 0; a < r; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]
					&& sameWorth[1] != cardWorth[a]) {
				same2 = same2 + 1;
				sameWorth[2] = same2;
				sameWorth[3] = cardWorth[a];

			}
		}
		return sameWorth;
	}

	public int followFive(int r, int[] cardWorth) {
		int follow = 1;
		for (int a = 0; a < r; a++) {
			if ((a != 0 && a != r - 1 && cardWorth[a] == cardWorth[a - 1] + 1 && cardWorth[a] == cardWorth[a + 1] - 1)
					|| (a == r - 1 && cardWorth[r - 1] == cardWorth[r - 2] + 1)) {
				follow = follow + 1;

				if (follow >= 5) {

					return cardWorth[a];
				}
			}
		}
		return 0;

	}

	public int handWorth(int[] cardWorth, int fiveColor, int followFive,
			int[] sameWorth) {

		if ((followFive == 12) & (fiveColor != -30000)) { // RoyalFlush
			return 10;
		} else {
			if (followFive != 0 && fiveColor != -30000) { // Straight Flush
				return 9;
			} else {
				if (sameWorth[0] == 4) { // Vierling
					return 8;
				} else {
					if (sameWorth[0] == 3 && sameWorth[2] == 2) { // drilling+pair
					// System.out
					// .println(sameWorth[0] + " " + sameWorth[1]
					// + " " + sameWorth[2] + " "
					// + sameWorth[3] + " ");
						return 7;
					} else {
						if (fiveColor != -30000) { // 5 from one color
							return 6;
						} else {
							if (followFive != 0) { // street
								return 5;
							} else {
								if (sameWorth[0] == 3 || sameWorth[2] == 3) { // drilling
								// System.out.println(sameWorth[0] + " "
								// + sameWorth[1] + " " + sameWorth[2]
								// + " " + sameWorth[3] + " ");
									return 4;
								} else {
									if (sameWorth[0] == 2 && sameWorth[2] == 2) { // two
																					// pairs
																					// System.out.println(sameWorth[0]
																					// +
																					// " "
										// + sameWorth[1] + " "
										// + sameWorth[2] + " "
										// + sameWorth[3] + " ");
										return 3;
									} else {
										if (sameWorth[0] == 2
												|| sameWorth[2] == 2) { // pair
											return 2;
										} else
											return 1; // Highcard

									}
								}
							}
						}
					}
				}
			}

		}

	}

}
