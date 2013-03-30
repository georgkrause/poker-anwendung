package core;

import java.util.Random;

public class Alfi extends Player {

	Alfi(int credit) {
		super(credit);
	}

	public int decideFirst(int money, Card[] playerCards) {
		Random r = new Random();

		if (playerCards[1].getWorthID() == playerCards[0].getWorthID()) {
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

	public int decideSecond(Card playerCard1, Card playerCard2) {

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

	public int decideThird() {

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

	public int decideLast() {

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
