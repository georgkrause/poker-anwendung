package core;

import gui.Window;

import java.util.Random;

import core.Card;
import core.Player;

public class Game {

	public int cue = 0;

	public Card[] tableCards = new Card[5];

	private int credit = 10000; // Startkapital des Spieler
	private final int minimumBet = 100; // Mindesteinsatz
	private int raiseWorth = 100; // TODO Testzweck, Wert des PC spielers fehlt

	private Card[] cards = new Card[52]; // Array für die Spielkarten
	private int givenCards = 0; // Anzahl der Karten die bereits ausgegeben
								// wurden

	private int smallBlind = 0; // ID des Spielers, der SmallBlind ist
	private int bigBlind = 0; // ID des Spielers, der BigBlind ist
	private int dealer = 0; // ID des Spielers, der Dealer ist

	private int turnPlayer; // ID des Spielers, der dran ist
	private boolean end = false; // wird true, wenn Spiel beendet ist

	private int pot; // Enthält Betrag des Pots
	public Player[] activePlayers = new Player[4]; // Array mit aktiven Spielern
	private int activePlayerNumber = 4; // Anzahl der aktiven Spieler

	private Window window; // Fenster-Objekt

	private Random r = new Random(); // Objekt zum generieren von Zufallszahlen

	

	/**
	 * initialisiert ein neues Spiel - bringt Spieler neu ins Spiel - startet
	 * die Main-Loop
	 * 
	 * @throws InterruptedException
	 */
	Game() throws InterruptedException {

		// erzeugt die Objekte der Spieler
		this.generatePlayers();

		// erzeugt das Fenster
		window = new Window(this);

		// Main-Loop bis Spiel beendet ist
		while (!end) {
			activePlayerNumber = 4; // Anzahl der Spieler, die noch Geld haben

			// durchläuft alle Spieler und prüft ihren Kontostand
			for (int i = 0; i < 4; i++) {
				if (activePlayers[i].getCredit() <= 0) {
					this.activePlayers[i].fold();
					activePlayerNumber--;
				}
			}

			// beende das Spiel, wenn nur noch ein Spieler Geld hat
			if (activePlayerNumber == 1) {
				end = true;
				window.dispose(); // schließe das Fenster
				return;
			}

			// führe eine Runde aus
			this.round();
		}
	}

