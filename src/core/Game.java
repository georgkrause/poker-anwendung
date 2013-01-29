package core;

import gui.Window;

import java.util.Random;

import core.Card;
import core.Player;

public class Game {

	public int cue = this.minimumBet;
	public Card[] tableCards = new Card[5];

	private int credit = 1000;
	private final int minimumBet = 100;

	private Card[] cards = new Card[52];
	private int givenCards = 0;

	private int smallBlind;
	private int bigBlind;
	private int dealer;
	private int turnPlayer;

	private int pot;
	public Player[] activePlayers = new Player[4];
	private int activePlayerNumber = 4;

	private Window window;

	private Random r = new Random();

	/**
	 * initialize a new game - creates all the cards - choose five table cards
	 */
	Game() {

		// Generate all cards
		this.generateCards();

		// create objects of the players
		this.generatePlayers();

		this.round();
	}

	private void round() {
		this.assignRoles();

		this.prepairPlayers();

		// choose table cards
		this.tableCards[0] = this.deal();
		this.tableCards[1] = this.deal();
		this.tableCards[2] = this.deal();
		this.tableCards[3] = this.deal();
		this.tableCards[4] = this.deal();

		window = new Window(this);

		this.collectBlinds();

		while (activePlayerNumber > 1 && !this.tableCards[4].isVisible()) {
			int movesWithoutRaise = 0;
			// Wettrunde
			while (movesWithoutRaise < activePlayerNumber) {

				System.out.println("Spieler " + turnPlayer + " am Zug.");

				int choice = 0;

				if (!activePlayers[turnPlayer].folded) {
					if (turnPlayer != 0) {
						choice = ((Alfi) this.activePlayers[turnPlayer])
								.decide();
					} else {
						do {
							choice = window.DialogBox();
						} while (choice < 0);
					}

					switch (choice) {
					case 0: // raise
						if (this.activePlayers[turnPlayer].raise(100)) {
							this.cue = 100;
							window.updateCredits();
							this.raisePot(100);
							window.updatePot();
							movesWithoutRaise = 0;
						}
						break;
					case 1: // call
						if (this.activePlayers[turnPlayer].call(cue)) {
							window.updateCredits();
							this.raisePot(cue);
							window.updatePot();
						}
						movesWithoutRaise++;
						break;
					case 2: // fold
						this.activePlayers[turnPlayer].fold();
						window.updatePlayerCards(turnPlayer);
						this.activePlayerNumber--;
						movesWithoutRaise++;
						break;
					}
					System.out.println("Z�ge: " + movesWithoutRaise);
				}
				if ((turnPlayer + 1) > 3)
					turnPlayer = turnPlayer + 1 - 4;
				else
					turnPlayer = turnPlayer + 1;
			}

			if (!this.tableCards[2].isVisible()) {
				for (int i = 0; i < 3; i++) {
					this.tableCards[i].discover();
					window.updateCommunityCards();
				}
			} else if (!this.tableCards[3].isVisible()) {
				this.tableCards[3].discover();
				window.updateCommunityCards();
			} else if (!this.tableCards[4].isVisible()) {
				this.tableCards[4].discover();
				window.updateCommunityCards();
			}
		}

	}

	private void prepairPlayers() {
		// Karten geben und Spieler wieder ins Spiel bringen
		for (int i = 0; i < 4; i++) {
			if (activePlayers[i] != null) {
				Card[] cards = { deal(), deal() };
				activePlayers[i].setCards(cards);
				activePlayers[i].folded = false;
			}
		}
		// show cards of the human player
		this.activePlayers[0].getCards()[0].discover();
		this.activePlayers[0].getCards()[1].discover();

	}

	private void assignRoles() {
		// choose dealer
		dealer = r.nextInt(4);

		// choose Blinds
		if ((dealer + 1) > 3)
			smallBlind = dealer + 1 - 4;
		else
			smallBlind = dealer + 1;

		if ((dealer + 2) > 3)
			bigBlind = dealer + 2 - 4;
		else
			bigBlind = dealer + 2;

		// choose first Player
		turnPlayer = dealer;

	}

	private void generatePlayers() {
		for (int i = 0; i < 4; i++) {
			if (i == 0) {
				this.activePlayers[i] = new Player(this.credit);
			} else {
				this.activePlayers[i] = new Alfi(this.credit);
			}
		}

	}

	private void collectBlinds() {
		this.activePlayers[this.bigBlind].raise(minimumBet);
		this.activePlayers[this.smallBlind].raise(minimumBet / 2);
		window.updateCredits();
		this.raisePot((int) (minimumBet * 1.5));
	}

	private void generateCards() {
		for (int colorCounter = 0; colorCounter < (Card.allColors.length); colorCounter++) {
			for (int worthCounter = 0; worthCounter < (Card.allWorths.length); worthCounter++) {
				int id = (worthCounter * Card.allColors.length + colorCounter + 1) - 1;
				this.cards[id] = new Card(colorCounter, worthCounter);
			}
		}
	}

	/**
	 * @return a random card
	 */
	public Card deal() {
		int randomCard = (int) (Math.random() * (this.cards.length - 1 - this.givenCards));
		Card card = this.cards[randomCard];

		for (int i = randomCard; i < this.cards.length - 1; i++) {
			this.cards[i] = this.cards[i + 1];
		}
		this.cards[this.cards.length - 1] = null;

		this.givenCards++;
		return card;
	}

