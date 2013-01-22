package core;

import gui.Window;

import java.util.Random;

import core.Card;
import core.Player;

public class Game {

	private int credit = 1000;
	public int cue = this.minimumBet;

	private Card[] cards = new Card[52];
	private int givenCards = 0;

	private final int minimumBet = 100;
	private int smallBlind;
	private int bigBlind;
	private int dealer;
	private int turnPlayer;

	private int pot;
	public Card[] tableCards = new Card[5];

	public Player[] activePlayers = new Player[4];
	private int activePlayerNumber = 4;

	Random r = new Random();

	/**
	 * initialize a new game - creates all the cards - choose five table cards
	 */
	Game() {

		for (int colorCounter = 0; colorCounter < (Card.allColors.length); colorCounter++) {
			for (int worthCounter = 0; worthCounter < (Card.allWorths.length); worthCounter++) {
				int id = (worthCounter * Card.allColors.length + colorCounter + 1) - 1;
				this.cards[id] = new Card(colorCounter, worthCounter);
			}
		}

		for (int i = 0; i < 4; i++) {
			this.activePlayers[i] = new Player(this.credit);
		}

		dealer = r.nextInt(4);

		if ((dealer + 1) > 3)
			smallBlind = dealer + 1 - 4;
		else
			smallBlind = dealer + 1;

		if ((dealer + 2) > 3)
			bigBlind = dealer + 2 - 4;
		else
			bigBlind = dealer + 2;

		turnPlayer = dealer;

		// Blinds einzahlen
		this.activePlayers[smallBlind].changeCredit(-this.minimumBet / 2);
		this.activePlayers[bigBlind].changeCredit(-minimumBet);

		// Karten geben und Spieler wieder ins Spiel bringen
		for (int i = 0; i < 4; i++) {
			if (activePlayers[i] != null) {
				Card[] cards = { deal(), deal() };
				activePlayers[i].setCards(cards);
				activePlayers[i].folded = false;
			}
		}

		this.activePlayers[0].getCards()[0].discover();
		this.activePlayers[0].getCards()[1].discover();

		this.tableCards[0] = this.deal();
		this.tableCards[1] = this.deal();
		this.tableCards[2] = this.deal();
		this.tableCards[3] = this.deal();
		this.tableCards[4] = this.deal();

		Window window = new Window(this);

		while (activePlayerNumber > 1) {
			int choice = 0;
			if ((turnPlayer + 1) > 3)
				turnPlayer = turnPlayer + 1 - 4;
			else
				turnPlayer = turnPlayer + 1;

			if (!activePlayers[turnPlayer].folded) {
				if (turnPlayer != 0) {
					choice = 1;
				} else {
					choice = window.DialogBox();
				}

				switch (choice) {
				case 0: // raise
					if (this.activePlayers[turnPlayer].raise(100)) {
						this.cue = 100;
						window.updateCredits();
					}
					break;
				case 1: // call
					if (this.activePlayers[turnPlayer].call(cue)) {
						window.updateCredits();
					}
					break;
				case 2: // fold
					this.activePlayers[turnPlayer].fold();
					window.updatePlayerCards(turnPlayer);
					break;
				}
			}
			System.out.println(turnPlayer);
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

}
