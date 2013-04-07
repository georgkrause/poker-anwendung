package core;

import java.util.Random;

public class Alfi extends Player {
	float outs = 0;

	Alfi(int credit) {
		super(credit);

	}

	public int decide(Card[] tableCards, int pot, int cue) {
		int choice;
		if (!tableCards[1].isVisible())
			choice = this.decideFirst(this.getCredit(), this.getCards());
		else if (!tableCards[3].isVisible())
			choice = this.decideSecond(this.getCredit(), tableCards,
					this.getCards(), 5, pot, cue);
		else if (!tableCards[4].isVisible())
			choice = this.decideThird(this.getCredit(), tableCards,
					this.getCards(), 6, pot, cue);
		else
			choice = this.decideLast(this.getCredit(), tableCards,
					this.getCards(), 7, pot, cue);
		return choice;
	}

	public int decideFirst(int money, Card[] playerCards) { // 1.Wettrunde,nur 2
															// Handkarten
		Random r = new Random();

		if (playerCards[1].getWorthID() == playerCards[0].getWorthID()
				|| money > 15000) {
			int random = r.nextInt(10);
			if (random > 7)
				return 0;
		} else if (money < 3000) {
			int random = r.nextInt(10);
			if (random > 6)
				return 2;
		}
		return 1;
	}

	public int decideSecond(int money, Card[] tableCards, Card[] playerCards,
			int round, int pot, int cue) {

		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards, playerCards);
		int fiveColor = fiveColor(round, tableCards, playerCards);
		pairWorth = sameWorth(round, sortedcards);
		int followfive = followFive(round, sortedcards);
		handWorth = handWorth(sortedcards, fiveColor, followfive, pairWorth);
		if (handWorth > 3) {
			int random = r.nextInt(10);
			if (random > 6) {
				return 0;
			} else {
				if (handWorth < 5 && (cue - debt) > money / 2)
					return 2;
				else
					return 1;
			}
		} else {
			calculateOuts(5, fiveColor, tableCards, playerCards);
			System.out.println(outs + " ");
			if (outs > ((cue - debt) / pot) * 100) {
				int random = r.nextInt(10);
				if (random > 9)
					return 2;
				else if ((random > 7 && outs / 2 > ((cue - debt) / pot) * 100)
						|| money > 15000)
					return 0;
				else
					return 1;
			} else {
				int random = r.nextInt(10);
				if (random > 7 && outs * 1.5 > ((cue - debt) / pot) * 100
						&& money > 4000)
					return 1;
				else
					return 2;
			}
		}
	}

	public int decideThird(int money, Card[] tableCards, Card[] playerCards,
			int round, int pot, int cue) {
		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards, playerCards);
		int fiveColor = fiveColor(round, tableCards, playerCards);
		pairWorth = sameWorth(round, sortedcards);
		int followfive = followFive(round, sortedcards);
		handWorth = handWorth(sortedcards, fiveColor, followfive, pairWorth);
		if (handWorth > 3) {
			int random = r.nextInt(10);
			if (random > 7) {
				return 0;
			} else {
				if (handWorth < 5 && (cue - debt) > money / 2)
					return 2;
				else
					return 1;
			}
		} else {
			calculateOuts(5, fiveColor, tableCards, playerCards);
			System.out.println(outs + " ");
			if (outs > ((cue - debt) / pot) * 100) {
				int random = r.nextInt(10);
				if (random > 9)
					return 2;
				else if ((random > 7 && outs / 2 > ((cue - debt) / pot) * 100)
						|| money > 15000)
					return 0;
				else
					return 1;
			} else {
				int random = r.nextInt(10);
				if (random > 7 && outs * 1.5 > ((cue - debt) / pot) * 100
						&& money > 4000)
					return 1;
				else
					return 2;
			}
		}
	}

	public int decideLast(int money, Card[] tableCards, Card[] playerCards,
			int round, int pot, int cue) {
		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards, playerCards);
		int fiveColor = fiveColor(round, tableCards, playerCards);
		pairWorth = sameWorth(round, sortedcards);
		int followfive = followFive(round, sortedcards);
		handWorth = handWorth(sortedcards, fiveColor, followfive, pairWorth);
		 int random = 0;
		if (handWorth > 6){
			 random = r.nextInt(10);
			 if(random > 5)
				 return 0;
			 else return 1;
		 }else if(handWorth < 6 && cue-debt > money * 0.75)
			 random = r.nextInt(10);
		 if(random > 6)
			 return 2;
		 else return 1;
}

	private void calculateOuts(int round, int fiveColor, Card[] tableCards,
			Card[] playerCards) {

		outs += fiveColorTry(round, tableCards, playerCards);
		outs += streetTry(sortWorth(round, tableCards, playerCards), round);
		outs += sameWorthTry(round, sortWorth(round, tableCards, playerCards));

		if (round == 5) {
			outs = outs * 4;
		} else
			outs = outs * 2;

	}

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

	private int fiveColorTry(int round, Card[] tableCards, Card[] playerCards) {
		Card[] allCards = new Card[7];
		int color = -1;
		allCards[0] = playerCards[0];
		allCards[1] = playerCards[1];
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
			return 13 - color;
		else
			return color;

	}
}
