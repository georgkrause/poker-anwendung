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

	private Card[] cards = new Card[52]; // Array f�r die Spielkarten
	private int givenCards = 0; // Anzahl der Karten die bereits ausgegeben
								// wurden

	private int smallBlind = 0; // ID des Spielers, der SmallBlind ist
	private int bigBlind = 0; // ID des Spielers, der BigBlind ist
	private int dealer = 0; // ID des Spielers, der Dealer ist

	private int turnPlayer; // ID des Spielers, der dran ist
	private boolean end = false; // wird true, wenn Spiel beendet ist

	private int pot; // Enth�lt Betrag des Pots
	public Player[] activePlayers = new Player[4]; // Array mit aktiven Spielern
	private int activePlayerNumber = 4; // Anzahl der aktiven Spieler
	private int validPlayers = 4; // Anzahl der Spieler, die noch genug Geld
									// haben

	private Window window; // Fenster-Objekt

	private Random r = new Random(); // Objekt zum generieren von Zufallszahlen

	/**
	 * initialisiert ein neues Spiel - bringt Spieler neu ins Spiel - startet
	 * die Main-Loop
	 * 
	 * @author georg
	 * @throws InterruptedException
	 */
	Game() throws InterruptedException {

		// erzeugt die Objekte der Spieler
		this.generatePlayers();

		// erzeugt das Fenster
		window = new Window(this);

		// Main-Loop bis Spiel beendet ist
		while (!end) {
			activePlayerNumber = 4; // Anzahl der Spieler, die noch in der Runde
									// sind
			validPlayers = 4; // Anzahl der Spieler, die noch Geld haben

			// durchl�uft alle Spieler und pr�ft ihren Kontostand
			for (int i = 0; i < 4; i++) {
				if (activePlayers[i].getCredit() <= 0) {

					this.activePlayers[i].fold();
					activePlayerNumber--;
					validPlayers--;
				}
			}

			// beende das Spiel, wenn nur noch ein Spieler Geld hat
			if (validPlayers == 1 || this.activePlayers[0].getCredit() <= 0) {
				end = true; // Spiel endet, wenn nur noch ein Spieler Geld hat
							// oder der menschliche Spieler kein Geld hat
				for (int i = 0; i < 4; i++) {
					if (this.activePlayers[i].getCredit() > 0)
						window.doWinDialog(this.activePlayers[i].getId());
				} // Zeigt Gewinnerbildschirm
				window.dispose(); // schlie�e das Fenster
				return;
			}

			// f�hre eine Runde aus
			this.round();
		}
	}

	/**
	 * eine Spielrunde
	 * 
	 * @author Georg, Sebastian
	 * @throws InterruptedException
	 */
	private void round() throws InterruptedException {

		// erzeugt alle Spielkarten
		this.generateCards();

		// gibt Handkarten
		this.preparePlayers();

		// verteilt Blinds, Dealer und TurnPlayer
		this.assignRoles();

		if (validPlayers > 3) {
			// Aktualisiert die Anzeige der Rollen
			window.updateDealer();
		} else
			window.deleteDealer();
		// gebe/w�hle Community-Cards
		this.tableCards[0] = this.deal();
		this.tableCards[1] = this.deal();
		this.tableCards[2] = this.deal();
		this.tableCards[3] = this.deal();
		this.tableCards[4] = this.deal();

		// aktualisiere Anzeige der Community-Karten
		window.updateCommunityCards();

		// zieht den Spielern die Blinds ab, wenn mehr als 3 Spieler im Spiel
		// sind
		if (validPlayers > 3) {
			this.collectBlinds();
		}

		// Setzt den aktuellen Einsatz auf den Mindesteinsatz
		this.cue = minimumBet;

		boolean endOfBetRound = false;

		while (activePlayerNumber > 1 && !endOfBetRound) {
			int movesWithoutRaise = 0; // Z�hlt Runden, in denen nicht erh�ht
										// wurde

			// Wettrunde
			while (movesWithoutRaise < activePlayerNumber) { // Wenn alle
																// Spieler
																// hintereinander
																// nicht erh�ht
																// haben

				int choice = 0;

				if (!activePlayers[turnPlayer].isFolded()) {
					if (turnPlayer != 0) {
						// L�sst KI entscheiden was getan werden soll
						Thread.sleep(5009);
						// f�hre KI aus, siehe Alfi Klasse
						choice = ((Alfi) this.activePlayers[turnPlayer])
								.decide(this.tableCards, this.getPot(),
										this.cue);
						((Alfi) this.activePlayers[turnPlayer]).outs = 0;
					} else {
						// L�sst menschl. Spieler entscheiden, was er tut ...
						if (this.activePlayers[0].getCredit() > 0) { // ...sofern
																		// er
																		// genug
																		// Geld
																		// hat
							int[] decision = this.activePlayers[turnPlayer]
									.decide(window); // Ruft das
														// Entscheidungsfenster
														// auf,siehe window
							choice = decision[0];
							this.activePlayers[turnPlayer].raiseWorth = decision[1];
						}
					}

					if (this.activePlayers[turnPlayer].getCredit() > 0) {

						switch (choice) { // Unterscheidet die Entscheidungen
											// der Spieler

						case 0: // raise/erh�hen

							this.cue += this.activePlayers[turnPlayer].raiseWorth;
							if (this.activePlayers[turnPlayer].raise(cue)) {
								// erh�ht den Einsatz
								window.updateCredits();
								this.raisePot(this.activePlayers[turnPlayer].raiseWorth);
								window.updatePot();
								movesWithoutRaise = 0;
								activePlayers[turnPlayer].debt = cue;
							} else {
								this.cue = this.activePlayers[turnPlayer]
										.getCredit();
								this.activePlayers[turnPlayer]
										.changeCredit(-this.activePlayers[turnPlayer]
												.getCredit());
								window.updateCredits();
								this.raisePot(cue);
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
							} else {
								this.activePlayers[turnPlayer]
										.changeCredit(-this.activePlayers[turnPlayer]
												.getCredit());
								window.updateCredits();
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
					}

					else {
						movesWithoutRaise++;
					}
				}
				// Ermittelt Spieler, der als n�chstes dran ist
				if ((turnPlayer + 1) > 3)
					turnPlayer = turnPlayer + 1 - 4;
				else
					turnPlayer = turnPlayer + 1;
			}
			// Decke die ersten 3 Karten auf falls diese noch verdeckt liegen
			if (!this.tableCards[2].isVisible()) {
				for (int i = 0; i < 3; i++) {
					this.tableCards[i].discover();
					window.updateCommunityCards();
				}

				// Decke 4 Karte auf
			} else if (!this.tableCards[3].isVisible()) {
				this.tableCards[3].discover();
				window.updateCommunityCards();

				// Decke 5. Karte auf
			} else if (!this.tableCards[4].isVisible()) {
				this.tableCards[4].discover();
				window.updateCommunityCards();
			} else {
				endOfBetRound = true;
			}
		}

		// Wenn nur noch ein Spieler �brig ist wird der Gewinn an diesen
		// ausgezahlt
		if (activePlayerNumber == 1) {
			for (int i = 0; i < 4; i++) {
				if (!activePlayers[i].isFolded()) {
					disburseAsset(activePlayers[i]); // Sch�tte Gewinn aus
				}

			}

			for (int i = 0; i < 4; i++) {
				if (this.activePlayers[i].getCredit() <= 0) {
					this.activePlayers[i].folded = true;
				} else
					this.activePlayers[i].folded = false;
			}

			window.updatePot(); // Aktualisiere die Pot-Anzeige
			window.updateCredits(); // Aktualisiere die Anzeige der
									// Spieler-Guthaben

		} else { // Wenn mehrere Spieler noch im Spiel sind, kommt es zum
					// Showdown
			if (this.tableCards[4].isVisible()) {
				for (int i = 0; i < 4; i++) {
					if (!activePlayers[i].isFolded()) {
						// Die folgenden Funktionen dienen dazu den Wert des
						// Blattes des jeweiligen Spielers zu ermitteln
						int[] sortedcards = this.activePlayers[i].sortWorth(7,
								tableCards);
						int fiveColor = this.activePlayers[i].fiveColor(7,
								tableCards);
						this.activePlayers[i].pairWorth = this.activePlayers[i]
								.sameWorth(7, sortedcards);
						int followfive = this.activePlayers[i].followFive(7,
								sortedcards);
						if (followfive != 0)
							activePlayers[i].highCard = followfive;

						this.activePlayers[i].getCards()[0].discover();
						this.activePlayers[i].getCards()[1].discover();
						this.activePlayers[i].getCards()[0].getPicture();
						this.activePlayers[i].getCards()[1].getPicture();
						window.updatePlayerCards(i);
						window.updatePlayerCards(i);
						Thread.sleep(10000);

						activePlayers[i].handWorth = activePlayers[i]
								.handWorth(sortedcards, fiveColor, followfive,
										this.activePlayers[i].pairWorth);

					}
				}

				int[] z = getWinner(); // Gewinner wird ermittelt ...

				disburseAsset(activePlayers[z[0]]); // ... und der Gewinn an
													// diesen ausgesch�ttet
				window.updatePot();
				window.updateCredits();
				this.givenCards = 0;
				this.activePlayerNumber = 4;

				for (int i = 0; i < 4; i++) {
					if (this.activePlayers[i].getCredit() <= 0) {
						this.activePlayers[i].folded = true;
					} else
						this.activePlayers[i].folded = false;
				}
			}
		}

		Thread.sleep(10000);
	}

	/**
	 * gibt Handkarten
	 * 
	 * @author georg
	 */
	private void preparePlayers() {

		// gebe jedem Spieler 2 Karten
		for (int i = 0; i < 4; i++) {
			if (!activePlayers[i].isFolded()) {
				Card[] cards = { deal(), deal() };
				activePlayers[i].setCards(cards);
				activePlayers[i].folded = false;
				activePlayers[i].debt = 0;
				activePlayers[i].handWorth = 0;
				activePlayers[i].highCard = 0;
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
	 * 
	 * @author georg
	 */
	private void assignRoles() {
		// w�hlt Dealer aus
		if (dealer == smallBlind) {
			dealer = r.nextInt(4);
		} else {
			dealer++;
		}

		if (dealer == 4) {
			dealer = 0;
		}

		// w�hlt Blinds aus
		if ((dealer + 1) > 3)
			smallBlind = dealer + 1 - 4;
		else
			smallBlind = dealer + 1;

		if ((dealer + 2) > 3)
			bigBlind = dealer + 2 - 4;
		else
			bigBlind = dealer + 2;

		// w�hlt Spieler aus, der zuerst dran ist
		turnPlayer = getTurnPlayer();

	}

	/**
	 * ermittelt den Spieler, der zuerst dran ist (immer derjenige, der rechts
	 * vom Dealer sitzt)
	 * 
	 * @author georg
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
	 * erzeugt f�r jeden Spieler ein Objekt
	 * 
	 * @author georg
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
	 * 
	 * @author georg
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
	 * 
	 * @author georg
	 */
	private void generateCards() {
		// durchl�uft alle Farben
		for (int colorCounter = 0; colorCounter < (Card.allColors.length); colorCounter++) {
			// durchl�uft alle Werte
			for (int worthCounter = 0; worthCounter < (Card.allWorths.length); worthCounter++) {
				// errechnet ID der aktuellen Karte
				int id = worthCounter * Card.allColors.length + colorCounter;
				// Erzeugt ein Objekt f�r die Karte und schreibt sie in ein
				// Array
				this.cards[id] = new Card(colorCounter, worthCounter);
			}
		}

		// setze die Anzahl der ausgegebenen Karten auf 0 zur�ck
		this.givenCards = 0;
	}

	/**
	 * @author georg
	 * @return zuf�llige Karte
	 */
	public Card deal() {
		// W�hlt Karte aus
		int randomCard = (int) (Math.random() * (this.cards.length - 1 - this.givenCards));
		Card card = this.cards[randomCard];

		// nimmt Karte aus dem Array und schiebt leere Stelle ans Ende
		for (int i = randomCard; i < this.cards.length - 1; i++) {
			this.cards[i] = this.cards[i + 1];
		}
		this.cards[this.cards.length - 1] = null;

		// z�hlt die ausgegebenen Karten mit
		this.givenCards++;
		return card;
	}

	/**
	 * Zahlt den Gewinn aus
	 * 
	 * @author georg
	 */
	void disburseAsset(Player winner) {
		winner.changeCredit(this.pot);
		this.pot = 0;
	}

	/**
	 * @author Sebastian
	 * @return Spieler, der die Runde gewonnen hat
	 */
	int[] getWinner() {
		int[] winnerArray = new int[2]; // Gewinnerarray
		int winnerAmount = 1; // Erkl�rung siehe unten
		int winners = 0; // Der menschl. Spieler wird zuerst als Gewinner
							// eingesetzt und dann ...

		for (int a = 1; a < 4; a++) { // werden die Bl�tter der Spieler
										// durchlaufen
			// Wenn der Wert des Blattes �ber dem Wert des derzeitigen
			// Gewinnerblattes liegt, ist dies das neue Gewinnerblatt
			if (activePlayers[a].handWorth > activePlayers[winners].handWorth) {
				winners = a;
			} else { // Wenn die Werte der beiden Bl�tter gleich sind, aber die
						// Highcard des aktuellen Spielers h�her ist, als die
						// des derzeitigen Gewinners, so ist der derzeitige
						// Spieler der neue Gewinner, au�er ...
				if (activePlayers[a].handWorth == activePlayers[winners].handWorth
						&& activePlayers[a].highCard > activePlayers[winners].highCard
						&& activePlayers[a].handWorth != 2
						&& activePlayers[a].handWorth != 3
						&& activePlayers[a].handWorth != 4
						&& activePlayers[a].handWorth != 7
						&& activePlayers[a].handWorth != 8) {
					winners = a;
				} else // ... wenn die Bl�tter den Wert eines Vierlings,
						// FullHouse, Drillings, 2 Paare oder Paar haben. Denn
						// dann...
				if (activePlayers[a].handWorth == activePlayers[winners].handWorth
						&& (activePlayers[a].handWorth == 2
								|| activePlayers[a].handWorth == 3
								|| activePlayers[a].handWorth == 4
								|| activePlayers[a].handWorth == 7 || activePlayers[a].handWorth == 8)) {

					switch (activePlayers[a].handWorth) { // ...wird das
															// pairWorth-Array
															// zu Rate gezogen
															// und geguckt,
															// welches Blatt den
															// h�heren
															// Kartenwert hat
															// (z.B: 10er
															// Vierling schl�gt
															// 5er Vierling)

					case 8: // Vierling
						if (activePlayers[a].pairWorth[1] > activePlayers[winners].pairWorth[1])
							winners = a;
						if (activePlayers[a].pairWorth[1] == activePlayers[winners].pairWorth[1])
							if (activePlayers[a].highCard > activePlayers[winners].highCard)
								winners = a;
							else {
								winnerAmount++;
							}
						break;

					case 7: // FullHouse
						if (activePlayers[a].pairWorth[1] > activePlayers[winners].pairWorth[1])
							winners = a;
						if (activePlayers[a].pairWorth[1] == activePlayers[winners].pairWorth[1])
							if (activePlayers[a].pairWorth[3] > activePlayers[winners].pairWorth[3])
								winners = a;
							else {
								winnerAmount++;
							}
						break;

					case 4: // Drilling
						if (activePlayers[a].pairWorth[1] > activePlayers[winners].pairWorth[1])
							winners = a;
						if (activePlayers[a].pairWorth[1] == activePlayers[winners].pairWorth[1])
							if (activePlayers[a].highCard > activePlayers[winners].highCard)
								winners = a;
							else {
								winnerAmount++;
							}
						break;

					case 3: // Zwei Paare
						if (activePlayers[a].pairWorth[3] > activePlayers[winners].pairWorth[3])
							winners = a;
						if (activePlayers[a].pairWorth[3] == activePlayers[winners].pairWorth[3])
							if (activePlayers[a].pairWorth[1] > activePlayers[winners].pairWorth[1])
								winners = a;
							else {
								winnerAmount++;
							}
						break;

					case 2: // Paar
						if (activePlayers[a].pairWorth[1] > activePlayers[winners].pairWorth[1])
							winners = a;
						if (activePlayers[a].pairWorth[1] == activePlayers[winners].pairWorth[1])
							if (activePlayers[a].highCard > activePlayers[winners].highCard)
								winners = a;
							else {
								winnerAmount++;
							}
						break;
					}
				}
			}
		}
		winnerArray[0] = winners; // Hier wird gespeichert, wer der GEwinner ist
		winnerArray[1] = winnerAmount; // Dies soll dazu dienen, den Gewinn an
										// mehrere Spieler auszusch�tten, wenn
										// diese genau das gleiche Blatt haben.
										// Dies ist jedoch noch nicht
										// implementiert
		return winnerArray; // Anschlie�end wird das Gewinnerarray returnt
	}

	/**
	 * @author Georg
	 * @return Pot-Betrag
	 */
	public int getPot() {
		return pot;
	}

	/**
	 * erh�ht den Pot
	 * 
	 * @author Georg
	 * @param Pot
	 */
	public void raisePot(int pot) {
		this.pot += pot;
		window.updatePot();
	}

	/**
	 * @author Georg
	 * @return Spieler, der Dealer ist
	 */
	public int getDealer() {
		return dealer;
	}

}