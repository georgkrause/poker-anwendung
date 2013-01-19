package core;

import gui.Fenstor;

public class Main {

	public static void main(String Args[]) {
		Game game = new Game();
		new Fenstor(game);
		for (int i = 0; i < 10; i++) {
			Card card = game.deal();
			System.out.println(card.getName());
		}
		game.tableCards[0].discover();
		game.tableCards[0].getPicture();
		
		game.activePlayers[0].getCards()[0].getPicture();
		
	}

}