	/**
	 * disburses the asset (the credits in the pot)
	 */
	void disburseAsset(Player winner) {
		winner.changeCredit(this.pot);
		this.pot = 0;
	}

	/**
	 * @return the player object which had won the round
	 */
	Player getWinner() {
		Player winner = new Player(1);
		return winner;
	}

	/**
	 * changes after each round the small and the big blind
	 */
	void changeBlinds() {

	}

	/**
	 * changes the dealer after each round
	 */
	void changeDealer() {

	}

	/**
	 * @return the minimumBet
	 */
	public int getMinimumBet() {
		return minimumBet;
	}

	/**
	 * @return the smallBlind
	 */
	public int getSmallBlind() {
		return smallBlind;
	}

	/**
	 * @return the bigBlind
	 */
	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * @return the pot
	 */
	public int getPot() {
		return pot;
	}

	/**
	 * 
	 * @param pot
	 */
	public void raisePot(int pot) {
		this.pot += pot;
		window.updatePot();
	}

	/**
	 * @return the dealer
	 */
	public int getDealer() {
		return dealer;
	}

	/**
	 * @return the activePlayerNumber
	 */
	public int getActivePlayerNumber() {
		return activePlayerNumber;
	}

	/**
	 * sorts the cards
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
	public int[] sortworth(int ccard0, int ccard1, int ccard2, int ccard3,
			int ccard4, int pcard0, int pcard1) {
		int[] allcardsworth = new int[7];
		allcardsworth[0] = ccard0;
		allcardsworth[1] = ccard1;
		allcardsworth[2] = ccard2;
		allcardsworth[3] = ccard3;
		allcardsworth[4] = ccard4;
		allcardsworth[5] = pcard0;
		allcardsworth[6] = pcard1;
		for (int a = 0; a < 7; a++) {
			int z = a;
			while ((z > 0) && (allcardsworth[z] < allcardsworth[z - 1])) {
				int hilf = allcardsworth[z];
				allcardsworth[z] = allcardsworth[z - 1];
				allcardsworth[z - 1] = hilf;
				z = z - 1;
			}
		}

		return allcardsworth;

	}

	/**
	 * returns wheather there are five cards from the same color
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
	public int fivecolor(int ccard0, int ccard1, int ccard2, int ccard3,
			int ccard4, int pcard0, int pcard1) {
		int[] allcardscolor = new int[7];
		allcardscolor[0] = ccard0;
		allcardscolor[1] = ccard1;
		allcardscolor[2] = ccard2;
		allcardscolor[3] = ccard3;
		allcardscolor[4] = ccard4;
		allcardscolor[5] = pcard0;
		allcardscolor[6] = pcard1;
		int herz = 0;
		int kreuz = 0;
		int karo = 0;
		int pik = 0;

		for (int a = 0; a < 7; a++) {

			switch (allcardscolor[a]) {
			case 0:
				herz = herz + 1;
				break;

			case 1:
				kreuz = kreuz + 1;
				break;

			case 2:
				karo = karo + 1;
				break;

			case 3:
				pik = pik + 1;
				break;

			}
		}

		if (herz >= 5) {
			return 0;
		} else if (kreuz >= 5) {
			return 1;
		} else if (karo >= 5) {
			return 2;
		} else if (pik >= 5) {
			return 3;
		} else {
			return 30000;
		}
	}

	/**
	 * counts pairs, threelings and fourlings
	 * 
	 * @param cardworth
	 * @return
	 */
	public int[] sameworth(int[] cardworth) {
		int same = 1;
		int same2 = 1;
		int[] sameworth = new int[4];

		// Durchl�uft alle Karten
		for (int a = 0; a < 7; a++) {
			if (a != 0 && cardworth[a] == cardworth[a - 1]) {
				same = same + 1;
			}
			if (same >= 4) {
				sameworth[0] = same;
				sameworth[1] = cardworth[a - 1];
				return sameworth;
			} else {
				if ((same == 3) && (a != 6)
						&& (cardworth[a] != cardworth[a + 1])
						&& (cardworth[a] == cardworth[a - 2])) {
					sameworth[0] = same;
					sameworth[1] = cardworth[a - 1];
				} else {
					if ((same == 2) && (a != 6)
							&& (cardworth[a] != cardworth[a + 1])) {
						sameworth[0] = same;
						sameworth[1] = cardworth[a];
					}

				}
			}
		}

		for (int a = 0; a < 7; a++) {
			if (a != 0 && cardworth[a] == cardworth[a - 1]
					&& sameworth[1] != cardworth[a]) {
				same2 = same2 + 1;
				sameworth[2] = same2;
				sameworth[3] = cardworth[a];
				return sameworth;
			}
		}
		return sameworth;
	}

	public int followfive(int[] cardworth) {
		int follow = 1;
		for (int a = 0; a < 7; a++) {
			if (a != 0 && cardworth[a] == cardworth[a - 1] + 1) {
				follow = follow + 1;
			}
			if (follow >= 5) {
				return cardworth[a];
			}
		}
		return 0;

	}

}
