package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import core.Game;

public class Window extends JFrame {

	private Game game;
	public JTextField[] credits = new JTextField[4];
	public JLabel[] cards = new JLabel[13];

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
		this.credits[id].setBounds(x, y, 150, 50);
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
	private void Dealer(int d) {
		int[] x = { 460, 180, 600, 950, 460, 180, 600, 950 };
		int[] y = { 670, 400, 150, 400, 670, 400, 150, 400 };

		ImageIcon background1 = new ImageIcon("img/dealerbutton.png");
		JLabel bereich1 = new JLabel(background1);
		bereich1.setBounds(x[d], y[d], 60, 60);
		add(bereich1);
		repaint();

		ImageIcon background2 = new ImageIcon("img/smallblind.png");
		JLabel bereich2 = new JLabel(background2);
		bereich2.setBounds(x[d + 1], y[d + 1], 60, 60);
		add(bereich2);
		repaint();

		ImageIcon background3 = new ImageIcon("img/bigblind.png");
		JLabel bereich3 = new JLabel(background3);
		bereich3.setBounds(x[d + 2], y[d + 2], 60, 60);
		add(bereich3);
		repaint();

	}

	/**
	 * shows a new Button
	 * 
	 * @param text
	 * @param x
	 * @param y
	 */
	private void newButton(final String text, int x, int y) {
		final JButton Button = new JButton(text);
		Button.setBounds(x, y, 100, 100);
		add(Button);
		repaint();
		Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text.equals("Fold")) {
					game.activePlayers[0].fold();
					game.tableCards[0].discover();
					cards[0].setIcon(new ImageIcon("img/"
							+ game.tableCards[0].getPicture()));
				} else if (text.equals("Raise")) {
					if (game.activePlayers[0].raise(100)) {
						game.cue = 100;
						String credit = game.activePlayers[0].getCredit()
								+ " $";
						credits[0].setText(credit);
					}
				} else if (text.equals("Call")) {
					if (game.activePlayers[0].call(10)) {
						String credit = game.activePlayers[0].getCredit()
								+ " $";
						credits[0].setText(credit);
					}
				}
			}
		});
	}

	public int DialogBox() {
		Object[] options = { "Raise", "Call", "Fold" };
		return JOptionPane.showOptionDialog(this,
				"Welche Aktion möchtest du ausführen?", "Eingabe",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
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

	public void updateCommunityCards() {
		for (int i = 0; i < 5; i++) {

			this.cards[i].setIcon(new ImageIcon("img/"
					+ game.tableCards[i].getPicture()));

		}
	}

	public void updatePlayerCards(int id) {
		if (!game.activePlayers[id].folded) {
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

		super("Fenst0r");

		this.game = game;

		setLayout(null);
		setSize(1200, 900);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Spielerkarten:

		newCard(5, 550, 700, game.activePlayers[0].getCards()[0].getPicture());
		newCard(6, 630, 700, game.activePlayers[0].getCards()[1].getPicture());

		newCard(7, 50, 400, game.activePlayers[1].getCards()[0].getPicture());
		newCard(8, 120, 400, game.activePlayers[1].getCards()[1].getPicture());

		newCard(9, 550, 50, game.activePlayers[2].getCards()[0].getPicture());
		newCard(10, 620, 50, game.activePlayers[2].getCards()[1].getPicture());

		newCard(11, 1030, 400, game.activePlayers[3].getCards()[0].getPicture());
		newCard(12, 1100, 400, game.activePlayers[3].getCards()[1].getPicture());

		// Gemeinschaftskarten:

		newCard(0, 400, 400, game.tableCards[0].getPicture());
		newCard(1, 470, 400, game.tableCards[1].getPicture());
		newCard(2, 540, 400, game.tableCards[2].getPicture());
		newCard(3, 610, 400, game.tableCards[3].getPicture());
		newCard(4, 680, 400, game.tableCards[4].getPicture());

		// Guthabenstände der Spieler:

		newText(0, 330, 600, game.activePlayers[0].getCredit());
		newText(1, 50, 530, game.activePlayers[1].getCredit());
		newText(2, 380, 90, game.activePlayers[2].getCredit());
		newText(3, 1010, 530, game.activePlayers[3].getCredit());

		// Aktionsbuttons der Spieler: Standard: Call,Raise, Fold

		newButton("Fold", 600, 580);
		newButton("Call", 720, 580);
		newButton("Raise", 720, 700);

		// Dealermarke,Small Blind,Big Blind

		Dealer(game.getDealer());

		// Hintergrund bzw. Spieltisch und CommunityCard Hintergrund

		ImageIcon backgroundccards = new ImageIcon("img/communitycardbg.png");
		JLabel ccardbereich = new JLabel(backgroundccards);
		ccardbereich.setBounds(370, 380, 400, 150);
		add(ccardbereich);
		repaint();

		ImageIcon background = new ImageIcon("img/background.png");
		JLabel bereich = new JLabel(background);
		bereich.setBounds(0, 0, 1200, 900);
		add(bereich);
		repaint();

	}
}
