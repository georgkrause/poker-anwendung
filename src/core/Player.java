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
	public int[] pairWorth = new int[4]; // wird f�r
											// Vierling,Drilling,Pair,ZweiPairs
											// und FullHouse verwendet
	public int highCard = 0; // HighCard des Spielers
	public int handWorth = 0; // Wert des derzeitigen Blattes des Spielers

	private Card[] cards = new Card[2]; // Handkarten

	/**
	 * erzeugt neuen Spieler
	 * 
	 * @author Georg
	 * @param credit
	 */
	Player(int credit) {
		this.credit = credit;
	}

	/**
	 * Fragt den Spielzug des Spielers ab
	 * 
	 * @author Georg
	 * @param window
	 * @return
	 */
	public int[] decide(Window window) {
		int[] choice = new int[2];
		boolean valid = false;
		do {
			// L�sst menschlichen Spieler entscheiden was getan werden soll
			choice[0] = window.DialogBox();
			if (choice[0] >= 0) {
				valid = true;
			}
		} while (!valid);

		// Wenn Spieler erh�hen m�chte, frage neuen Wert ab
		if (choice[0] == 0) {
			do {
				choice[1] = window.RaiseDialogBox();
			} while (choice[1] == 0 || (choice[1] % 50 != 0));

		}

		return choice;
	}

	/**
	 * Spieler checkt
	 * 
	 * @author Georg
	 */
	public void check() {
		return;
	}

	/**
	 * Spieler verl�sst die aktuelle Runde
	 * 
	 * @author Georg
	 */
	public void fold() {
		this.setCards(null); // Karten werden gel�scht
		this.folded = true;
		return;
	}

	/**
	 * Spieler bezahlt den aktuellen Einsatz
	 * 
	 * @author georg
	 * @return Gibt zur�ck ob m�glich oder nicht
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
	 * Spieler erh�ht den aktuellen Einsatz
	 * 
	 * @author georg
	 * @return Gibt zur�ck, ob die Aktion m�glich ist oder nicht
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
	 * @author georg
	 * @return aktueller Kontostand
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * �ndert den Kontostand
	 * 
	 * @author georg
	 * @param value
	 */
	public void changeCredit(int value) {
		this.credit += value;
		return;
	}

	/**
	 * @author georg
	 * @return Spieler-ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * @author georg
	 * @return Handkarten
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * Handkarten
	 * 
	 * @author georg
	 * @param Setzt
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * @author georg
	 * @return ob Spieler die Runde verlassen hat
	 */
	public boolean isFolded() {
		return folded;
	}

	/**
	 * Sortiert die Karten nach Wert
	 * 
	 * @author Sebastian
	 * @param r
	 * @param tableCards
	 * @return
	 */
	public int[] sortWorth(int r, Card[] tableCards) {

		int[] allCardsWorth = new int[7];
		allCardsWorth[0] = this.getCards()[0].getWorthID();
		allCardsWorth[1] = this.getCards()[1].getWorthID();
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
			while ((z > 0) && (allCardsWorth[z] < allCardsWorth[z - 1])) { // "stinknormales"
																			// Sortieren
				int hilf = allCardsWorth[z];
				allCardsWorth[z] = allCardsWorth[z - 1];
				allCardsWorth[z - 1] = hilf;
				z = z - 1;
			}
		}

		return allCardsWorth;

	}

	/**
	 * Gibt Farbe zur�ck, die 5 Karten haben. Ansonsten wird -30000
	 * zur�ckgegeben.
	 * 
	 * @author Sebastian
	 * @param r
	 * @param tableCards
	 * @return
	 */
	public int fiveColor(int r, Card[] tableCards) {
		Card[] allCards = new Card[7];

		allCards[0] = this.getCards()[0];
		allCards[1] = this.getCards()[1];
		allCards[2] = tableCards[0];
		allCards[3] = tableCards[1];
		allCards[4] = tableCards[2];
		allCards[5] = tableCards[3];
		allCards[6] = tableCards[4];

		int[] colors = { 0, 0, 0, 0 };

		for (int i = 0; i < r; i++) { // liest die Farben der Karten aus ...
			colors[allCards[i].getColorID()]++;
		}

		for (int i = 0; i < 4; i++) { // ... und pr�ft, ob der Spieler 5 Karten
										// einer Farbe (Flush) hat
			if (colors[i] >= 5) {
				return i; // Wenn ja wird der Farbindex zur�ckgegeben
			}
		}
		return -30000;
	}

	/**
	 * Z�hlt Paare, Drillinge und Vierlinge
	 * 
	 * @author Sebastian
	 * @param cardWorth
	 * @return
	 */
	public int[] sameWorth(int r, int[] cardWorth) {
		int same = 1;
		int same2 = 1;
		int twins = 0;
		int[] sameWorth = new int[4];

		// Durchl�uft alle Karten
		for (int a = 0; a < r; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]) {
				same = same + 1;
			}
			if (same >= 4 && cardWorth[a] == cardWorth[a - 3]) { // Wenn ein
																	// Vierling
																	// (4 Karten
																	// eines
																	// Wertes)
																	// dabei
																	// ist, wird
																	// sofort
																	// returnt
				sameWorth[0] = same; // In sameWorth[0] wird je die Anzahl der
										// vorkommenden Karten mit gleichem
										// Wert, z.B. bei Vierling =
										// 4,gespeichert
				sameWorth[1] = cardWorth[a - 1]; // In sameWorth[1] wird der
													// Wert der Karten mit
													// gleichem Wert gespeichert
				return sameWorth;
			} else {
				if (same == 3 && a != r - 1 && cardWorth[a] != cardWorth[a + 1] // Wenn
																				// es
																				// keinen
																				// Vierling
																				// gibt,wird
																				// gesucht,
																				// ob
																				// es
																				// einen
																				// Drilling
																				// gibt
																				// ...
						&& cardWorth[a] == cardWorth[a - 2]) {
					sameWorth[0] = same;
					sameWorth[1] = cardWorth[a - 1];
				} else {
					if ((same == 2) && (a != r - 1) && (twins == 0) // Und wenn
																	// es keinen
																	// Drilling
																	// gibt,
																	// wird
																	// gesucht,
																	// ob es
																	// einen
																	// Zwilling/Paar
																	// gibt
							&& (cardWorth[a] != cardWorth[a + 1])) {
						twins = 1;
						sameWorth[0] = same;
						sameWorth[1] = cardWorth[a - 1];
					}

				}
			}
		}

		for (int a = 0; a < r; a++) { // Hier wird gesucht,ob es noch ein
										// zweites Paar gibt ...
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]
					&& sameWorth[1] != cardWorth[a]) {
				same2 = same2 + 1;
				sameWorth[2] = same2; // und dieses wird entsprechend in
										// sameWorth[2] ...
				sameWorth[3] = cardWorth[a]; // und [3] gespeichert

			}
		}
		return sameWorth; // anschlie�end wird das Array returnt
	}

	/**
	 * pr�ft, ob ein Spieler f�nf Karten in einer Wertreihenfolge (Stra�e) hat
	 * 
	 * @author Sebastian
	 * @param r
	 * @param cardWorth
	 * @return
	 */
	public int followFive(int r, int[] cardWorth) {
		int follow = 1;
		for (int a = 0; a < r; a++) { // Pr�ft, ob die sortierten Karten in
										// einer Wertreihenfolge liegen z.B:
										// 1-2-3-4-5
			if ((a != 0 && a != r - 1 && cardWorth[a] == cardWorth[a - 1] + 1 && cardWorth[a] == cardWorth[a + 1] - 1)
					|| (a == r - 1 && cardWorth[r - 1] == cardWorth[r - 2] + 1)) {
				follow = follow + 1; // F�r jede weitere anliegende Zahl, wird
										// die Variable follow um eins erh�ht
										// ...

				if (follow >= 5) { // und wenn diese gr��ergleich 5 ist, also
									// eine Stra�e vorliegt, wird der h�chste
									// Wert returnt, der somit die Highcard des
									// Blattes ist

					return cardWorth[a];
				}
			}
		}
		return 0; // Ansonsten wird 0 returnt

	}

	/**
	 * Berechnet den Wert der Hand
	 * 
	 * @author Sebastian
	 * @param cardWorth
	 * @param fiveColor
	 * @param followFive
	 * @param sameWorth
	 * @return
	 */
	public int handWorth(int[] cardWorth, int fiveColor, int followFive,
			int[] sameWorth) {
		// Die verschiedenen m�glichen Gewinnh�nde werden in absteigender
		// Reihenfolge gepr�ft, d.h. es wird gepr�ft, ob diese vorhanden sind.
		// Dazu werden die Ergebnisse aus den vorherigen Funktionen
		// sortWorth,fiveColor,sameWorth und followFive benutzt
		if ((followFive == 12) & (fiveColor != -30000)) { // RoyalFlush
			return 10;
		} else {
			if (followFive != 0 && fiveColor != -30000) { // Straight Flush
				return 9;
			} else {
				if (sameWorth[0] == 4) { // Vierling
					return 8;
				} else {
					if (sameWorth[0] == 3 && sameWorth[2] == 2) { // drilling+Paar
																	// (Full
																	// House)
						return 7;
					} else {
						if (fiveColor != -30000) { // 5 from one color (Flush)
							return 6;
						} else {
							if (followFive != 0) { // street/Stra�e
								return 5;
							} else {
								if (sameWorth[0] == 3 || sameWorth[2] == 3) { // drilling
									return 4;
								} else {
									if (sameWorth[0] == 2 && sameWorth[2] == 2) { // zwei
																					// Paare
										return 3;
									} else {
										if (sameWorth[0] == 2
												|| sameWorth[2] == 2) { // paar
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
