package gui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import core.Game;
import core.Player;

public class Window extends JFrame {

	private Game game;
	// Array mit angezeigten Guthaben-Ständen
	private JTextField[] credits = new JTextField[4];
	// Array mit angezeigten Karten
	private JLabel[] cards = new JLabel[13];
	// Array mit angezeigten Markierungen
	private JLabel[] marks = new JLabel[3];
	// Text-Feld für den Pot
	JTextField pot;

	// Arrays für die Platzierung von Objekten
	private int[] x = { 260, 100, 270, 700, 260, 100, 270, 700, };
	private int[] y = { 470, 220, 50, 200, 470, 220, 50, 200, };

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor, erstellt ein neues Fenster
	 * 
	 * @param game
	 */
	public Window(Game game) {

		super("Window");
		this.game = game;

		setLayout(null);
		setSize(800, 600);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Spielerkarten:
		newCard(5, 350, 460, "");
		newCard(6, 420, 460, "");

		newCard(7, 50, 280, "");
		newCard(8, 120, 280, "");

		newCard(9, 350, 30, "");
		newCard(10, 420, 30, "");

		newCard(11, 630, 280, "");
		newCard(12, 700, 280, "");

		// Gemeinschaftskarten:
		newCard(0, 250, 280, "");
		newCard(1, 320, 280, "");
		newCard(2, 390, 280, "");
		newCard(3, 460, 280, "");
		newCard(4, 530, 280, "");

		// Guthabenstände der Spieler:
		newText(0, 500, 460, game.activePlayers[0].getCredit());
		newText(1, 50, 400, game.activePlayers[1].getCredit());
		newText(2, 500, 50, game.activePlayers[2].getCredit());
		newText(3, 700, 400, game.activePlayers[3].getCredit());

		// Pott: (braucht extra Festlegung, da Größe anders sein muss, als
		// die der anderen Textfelder
		pot = new JTextField(game.getPot() + " $");
		pot.setBounds(360, 180, 100, 50);
		pot.setBorder(BorderFactory.createEmptyBorder());
		pot.setHorizontalAlignment(JTextField.CENTER);
		pot.setEditable(false);
		add(pot);

		// Dealermarke,Small Blind,Big Blind
		Dealer();

		// Hintergrund der Tischkarten
		ImageIcon backgroundccards = new ImageIcon("img/communitycardbg.png");
		JLabel ccardbereich = new JLabel(backgroundccards);
		ccardbereich.setBounds(230, 250, 370, 150);
		add(ccardbereich);
		repaint();

		// Spieltisch
		ImageIcon background = new ImageIcon("img/background.png");
		JLabel bereich = new JLabel(background);
		bereich.setBounds(0, 0, 800, 600);
		add(bereich);
		repaint();
	}

	/**
	 * zeigt eine Karte
	 * 
	 * @param x
	 * @param y
	 * @param cardfile
	 */
	private void newCard(int id, int x, int y, String cardfile) {
		ImageIcon CardIcon = new ImageIcon("img/" + cardfile);
		cards[id] = new JLabel(CardIcon);
		cards[id].setBounds(x, y, 60, 100);
		add(cards[id]);
		repaint();
	}

	/**
	 * zeigt das Guthaben eines Spielers an
	 * 
	 * @param x
	 * @param y
	 * @param money
	 */
	private void newText(int id, int x, int y, int money) {

		final JTextField Text = new JTextField(money + " $");
		this.credits[id] = Text;
		this.credits[id].setBounds(x, y, 50, 50);
		this.credits[id].setBorder(BorderFactory.createEmptyBorder());
		this.credits[id].setHorizontalAlignment(JTextField.CENTER);
		this.credits[id].setEditable(false);
		add(this.credits[id]);

	}

	/**
	 * zeigt Markierung für Dealer und Blinds an
	 * 
	 * @param ID
	 *            des Spielers, der Dealer ist
	 */
	private void Dealer() {
		ImageIcon background1 = new ImageIcon("img/dealerbutton.png");
		marks[0] = new JLabel(background1);
		add(marks[0]);
		repaint();

		ImageIcon background2 = new ImageIcon("img/smallblind.png");
		marks[1] = new JLabel(background2);
		add(marks[1]);
		repaint();

		ImageIcon background3 = new ImageIcon("img/bigblind.png");
		marks[2] = new JLabel(background3);
		add(marks[2]);
		repaint();

	}

