package core;

import core.Card;
import core.Player;

public class Game {
	
	private Card[] cards = new Card[52];
	private int givenCards = 0;
	
	private final int minimumBet = 100;
	private Player smallBlind;
	private Player bigBlind;
	
	private int pot;
	public Card[] tableCards = new Card[5];
	
	public Player[] activePlayers = new Player[4];
	
	/**
	 * initialize a new game
	 * 	- creates all the cards
	 * 	- choose five table cards
	 * TODO #8: 
	 * 	- create Players
	 * 	- distribute the credits
	 */
	Game() {
		
		for(int colorCounter = 0; colorCounter < (Card.allColors.length); colorCounter++) {
			for(int worthCounter = 0; worthCounter < (Card.allWorths.length); worthCounter++) {
				int id = (worthCounter * Card.allColors.length + colorCounter + 1) - 1;
				this.cards[id] = new Card(colorCounter, worthCounter);
			}
		}
		
		this.tableCards[0] = this.deal();
		this.tableCards[1] = this.deal();
		this.tableCards[2] = this.deal();
		this.tableCards[3] = this.deal();
		this.tableCards[4] = this.deal();
		
		for(int i = 0; i < 4; i++) {
			Card[] cards = {deal(), deal()};
			this.activePlayers[i] = new Player(cards);
		}
	}
	
	/** 
	 * @return a random card
	 */
	public Card deal() {
		int randomCard = (int) (Math.random() * (this.cards.length - 1 - this.givenCards));
		Card card = this.cards[randomCard];
		
		for(int i = randomCard; i < this.cards.length-1; i++) {
			this.cards[i] = this.cards[i+1];
		}
		this.cards[this.cards.length-1] = null;
		
		this.givenCards++;
		return card;
	}
	
	/**
	 * disburses the asset (the credits in the pot)
	 */
	void disburseAsset(Player winner) {
		winner.raiseCredit(this.pot);
		this.pot = 0;
	}
	
	/**
	 * @return the player object which had won the round
	 * TODO #7
	 */
	Player getWinner() {
		Player winner = new Player(new Card[2]);
		return winner;
	}
	
	/**
	 * changes after every round the small and the big blind
	 * TODO #6
	 */
	void changeBlinds() {
		
	}
	
	/**
	 * changes the dealer after every round
	 * TODO #6
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
	public Player getSmallBlind() {
		return smallBlind;
	}

	/**
	 * @return the bigBlind
	 */
	public Player getBigBlind() {
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

}
