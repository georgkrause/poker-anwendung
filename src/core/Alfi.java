package core;

import java.util.Random;

/**
 * 
 * @author sebastian
 *
 */
public class Alfi extends Player {
	// Wir haben uns dazu entschlossen, die PC-Spieler Alfi zu nennen
	float outs = 0;

	/**
	 * Konstruktor
	 * 
	 * @param credit
	 */
	Alfi(int credit) {
		super(credit);
	}

	/**
	 * entscheidet, welchen Zug die KI ausführt
	 * 
	 * @author sebastian
	 * @param tableCards
	 * @param pot
	 * @param cue
	 * @return
	 */
	public int decide(Card[] tableCards, int pot, int cue) {
		int choice;
		if (!tableCards[1].isVisible()) {
			choice = this.decideFirst(this.getCredit());
		} else {
			if (!tableCards[3].isVisible()) {
				choice = this.decideSecond(this.getCredit(), tableCards, 5,
						pot, cue);
			} else {
				if (!tableCards[4].isVisible()) {
					choice = this.decideThird(this.getCredit(), tableCards,
							this.getCards(), 6, pot, cue);
				} else {
					choice = this.decideLast(this.getCredit(), tableCards,
							this.getCards(), 7, pot, cue);
				}
			}
		}
		return choice;
	}

	/**
	 * KI für die erste Wettrunde (0 Community Cards sichtbar)
	 * 
	 * @author sebastian
	 * @param money
	 * @return
	 */
	public int decideFirst(int money) { // 1.Wettrunde,nur 2
										// Handkarten
		Random r = new Random();

		if (this.getCards()[1].getWorthID() == this.getCards()[0].getWorthID()
				|| this.getCredit() > 15000) {
			int random = r.nextInt(10);

			if (random > 5) {
				int raiseRandom = r.nextInt(5) + 1;
				this.raiseWorth = 100 * raiseRandom;
				return 0;
			}
		} else if (this.getCredit() < 3000) {
			int random = r.nextInt(10);
			if (random > 7)
				return 2;
		}
		return 1;
	}