	/**
	 * Aktualisiert die Anzeige des Dealers
	 */
	public void updateDealer() {
		int dealer = game.getDealer();
		marks[0].setBounds(x[dealer], y[dealer], 50, 50);
		marks[1].setBounds(x[dealer + 1], y[dealer + 1], 50, 50);
		marks[2].setBounds(x[dealer + 2], y[dealer + 2], 50, 50);
		repaint();
	}

	/**
	 * Zeigt eine Dialog-Box an, mit der der Spieler seinen Zug auswählt/ Was er tun möchte
	 * 
	 * @return
	 */
	public int DialogBox() {
		Object[] options = new Object[3];
		if (game.cue > game.activePlayers[0].debt) {
			options[0] = "Raise";
			options[1] = "Call";
			options[2] = "Fold";
		} else {
			options[0] = "Raise";
			options[1] = "Check";
			options[2] = "Fold";
		}
		return JOptionPane.showOptionDialog(this,
				"Welche Aktion möchtest du ausführen?\n "
						+ "Der aktuelle Einsatz liegt bei " + game.cue + "$.\n"
						+ "Du hast bereits " + game.activePlayers[0].debt
						+ "$ gesetzt.", "Eingabe",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
	}

	/**
	 * Zeigt eine Dialog-Box an, mit der der Spieler angibt, wie viel erhöhen
	 * möchte und returnt den Wert, um den erhöht wird
	 * 
	 * @return 
	 */
	public int RaiseDialogBox() {

		String inputValue = JOptionPane
				.showInputDialog("Um wie viel möchtest du erhöhen?");
		int intZahl = Integer.parseInt(inputValue);

		return intZahl;
	}

	/**
	 * Aktualisiert die Anzeige der Guthaben-Stände
	 */
	public void updateCredits() {
		for (int i = 0; i < 4; i++) {
			String credit = game.activePlayers[i].getCredit() + " $";
			credits[i].setText(credit);
		}
	}

	/**
	 * Aktualisiert die Anzeige des Pots
	 */
	public void updatePot() {
		this.pot.setText(game.getPot() + " $");
	}

	/**
	 * Aktualisiert die Anzeige der Tisch-Karten
	 */
	public void updateCommunityCards() {
		for (int i = 0; i < 5; i++) {

			this.cards[i].setIcon(new ImageIcon("img/"
					+ game.tableCards[i].getPicture()));

		}
	}

	/**
	 * Aktualisiert die Anzeige der Spieler-Karten
	 * 
	 * @param id
	 */
	public void updatePlayerCards(int id) {
		if (!game.activePlayers[id].isFolded()) {
			// Wenn Spieler die Runde nicht verlassen hat, lade neue Bilder
			this.cards[id * 2 + 5].setIcon(new ImageIcon("img/"
					+ game.activePlayers[id].getCards()[0].getPicture()));
			this.cards[id * 2 + 6].setIcon(new ImageIcon("img/"
					+ game.activePlayers[id].getCards()[1].getPicture()));
		} else {
			// Wenn Spieler die Runde verlassen hat, blende die Karten aus
			this.cards[id * 2 + 5].setIcon(new ImageIcon("keine_Karte"));
			this.cards[id * 2 + 6].setIcon(new ImageIcon("keine_Karte"));
		}
	}

	/**
	 * Löscht den Dealer-Button und die Blind-Markierungen
	 */
	public void deleteDealer() {
		marks[0].setBounds(0, 0, 0, 0);
		marks[1].setBounds(0, 0, 0, 0);
		marks[2].setBounds(0, 0, 0, 0);
		repaint();

	}
	/*
	 * Gibt den Gewinner in einem Textfeld aus
	 */
	public void doWinDialog(int player) {
		final JTextField winner = new JTextField("Der Gewinner ist Spieler "
				+ player);

		winner.setBounds(200, 150, 400, 300);
		winner.setBorder(BorderFactory.createEmptyBorder());
		winner.setHorizontalAlignment(JTextField.CENTER);
		winner.setEditable(false);
		add(winner);

	}

}