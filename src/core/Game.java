package core;

import gui.Window;

import java.util.Random;

import core.Card;
import core.Player;

public class Game {

	public int cue = this.minimumBet;

	public Card[] tableCards = new Card[5];

	private int credit = 10000;
	private final int minimumBet = 100;
	private int raiseWorth = 100; // Testzweck, Wert des PC spielers fehlt TODO
	private int richPlayers = 0;

	private Card[] cards = new Card[52];
	private int givenCards = 0;

	private int smallBlind = 0;
	private int bigBlind = 0;
	private int dealer = 0;

	private int turnPlayer;
	private boolean end = false;

	private int pot;
	public Player[] activePlayers = new Player[4];
	private int activePlayerNumber = 4;

	private Window window;

	private Random r = new Random();

	/**
	 * initialize a new game - creates all the cards - choose five table cards
	 * 
	 * @throws InterruptedException
	 */
	Game() throws InterruptedException {

		// create objects of the players
		this.generatePlayers();

		window = new Window(this);

		while (!end) {
			richPlayers = 4;
			for (int i = 0; i < 4; i++) {
				if (activePlayers[i].getCredit() <= 0) {
					this.activePlayers[i].fold();
					richPlayers--;
				}
			}
			if (richPlayers == 1) {
				end = true;
				window.dispose();
				return;
			}
			this.round();
		}
	}