	/**
	 * KI für die zweite Wettrunde (3 Community Cards sichtbar)
	 * 
	 * @author sebastian
	 * @param money
	 * @param tableCards
	 * @param round
	 * @param pot
	 * @param cue
	 * @return
	 */
	public int decideSecond(int money, Card[] tableCards, int round, int pot,
			int cue) {

		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards);
		int fiveColor = fiveColor(round, tableCards);
		pairWorth = sameWorth(round, sortedcards);
		int followfive = followFive(round, sortedcards);
		this.handWorth = handWorth(sortedcards, fiveColor, followfive,
				pairWorth);
		if (this.handWorth > 3) {
			int random = r.nextInt(12);
			if (random > 10) {
				this.raiseWorth = this.getCredit();
				return 0;
			}
			if (random > 5) {
				int raiseRandom = r.nextInt(10) + 1;
				this.raiseWorth = 100 * raiseRandom;
				return 0;
			} else {
				if (this.handWorth < 5
						&& (cue - this.debt) > this.getCredit() / 2)
					return 2;
				else
					return 1;
			}
		} else {
			calculateOuts(5, fiveColor, tableCards);

			if (outs >= ((cue - this.debt) * 100 / pot)) {
				int random = r.nextInt(10);
				if (random > 9)
					return 2;
				else if ((random > 5
						|| outs / 2 >= ((cue - this.debt) * 100 / pot) || this
						.getCredit() > 15000)) {
					int raiseRandom = r.nextInt(8) + 1;
					this.raiseWorth = 100 * raiseRandom;
					return 0;
				} else
					return 1;
			} else {
				int random = r.nextInt(10);
				if (random > 5 && outs * 1.5 > ((cue - this.debt) * 100 / pot)
						&& this.getCredit() > 4000)
					return 1;
				else if (outs == 0) {
					int random1 = r.nextInt(10);
					if (random1 > 4)
						return 1;
					else
						return 2;
				} else
					return 2;
			}
		}
	}

	/**
	 * KI für die dritte Wettrunde (4 Community Cards sichtbar)
	 * 
	 * @author sebastian
	 * @param money
	 * @param tableCards
	 * @param playerCards
	 * @param round
	 * @param pot
	 * @param cue
	 * @return
	 */
	public int decideThird(int money, Card[] tableCards, Card[] playerCards,
			int round, int pot, int cue) {
		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards);
		int fiveColor = fiveColor(round, tableCards);
		pairWorth = sameWorth(round, sortedcards);
		int followfive = followFive(round, sortedcards);
		this.handWorth = handWorth(sortedcards, fiveColor, followfive,
				pairWorth);
		if (this.handWorth > 4) {
			int random = r.nextInt(12);
			if (random > 10) {
				this.raiseWorth = this.getCredit();
				return 0;
			}
			if (random > 5) {
				int raiseRandom = r.nextInt(10) + 1;
				this.raiseWorth = 100 * raiseRandom;
				return 0;
			} else {
				if (this.handWorth < 5
						&& (cue - this.debt) > this.getCredit() / 2)
					return 2;
				else
					return 1;
			}
		} else {
			calculateOuts(5, fiveColor, tableCards);

			if (outs >= ((cue - this.debt) * 100 / pot)) {
				int random = r.nextInt(10);
				if (random > 9)
					return 2;
				else if ((random > 5 && outs / 2 >= ((cue - this.debt) * 100 / pot))
						|| this.getCredit() > 15000) {
					int raiseRandom = r.nextInt(10) + 1;
					this.raiseWorth = 100 * raiseRandom;
					return 0;
				} else
					return 1;
			} else {
				int random = r.nextInt(10);
				if (random > 7 && outs * 1.5 > ((cue - this.debt) * 100 / pot)
						&& this.getCredit() > 4000)
					return 1;
				else
					return 2;
			}
		}
	}

	/**
	 * KI für die letzte Wettrunde (5 Community Cards sichtbar)
	 * 
	 * @author sebastian
	 * @param money
	 * @param tableCards
	 * @param playerCards
	 * @param round
	 * @param pot
	 * @param cue
	 * @return
	 */
	public int decideLast(int money, Card[] tableCards, Card[] playerCards,
			int round, int pot, int cue) {
		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards);
		int fiveColor = fiveColor(round, tableCards);
		pairWorth = sameWorth(round, sortedcards);
		int followfive = followFive(round, sortedcards);
		this.handWorth = handWorth(sortedcards, fiveColor, followfive,
				pairWorth);
		int random = 0;
		if (this.handWorth > 5) {
			random = r.nextInt(12);
			if (random > 10) {
				this.raiseWorth = this.getCredit();
				return 0;
			} else {
				if (random > 5) {
					int raiseRandom = r.nextInt(10) + 1;
					this.raiseWorth = 100 * raiseRandom;
					return 0;
				} else
					return 1;
			}
		} else if (this.handWorth < 5
				&& cue - this.debt > this.getCredit() * 0.75)
			random = r.nextInt(10);
		if (random > 6)
			return 2;
		else
			return 1;
	}

	/**
	 * Berechnet die Outs des jeweiligen PC-Spielers
	 * 
	 * @author sebastian
	 * @param round
	 * @param fiveColor
	 * @param tableCards
	 */
	private void calculateOuts(int round, int fiveColor, Card[] tableCards) {

		outs += fiveColorTry(round, tableCards);
		outs += streetTry(sortWorth(round, tableCards), round);
		outs += sameWorthTry(round, sortWorth(round, tableCards));

		if (round == 5) {
			outs = outs * 4;
		} else
			outs = outs * 2;

	}

	/**
	 * Prüft, ob ein Spieler Paare/Drillinge hat und wie viel Outs er somit hat
	 * 
	 * @author sebastian
	 * @param round
	 * @param cardWorth
	 * @return
	 */
	private int sameWorthTry(int round, int[] cardWorth) {

		int same = 1;
		int same2 = 1;
		int twins = 0;
		int[] sameWorth = new int[4];

		// Durchläuft alle Karten
		for (int a = 0; a < round; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]) {
				same = same + 1;
			}

			if (same == 3 && a != round - 1 && cardWorth[a] != cardWorth[a + 1]
					&& cardWorth[a] == cardWorth[a - 2]) {
				sameWorth[0] = same;
				sameWorth[1] = cardWorth[a - 1];
			} else {
				if ((same == 2) && (a != round - 1) && (twins == 0)
						&& (cardWorth[a] != cardWorth[a + 1])) {
					twins = 1;
					sameWorth[0] = same;
					sameWorth[1] = cardWorth[a - 1];
				}

			}
		}

		for (int a = 0; a < round; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]
					&& sameWorth[1] != cardWorth[a]) {
				same2 = same2 + 1;
				sameWorth[2] = same2;
				sameWorth[3] = cardWorth[a];

			}
		}
		if (sameWorth[0] == 3) {
			return 1;
		} else if (sameWorth[0] == 2 && sameWorth[2] == 2) {
			return 4;
		} else if (sameWorth[0] == 2 || sameWorth[2] == 2) {
			return 2;
		}
		return 0;
	}

	/**
	 * Überprüft, ob ein Spieler beinahe 5 Karten in einer Reihe (Straße) hat
	 * und gibt entsprechend die Outs zurück
	 * 
	 * @author sebastian
	 * @param cardWorth
	 * @param round
	 * @return
	 */
	private int streetTry(int[] cardWorth, int round) {
		int follow = 1;
		for (int a = 0; a < round; a++) {
			if ((a != 0 && a != round - 1
					&& cardWorth[a] == cardWorth[a - 1] + 1 && cardWorth[a] == cardWorth[a + 1] - 1)
					|| (a == round - 1
							&& cardWorth[round - 1] == cardWorth[round - 2] + 1 && cardWorth[round - 1] == cardWorth[round - 3] + 2)
					|| (a == 0 && cardWorth[a] == cardWorth[a + 1] - 1)
					&& cardWorth[a] == cardWorth[a + 2] - 2) {
				follow = follow + 1;

				if (follow >= 4) {
					return 8;
				}
			}

		}
		return 0;
	}

	/**
	 * Prüft, ob ein Spieler beinahe 5 Karten einer Farbe (Flush) hat und gibt
	 * entsprechend die Outs zurück
	 * 
	 * @author sebastian
	 * @param round
	 * @param tableCards
	 * @return
	 */
	private int fiveColorTry(int round, Card[] tableCards) {
		Card[] allCards = new Card[7];
		int color = -1;
		allCards[0] = this.getCards()[0];
		allCards[1] = this.getCards()[1];
		allCards[2] = tableCards[0];
		allCards[3] = tableCards[1];
		allCards[4] = tableCards[2];
		allCards[5] = tableCards[3];
		allCards[6] = tableCards[4];

		int[] colors = { 0, 0, 0, 0 };

		for (int i = 0; i < round; i++) {
			colors[allCards[i].getColorID()]++;
		}

		for (int i = 0; i < 4; i++) {
			if (colors[i] >= 4) {
				color = i; // Herz,Kreuz,Karo,Pik
			}

		}
		if (color != -1) // Wenn der Spieler 3 oder mehr Karten einer Farbe hat,
							// werden die outs dazugerechnet
			return 9;
		else
			return 0;

	}
}