	/**
	 * eine Spielrunde
	 * 
	 * @throws InterruptedException
	 */
	private void round() throws InterruptedException {

		// erzeugt alle Spielkarten
		this.generateCards();

		// gibt Handkarten
		this.preparePlayers();

		// verteilt Blinds, Dealer und TurnPlayer
		this.assignRoles();

		// Aktualisiert die Anzeige der Rollen
		window.updateDealer();

		// gebe Community-Cards
		this.tableCards[0] = this.deal();
		this.tableCards[1] = this.deal();
		this.tableCards[2] = this.deal();
		this.tableCards[3] = this.deal();
		this.tableCards[4] = this.deal();

		// aktualisiere Anzeige der Community-Karten
		window.updateCommunityCards();

		// zieht den Spielern die Blinds ab
		this.collectBlinds();

		// Setzt den aktuellen Einsatz auf den Mindesteinsatz
		this.cue = minimumBet;

		while (activePlayerNumber > 1 && !this.tableCards[4].isVisible()) {
			int movesWithoutRaise = 0; // Zählt Runden, in denen nicht erhöht
										// wurde

			// Wettrunde
			while (movesWithoutRaise < activePlayerNumber) { // Wenn alle
																// Spieler
																// hintereinander
																// nicht erhöht
																// haben

				int choice = 0;

				if (!activePlayers[turnPlayer].isFolded()) {
					if (turnPlayer != 0) {
						// Lässt KI entscheiden was getan werden soll
						choice = ((Alfi) this.activePlayers[turnPlayer])
								.decide();
					} else {
						do {
							// Lässt Spieler entscheiden was getan werden soll
							choice = window.DialogBox();
						} while (choice < 0);

						// Wenn Spieler erhöhen möchte, frage neuen Wert ab
						if (choice == 0) {

							do {
								raiseWorth = window.RaiseDialogBox();
							} while (raiseWorth == 0 || (raiseWorth % 50 != 0));

						}
					}

					// TODO #27: Das muss hier unbedingt überarbeitet werden!
					switch (choice) {
					case 0: // raise/erhöhen
						if (this.activePlayers[turnPlayer].raise(cue)) {
							// erhöht den Einsatz
							this.cue += raiseWorth;
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

					// TODO: Vor der Abgabe entfernen
					System.out.println(turnPlayer + ": " + choice
							+ "; aktueller Einsatz" + cue + "; Pot: "
							+ this.pot);
				}

				// Ermittelt Spieler, der als nächstes dran ist
				if ((turnPlayer + 1) > 3)
					turnPlayer = turnPlayer + 1 - 4;
				else
					turnPlayer = turnPlayer + 1;
			}

			// Decke die ersten 3 Karten auf falls noch nicht erledigt
			if (!this.tableCards[2].isVisible()) {
				for (int i = 0; i < 3; i++) {
					this.tableCards[i].discover();
					window.updateCommunityCards();
				}

				// TODO: Vor der Abgabe entfernen
				System.out.println("neue Karte");

				// Decke 4 Karte auf falls noch nicht erledigt
			} else if (!this.tableCards[3].isVisible()) {
				this.tableCards[3].discover();
				window.updateCommunityCards();

				// TODO: Vor der Abgabe entfernen
				System.out.println("neue Karte");

				// Decke 5. Karte auf falls noch nicht erledigt
			} else if (!this.tableCards[4].isVisible()) {
				this.tableCards[4].discover();
				window.updateCommunityCards();

				// TODO: Vor der Abgabe entfernen
				System.out.println("neue Karte");
			}
		}

		// Wenn nur noch ein Spieler übrig ist wird der Gewinn an diesen
		// ausgezahlt
		if (activePlayerNumber == 1) {
			for (int i = 0; i < 4; i++) {
				if (!activePlayers[i].isFolded()) {
					// TODO: Muss die Pause hier sein?
					Thread.sleep(4000);
					disburseAsset(activePlayers[i]); // Schütte Gewinn aus
				}

			}
			for (int i = 0; i < 4; i++) {
				this.activePlayers[i].folded = false;
			}

			window.updatePot(); // Aktualisiere die Pot-Anzeige
			window.updateCredits(); // Aktualisiere die Anzeige der
									// Spieler-Guthaben

		} else {
			if (this.tableCards[4].isVisible()) {

				for (int i = 0; i < 4; i++) {
					if (!activePlayers[i].isFolded()) {
						int[] sortedcards = sortWorth(tableCards,
								activePlayers[i].getCards());
						int fiveColor = fiveColor(tableCards,
								activePlayers[i].getCards());
						this.activePlayers[i].pairWorth = sameWorth(sortedcards);
						int followfive = followFive(sortedcards);
						if (followfive != 0)
							activePlayers[i].highCard = followfive;

						this.activePlayers[i].getCards()[0].discover();
						this.activePlayers[i].getCards()[1].discover();
						this.activePlayers[i].getCards()[0].getPicture();
						this.activePlayers[i].getCards()[1].getPicture();
						window.updatePlayerCards(i);
						window.updatePlayerCards(i);
						Thread.sleep(10000);

						activePlayers[i].handWorth = handWorth(sortedcards,
								fiveColor, followfive,
								this.activePlayers[i].pairWorth);

					}
				}

				int[] z = getWinner();

				System.out.println("Winner: " + z[0] + " "
						+ activePlayers[z[0]].handWorth+" Es gibt "+z[1]+" Gewinner.");
				disburseAsset(activePlayers[z[0]]);
				window.updatePot();
				window.updateCredits();
				this.givenCards = 0;
				this.activePlayerNumber = 4;

				for (int i = 0; i < 4; i++) {
					this.activePlayers[i].folded = false;
				}
			}
		}

		Thread.sleep(10000);
	}

	/**
	 * gibt Handkarten
	 */
	private void preparePlayers() {

		// gebe jedem Spieler 2 Karten
		for (int i = 0; i < 4; i++) {
			if (!activePlayers[i].isFolded()) {
				Card[] cards = { deal(), deal() };
				activePlayers[i].setCards(cards);
				activePlayers[i].folded = false;
				activePlayers[i].debt = 0;
				activePlayers[i].handWorth=0;
				activePlayers[i].highCard=0;
			}
		}

		// zeige die Karten des menschlichen Spielers
		if (!this.activePlayers[0].isFolded()) {
			this.activePlayers[0].getCards()[0].discover();
			this.activePlayers[0].getCards()[1].discover();
		}

		// aktualisiere das Fenster um die Karten anzuzeigen
		for (int i = 0; i < 4; i++) {
			window.updatePlayerCards(i);
		}

	}

	/**
	 * verteilt die Rollen an die Spieler
	 */
	private void assignRoles() {
		// wählt Dealer aus
		if (dealer == smallBlind) {
			dealer = r.nextInt(4);
		} else {
			dealer++;
		}

		if (dealer == 4) {
			dealer = 0;
		}

		// wählt Blinds aus
		if ((dealer + 1) > 3)
			smallBlind = dealer + 1 - 4;
		else
			smallBlind = dealer + 1;

		if ((dealer + 2) > 3)
			bigBlind = dealer + 2 - 4;
		else
			bigBlind = dealer + 2;

		// wählt Spieler aus, der zuerst dran ist
		turnPlayer = getTurnPlayer();

	}

	/**
	 * ermittelt den Spieler, der zuerst dran ist
	 * 
	 * @return
	 */
	private int getTurnPlayer() {
		int turnPlayer = this.dealer - 1;
		if (turnPlayer < 0) {
			turnPlayer = 3;
		}
		return turnPlayer;
	}

	/**
	 * erzeugt für jeden Spieler ein Objekt
	 */
	private void generatePlayers() {
		for (int i = 0; i < 4; i++) {
			if (i == 0) {
				this.activePlayers[i] = new Player(this.credit);
			} else {
				this.activePlayers[i] = new Alfi(this.credit);
			}
		}

	}

	/**
	 * Zieht den Blinds das Geld ab und schiebt es in den Pot
	 */
	private void collectBlinds() {
		this.activePlayers[this.bigBlind].raise(minimumBet);
		this.activePlayers[this.smallBlind].debt = minimumBet / 2;
		this.activePlayers[this.smallBlind].changeCredit(-minimumBet / 2);
		window.updateCredits();
		this.raisePot((int) (minimumBet * 1.5));
	}

	/**
	 * erzeugt die Spielkarten und schreibt sie in ein Array
	 */
	private void generateCards() {
		// durchläuft alle Farben
		for (int colorCounter = 0; colorCounter < (Card.allColors.length); colorCounter++) {
			// durchläuft alle Werte
			for (int worthCounter = 0; worthCounter < (Card.allWorths.length); worthCounter++) {
				// errechnet ID der aktuellen Karte
				int id = worthCounter * Card.allColors.length + colorCounter;
				// Erzeugt ein Objekt für die Karte und schreibt sie in ein
				// Array
				this.cards[id] = new Card(colorCounter, worthCounter);
			}
		}

		// setze die Anzahl der ausgegebenen Karten auf 0 zurück
		this.givenCards = 0;
	}

	/**
	 * @return zufällige Karte
	 */
	public Card deal() {
		// Wählt Karte aus
		int randomCard = (int) (Math.random() * (this.cards.length - 1 - this.givenCards));
		Card card = this.cards[randomCard];

		// nimmt Karte aus dem Array und schiebt leere Stelle ans Ende
		for (int i = randomCard; i < this.cards.length - 1; i++) {
			this.cards[i] = this.cards[i + 1];
		}
		this.cards[this.cards.length - 1] = null;

		// zählt die ausgegebenen Karten mit
		this.givenCards++;
		return card;
	}

	/**
	 * Zahlt den Gewinn aus
	 */
	void disburseAsset(Player winner) {
		winner.changeCredit(this.pot);
		this.pot = 0;
	}

	/**
	 * @return Spieler, der die Runde gewonnen hat
	 */
	int[] getWinner() {
		int []winnerArray=new int [2];
		int winnerAmount=1;
		int multipleWinners=-1;
		int winners = 0;

		for (int a = 1; a < 4; a++) {
			if (activePlayers[a].handWorth > activePlayers[winners].handWorth) {
				winners = a;
			} else {
				if (activePlayers[a].handWorth == activePlayers[winners].handWorth
						&& activePlayers[a].highCard > activePlayers[winners].highCard
						&& activePlayers[a].handWorth != 2 && activePlayers[a].handWorth != 3
						&& activePlayers[a].handWorth != 4 && activePlayers[a].handWorth != 7
						&& activePlayers[a].handWorth != 8) {
					winners = a;
				} else if (activePlayers[a].handWorth == activePlayers[winners].handWorth
						&& (activePlayers[a].handWorth == 2 || activePlayers[a].handWorth == 3
								|| activePlayers[a].handWorth == 4 || activePlayers[a].handWorth == 7 
								|| activePlayers[a].handWorth == 8)) {
					
					switch(activePlayers[a].handWorth){
					
					case 8: //Vierling
						if(activePlayers[a].pairWorth[1]>activePlayers[winners].pairWorth[1])
							winners=a;
							System.out.println("Gewinn durch" +activePlayers[a].pairWorth[1]);
						if(activePlayers[a].pairWorth[1]==activePlayers[winners].pairWorth[1])
							if(activePlayers[a].highCard>activePlayers[winners].highCard)
								winners=a;
							else{
							winnerAmount++;
							multipleWinners=8;}
						break;
						
					case 7: //FullHouse
						if(activePlayers[a].pairWorth[1]>activePlayers[winners].pairWorth[1])
							winners=a;
							System.out.println("Gewinn durch" +activePlayers[a].pairWorth[1]);
						if(activePlayers[a].pairWorth[1]==activePlayers[winners].pairWorth[1])
							if(activePlayers[a].pairWorth[3]>activePlayers[winners].pairWorth[3])
								winners=a;
							else{
							winnerAmount++;
							multipleWinners=7;}
						break;
						
					case 4: //Drilling
						if(activePlayers[a].pairWorth[1]>activePlayers[winners].pairWorth[1])
							winners=a;
							System.out.println("Gewinn durch" +activePlayers[a].pairWorth[1]);
						if(activePlayers[a].pairWorth[1]==activePlayers[winners].pairWorth[1])
							if(activePlayers[a].highCard>activePlayers[winners].highCard)
								winners=a;
							else{
							winnerAmount++;
							multipleWinners=8;}
						break;
						
					case 3: //Zwei Paare
						if(activePlayers[a].pairWorth[3]>activePlayers[winners].pairWorth[3])
							winners=a;
							System.out.println("Gewinn durch" +activePlayers[a].pairWorth[1]);
						if(activePlayers[a].pairWorth[3]==activePlayers[winners].pairWorth[3])
							winnerAmount++;
							multipleWinners=3;
						break;
						
					case 2: //Paar
						if(activePlayers[a].pairWorth[1]>activePlayers[winners].pairWorth[1])
							winners=a;
							System.out.println("Gewinn durch" +activePlayers[a].pairWorth[1]);
						if(activePlayers[a].pairWorth[1]==activePlayers[winners].pairWorth[1])
							winnerAmount++;
							multipleWinners=2;
						break;
					}
				}
			}
		}
		if(activePlayers[winners].handWorth==multipleWinners){
			System.out.println("Es gibt " +winnerAmount+" Gewinner");}
			winnerArray[0]=winners;
			winnerArray[1]=winnerAmount;
		return winnerArray;
	}

	/**
	 * TODO wechselt Blinds nach jeder Runde
	 */
	void changeBlinds() {

	}

	/**
	 * TODO welchselt den Dealer nach jeder Runde
	 */
	void changeDealer() {

	}

	/**
	 * @return Mindesteinsatz
	 */
	public int getMinimumBet() {
		return minimumBet;
	}

	/**
	 * @return Spieler, der SmallBlind ist
	 */
	public int getSmallBlind() {
		return smallBlind;
	}

	/**
	 * @return Spieler, der BigBlind ist
	 */
	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * @return Pot-Betrag
	 */
	public int getPot() {
		return pot;
	}

	/**
	 * erhöht den Pot
	 * 
	 * @param Pot
	 */
	public void raisePot(int pot) {
		this.pot += pot;
		window.updatePot();
	}

	/**
	 * @return Spieler, der Dealer ist
	 */
	public int getDealer() {
		return dealer;
	}

	/**
	 * @return Anzahl der aktiven Spieler
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
	public int[] sortWorth(Card[] tableCards, Card[] cards) {
		// TODO Auto-generated method stub

		int[] allCardsWorth = new int[7];
		allCardsWorth[0] = cards[0].getWorthID();
		allCardsWorth[1] = cards[1].getWorthID();
		allCardsWorth[2] = tableCards[0].getWorthID();
		allCardsWorth[3] = tableCards[1].getWorthID();
		allCardsWorth[4] = tableCards[2].getWorthID();
		allCardsWorth[5] = tableCards[3].getWorthID();
		allCardsWorth[6] = tableCards[4].getWorthID();
		for (int a = 0; a < 7; a++) {
			int z = a;
			while ((z > 0) && (allCardsWorth[z] < allCardsWorth[z - 1])) {
				int hilf = allCardsWorth[z];
				allCardsWorth[z] = allCardsWorth[z - 1];
				allCardsWorth[z - 1] = hilf;
				z = z - 1;
			}
		}
		for (int a = 0; a < 7; a++) {
			System.out.print(allCardsWorth[a]);
		}
		System.out.println();
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
		int twins = 0;
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
					if ((same == 2) && (a != 6) && (twins == 0)
							&& (cardWorth[a] != cardWorth[a + 1])) {
						twins = 1;
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

				if (follow >= 4) {

					return cardWorth[a];
				}
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

	public int handWorth(int[] cardWorth, int fiveColor, int followFive,
			int[] sameWorth) {

		if ((followFive == 12) & (fiveColor != 30000)) { // RoyalFlush
			return 10;
		} else {
			if (followFive != 0 && fiveColor != 30000) { //Straight Flush
				return 9;
			} else {
				if (sameWorth[0] == 4) { // Vierling
					return 8;
				} else {
					if (sameWorth[0] == 3 && sameWorth[2] == 2) { // drilling+pair
						System.out
								.println(sameWorth[0] + " " + sameWorth[1]
										+ " " + sameWorth[2] + " "
										+ sameWorth[3] + " ");
						return 7;
					} else {
						if (fiveColor != 30000) { // 5 from one color
							return 6;
						} else {
							if (followFive != 0) { // street
								return 5;
							} else {
								if (sameWorth[0] == 3 || sameWorth[2] == 3) { // drilling
									System.out.println(sameWorth[0] + " "
											+ sameWorth[1] + " " + sameWorth[2]
											+ " " + sameWorth[3] + " ");
									return 4;
								} else {
									if (sameWorth[0] == 2 && sameWorth[2] == 2) { // two
																					// pairs
										System.out.println(sameWorth[0] + " "
												+ sameWorth[1] + " "
												+ sameWorth[2] + " "
												+ sameWorth[3] + " "); 
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
