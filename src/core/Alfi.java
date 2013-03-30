package core;

import java.util.Random;

public class Alfi extends Player {
	
	Alfi(int credit) {
		super(credit);
		
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
			int round) {
		Random r = new Random();
		System.out.println("Ja");
		int[] sortedcards = sortWorth(round, tableCards,
				playerCards);
		int fiveColor = fiveColor(round, tableCards,
				playerCards);
		pairWorth = sameWorth(round,
				sortedcards);
		int followfive = followFive(round, sortedcards);
		handWorth = handWorth(sortedcards,
				fiveColor, followfive,
				pairWorth);
		if(handWorth>3)
			return 0;
		else {
			int random = r.nextInt(10);
			if(random > 7)
				return 2;
		}return 1;
	}

	public int decideThird(int money, Card[] tableCards, Card[] playerCards,
			int round) {
		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards,
				playerCards);
		int fiveColor = fiveColor(round, tableCards,
				playerCards);
		pairWorth = sameWorth(round,
				sortedcards);
		int followfive = followFive(round, sortedcards);
		handWorth = handWorth(sortedcards,
				fiveColor, followfive,
				pairWorth);
		if(handWorth>3)
			return 0;
		else {
			int random = r.nextInt(10);
			if(random > 7)
				return 2;
		}return 1;
	
	}

	public int decideLast(int money, Card[] tableCards, Card[] playerCards,
			int round) {
		Random r = new Random();
		int[] sortedcards = sortWorth(round, tableCards,
				playerCards);
		int fiveColor = fiveColor(round, tableCards,
				playerCards);
		pairWorth = sameWorth(round,
				sortedcards);
		int followfive = followFive(round, sortedcards);
		handWorth = handWorth(sortedcards,
				fiveColor, followfive,
				pairWorth);
		if(handWorth>3)
			return 0;
		else {
			int random = r.nextInt(10);
			if(random > 7)
				return 2;
		}return 1;
	}
	

}