	private void round() throws InterruptedException {

		this.generateCards();

		this.prepairPlayers();

		this.assignRoles();

		this.activePlayerNumber = 4;
		// System.out.println(activePlayerNumber);

		window.updateDealer();

		// choose table cards
		this.tableCards[0] = this.deal();
		this.tableCards[1] = this.deal();
		this.tableCards[2] = this.deal();
		this.tableCards[3] = this.deal();
		this.tableCards[4] = this.deal();

		window.updateCommunityCards();

		this.collectBlinds();

		this.cue = minimumBet;

		while (activePlayerNumber > 1 && !this.tableCards[4].isVisible()) {
			int movesWithoutRaise = 0;
			// Wettrunde
			while (movesWithoutRaise < activePlayerNumber) {

				int choice = 0;

				if (!activePlayers[turnPlayer].isFolded()) {
					if (turnPlayer != 0) {
						choice = ((Alfi) this.activePlayers[turnPlayer])
								.decide();
					} else {
						do {
							choice = window.DialogBox();
						} while (choice < 0);

						if (choice == 0) {
							
							do {
								raiseWorth = window.RaiseDialogBox();
							} while (raiseWorth == 0 || (raiseWorth % 50 != 0));
						
							do {
								if (cue - activePlayers[turnPlayer].debt == 0) {
									raiseWorth = window.RaiseDialogBox();
								} else {
									raiseWorth = window.RaiseDialogBox();
								}
							} while (raiseWorth == 0 || (raiseWorth % 50 != 0));
							

						}
					}
					switch (choice) {
					case 0: // raise
						this.cue += raiseWorth;
						if (this.activePlayers[turnPlayer].raise(cue)) {
							window.updateCredits();
							this.raisePot(activePlayers[turnPlayer].debt);
							window.updatePot();
							movesWithoutRaise = 0;
							activePlayers[turnPlayer].debt = cue;
						}
						break;
					case 1: // call
						if (this.activePlayers[turnPlayer].call(cue)) {
							window.updateCredits();
							this.raisePot(activePlayers[turnPlayer].debt);
							window.updatePot();
							activePlayers[turnPlayer].debt = cue;
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
					System.out.println(turnPlayer + ": " + choice
							+ "; aktueller Einsatz" + cue + "; Pot: "
							+ this.pot);
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
				System.out.println("neue Karte");
			} else if (!this.tableCards[3].isVisible()) {
				this.tableCards[3].discover();
				window.updateCommunityCards();
				System.out.println("neue Karte");
			} else if (!this.tableCards[4].isVisible()) {
				this.tableCards[4].discover();
				window.updateCommunityCards();
				System.out.println("neue Karte");
			}
		}
		if (activePlayerNumber == 1) {
			for (int i = 0; i < 4; i++) {
				if (!activePlayers[i].isFolded()) {
					Thread.sleep(4000);
					disburseAsset(activePlayers[i]);
					window.updatePot();
					window.updateCredits();
					this.givenCards = 0;
					this.activePlayerNumber = 4;
					for (int x = 0; x < 4; x++) {
						this.activePlayers[i].folded = false;
					}
					this.generateCards();
				}
			}
		} else {
			if (this.tableCards[4].isVisible()) {
				int[] feld = new int[4];
				int[] highcards = new int[4];
				for (int i = 0; i < 4; i++) {
					if (!activePlayers[i].isFolded()) {
						int[] sortedcards = sortWorth(
								tableCards[0].getWorthID(),
								tableCards[1].getWorthID(),
								tableCards[2].getWorthID(),
								tableCards[3].getWorthID(),
								tableCards[4].getWorthID(),
								activePlayers[i].getCards()[0].getWorthID(),
								activePlayers[i].getCards()[1].getWorthID());
						int fivecolor = fiveColor(tableCards,
								activePlayers[i].getCards());
						int[] sameworthfield = sameWorth(sortedcards);
						int followfive = followFive(sortedcards);
						this.activePlayers[i].getCards()[0].discover();
						this.activePlayers[i].getCards()[1].discover();
						this.activePlayers[i].getCards()[0].getPicture();
						this.activePlayers[i].getCards()[1].getPicture();
						window.updatePlayerCards(i);
						window.updatePlayerCards(i);
						Thread.sleep(6000);

						feld[i] = handworth(sortedcards, fivecolor, followfive,
								sameworthfield);
						highcards[i] = sortedcards[6];
					}
				}
				
				int z = getWinner(feld, highcards);
				if(z<10){
				System.out.println("Winner: " + z + " " + feld[z]);
				disburseAsset(activePlayers[z]);
				window.updatePot();
				window.updateCredits();
				this.givenCards = 0;
				this.activePlayerNumber = 4; }
				else {System.out.println("Es gibt 2 Gewinner!");
				//In der Fkt. getWinner muss gesagt werden, WER die beiden Gewinner sind. TODO 
				}
				for (int i = 0; i < 4; i++) {
					this.activePlayers[i].folded = false;
				}
			}
		}

		Thread.sleep(4000);
	}

	private void prepairPlayers() {
		// Karten geben und Spieler wieder ins Spiel bringen

		this.turnPlayer = getTurnPlayer();

		for (int i = 0; i < 4; i++) {
			if (!activePlayers[i].isFolded()) {
				Card[] cards = { deal(), deal() };
				activePlayers[i].setCards(cards);
				activePlayers[i].folded = false;
				activePlayers[i].debt = 0;
			}
		}
		// show cards of the human player
		if (!this.activePlayers[0].isFolded()) {
			this.activePlayers[0].getCards()[0].discover();
			this.activePlayers[0].getCards()[1].discover();
		}

		for (int i = 0; i < 4; i++) {
			window.updatePlayerCards(i);
		}

	}

	private void assignRoles() {
		// choose dealer
		if (dealer == smallBlind) {
			dealer = r.nextInt(4);
		} else {
			dealer++;
		}

		// choose Blinds
		if (dealer == 4) {
			dealer = 0;
		}

		if ((dealer + 1) > 3)
			smallBlind = dealer + 1 - 4;
		else
			smallBlind = dealer + 1;

		if ((dealer + 2) > 3)
			bigBlind = dealer + 2 - 4;
		else
			bigBlind = dealer + 2;

		
		turnPlayer = getTurnPlayer();

	}

	private int getTurnPlayer() {
		int turnPlayer = this.dealer - 1;
		if (turnPlayer < 0) {
			turnPlayer = 3;
		}
		return turnPlayer;
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
		this.activePlayers[this.smallBlind].debt = minimumBet / 2;
		this.activePlayers[this.smallBlind].changeCredit(-minimumBet / 2);
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
	int getWinner(int[] handResults, int[] highCards) {
		int winners = 0;
		boolean twoWinners = false;

		for (int a = 1; a < handResults.length - 1; a++) {
			if (handResults[a] > handResults[winners]) {
				winners = a;

			} else {
				if (handResults[a] == handResults[winners]
						&& highCards[a] > highCards[winners]) {
					winners = a;

				} else {
					if (handResults[a] == handResults[winners]
							&& highCards[a] == highCards[winners])
						twoWinners=true;
				}
			}
		}
		if(twoWinners==true)
		return 1337;
		else return winners;
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
	public int[] sortWorth(int ccard0, int ccard1, int ccard2, int ccard3,
			int ccard4, int pcard0, int pcard1) {
		int[] allCardsWorth = new int[7];
		allCardsWorth[0] = ccard0;
		allCardsWorth[1] = ccard1;
		allCardsWorth[2] = ccard2;
		allCardsWorth[3] = ccard3;
		allCardsWorth[4] = ccard4;
		allCardsWorth[5] = pcard0;
		allCardsWorth[6] = pcard1;
		for (int a = 0; a < 7; a++) {
			int z = a;
			while ((z > 0) && (allCardsWorth[z] < allCardsWorth[z - 1])) {
				int hilf = allCardsWorth[z];
				allCardsWorth[z] = allCardsWorth[z - 1];
				allCardsWorth[z - 1] = hilf;
				z = z - 1;
			}
		}

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
	public int fiveColor(Card[] tableCards, Card[] playerCards) {
		Card[] allCards = new Card[7];
		// for(int i = 0; i < 2; i++) {
		// allCards[i] = playerCards[i];
		// }
		// for(int i = 2; i < 7; i++) {
		// allCards[i] = tableCards[i];
		// }
		allCards[0] = playerCards[0];
		allCards[1] = playerCards[1];
		allCards[2] = tableCards[0];
		allCards[3] = tableCards[1];
		allCards[4] = tableCards[2];
		allCards[5] = tableCards[3];
		allCards[6] = tableCards[4];

		int[] colors = { 0, 0, 0, 0 };

		for (int i = 0; i < 7; i++) {
			colors[allCards[i].getColorID()]++;
		}

		for (int i = 0; i < 4; i++) {
			if (colors[i] >= 5) {
				return i;
			}
		}
		return 30000;
	}

	/**
	 * counts pairs, threelings and fourlings
	 * 
	 * @param cardWorth
	 * @return
	 */
	public int[] sameWorth(int[] cardWorth) {
		int same = 1;
		int same2 = 1;
		int[] sameWorth = new int[4];

		// Durchläuft alle Karten
		for (int a = 0; a < 7; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]) {
				same = same + 1;
			}
			if (same >= 4 && cardWorth[a] == cardWorth[a - 3]) {
				sameWorth[0] = same;
				sameWorth[1] = cardWorth[a - 1];
				return sameWorth;
			} else {
				if (same == 3 && a != 6 && cardWorth[a] != cardWorth[a + 1]
						&& cardWorth[a] == cardWorth[a - 2]) {
					sameWorth[0] = same;
					sameWorth[1] = cardWorth[a - 1];
				} else {
					if (same == 2 && a != 6 && cardWorth[a] != cardWorth[a + 1]) {
						sameWorth[0] = same;
						sameWorth[1] = cardWorth[a - 1];
					}

				}
			}
		}

		for (int a = 0; a < 7; a++) {
			if (a != 0 && cardWorth[a] == cardWorth[a - 1]
					&& sameWorth[1] != cardWorth[a]) {
				same2 = same2 + 1;
				sameWorth[2] = same2;
				sameWorth[3] = cardWorth[a];

			}
		}
		return sameWorth;
	}

	public int followFive(int[] cardWorth) {
		int follow = 1;
		for (int a = 0; a < 7; a++) {
			if ((a != 0 && a != 6 && cardWorth[a] == cardWorth[a - 1] + 1 && cardWorth[a] == cardWorth[a + 1] - 1)
					|| (a == 6 && cardWorth[6] == cardWorth[5] + 1)) {
				follow = follow + 1;
			}
			if (follow >= 5) {
				return cardWorth[a];
			}
		}
		return 0;

	}

	/**
	 * gives back the worth of the hand
	 * 
	 * @param cardWorth
	 * @param fiveColor
	 * @param followFive
	 * @param sameWorth
	 * @return
	 */

	public int handworth(int[] cardWorth, int fiveColor, int followFive,
			int[] sameWorth) {

		if ((followFive == 12) & (fiveColor != 30000)) { // RoyalFlush
			return 10;
		} else {
			if (followFive != 0 && fiveColor != 30000) { // Flush
				return 9;
			} else {
				if (sameWorth[0] == 4) { // Vierling
					return 8;
				} else {
					if (sameWorth[0] == 3 && sameWorth[2] == 2) { // drilling+pair
						return 7;
					} else {
						if (fiveColor != 30000) { // 5 from one color
							return 6;
						} else {
							if (followFive != 0) { // street
								return 5;
							} else {
								if (sameWorth[0] == 3 || sameWorth[2] == 3) { // drilling
									return 4;
								} else {
									if (sameWorth[0] == 2 && sameWorth[2] == 2) { // two
																					// pairs
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
