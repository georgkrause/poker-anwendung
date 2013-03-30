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
		return 1; // 2.Wettrunde, Handkarten+Flop
		// //(this.activePlayers[turnPlayer]
		// .getCredit(), this.tableCards,
		// this.activePlayers[turnPlayer]
		// .getCards(), 6);
		//
	}

	public int decideThird(int money, Card[] tableCards, Card[] playerCards,
			int round) {
		return 1;
		// Thread.sleep(5000);
		// Random r = new Random();
		// int random = r.nextInt(10);
		//
		// if(random < 6) {
		// return 1;
		// } else if (random < 8) {
		// return 0;
		// } else {
		// return 2;
		// }
	}

	public int decideLast(int money, Card[] tableCards, Card[] playerCards,
			int round) {
		return 1;
		// Thread.sleep(5000);
		// Random r = new Random();
		// int random = r.nextInt(10);
		//
		// if(random < 6) {
		// return 1;
		// } else if (random < 8) {
		// return 0;
		// } else {
		// return 2;
		// }
	}

}
