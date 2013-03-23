package gui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import core.Game;

public class Window extends JFrame {

	private Game game;
	private JTextField[] credits = new JTextField[4];
	private JLabel[] cards = new JLabel[13];
	private JLabel[] marks = new JLabel[3];
	JTextField pot;

	private int[] x = { 260, 100, 270, 700, 260, 100, 270, 700, };
	private int[] y = { 470, 220, 50, 200, 470, 220, 50, 200, };

	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * shows a card
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
	 * shows the credit of a player
	 * 
	 * @param x
	 * @param y
	 * @param money
	 */
	private void newText(int id, int x, int y, int money) {

		final JTextField Text = new JTextField(money + " $");
		this.credits[id] = Text;
		this.credits[id].setBounds(x, y, 50, 50);
		// this.credits[id].setBackground(Color.blue);
		this.credits[id].setBorder(BorderFactory.createEmptyBorder());
		this.credits[id].setHorizontalAlignment(JTextField.CENTER);
		this.credits[id].setEditable(false);
		add(this.credits[id]);

	}

	/**
	 * shows the marks for the dealer and the blinds
	 * 
	 * @param d
	 *            id of the player who is dealer
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

	public void updateDealer() {
		int dealer = game.getDealer();
		marks[0].setBounds(x[dealer], y[dealer], 50, 50);
		marks[1].setBounds(x[dealer + 1], y[dealer + 1], 50, 50);
		marks[2].setBounds(x[dealer + 2], y[dealer + 2], 50, 50);
		repaint();
	}

	/**
	 * shows a new Button
	 * 
	 * @param text
	 * @param x
	 * @param y
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
	 * 
	 * @return
	 */
	public int RaiseDialogBox() {

		String inputValue = null;
		inputValue = JOptionPane
				.showInputDialog("Um wie viel möchtest du erhöhen?");
		if (inputValue != null) {
			int intZahl = Integer.parseInt(inputValue);

			return intZahl;
		}
		return 0;
	}

	/**
	 * updates each credits of the GUI
	 */
	public void updateCredits() {
		for (int i = 0; i < 4; i++) {
			String credit = game.activePlayers[i].getCredit() + " $";
			credits[i].setText(credit);
		}
	}

	public void updatePot() {
		this.pot.setText(game.getPot() + " $");
	}

	/**
	 * updates the pictures of the community cards
	 */
	public void updateCommunityCards() {
		for (int i = 0; i < 5; i++) {

			this.cards[i].setIcon(new ImageIcon("img/"
					+ game.tableCards[i].getPicture()));

		}
	}

	public void updatePlayerCards(int id) {
		if (!game.activePlayers[id].isFolded()) {
			this.cards[id * 2 + 5].setIcon(new ImageIcon("img/"
					+ game.activePlayers[id].getCards()[0].getPicture()));
			this.cards[id * 2 + 6].setIcon(new ImageIcon("img/"
					+ game.activePlayers[id].getCards()[1].getPicture()));
		} else {
			this.cards[id * 2 + 5].setIcon(new ImageIcon("nix"));
			this.cards[id * 2 + 6].setIcon(new ImageIcon("nix"));
		}
	}

	/**
	 * shows the window
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

		// Hintergrund bzw. Spieltisch und CommunityCard Hintergrund

		ImageIcon backgroundccards = new ImageIcon("img/communitycardbg.png");
		JLabel ccardbereich = new JLabel(backgroundccards);
		ccardbereich.setBounds(230, 250, 370, 150);
		add(ccardbereich);
		repaint();

		ImageIcon background = new ImageIcon("img/background.png");
		JLabel bereich = new JLabel(background);
		bereich.setBounds(0, 0, 800, 600);
		add(bereich);
		repaint();
	}
}
