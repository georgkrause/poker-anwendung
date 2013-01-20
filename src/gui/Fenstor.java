package gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;

import core.Game;
public class Fenstor extends JFrame {
	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void newCard(int x, int y, String cardfile) {
		ImageIcon CardIcon = new ImageIcon("img/" + cardfile);
		JLabel CardLabel = new JLabel(CardIcon);
		CardLabel.setBounds(x, y, 60, 100);
		add(CardLabel);
		repaint();
	} // Methode für die Anzeige der Karten

	private void newText(int x, int y, int money) {

		final JTextField Text = new JTextField(money + " $");
		Text.setBounds(x, y, 150, 50);
		Text.setBorder(BorderFactory.createEmptyBorder());
		Text.setHorizontalAlignment(JTextField.CENTER);
		Text.setEditable(false);
		add(Text);

	} // Methode für das Anzeigen der Spielerguthaben
	
	private void Dealer (int d){ //d=Variable, die anzeigt, wer der aktuelle Dealer ist
		int [] x={460,180,600,950,460,180,600,950};
		int [] y={670,400,150,400,670,400,150,400};
		
		ImageIcon background1 = new ImageIcon("img/dealerbutton.png");
		JLabel bereich1 = new JLabel(background1);
		bereich1.setBounds(x[d], y[d], 60, 60);
		add(bereich1);
		repaint();
		
		ImageIcon background2 = new ImageIcon("img/smallblind.png");
		JLabel bereich2 = new JLabel(background2);
		bereich2.setBounds(x[d+1], y[d+1], 60, 60);
		add(bereich2);
		repaint();
		
		ImageIcon background3 = new ImageIcon("img/bigblind.png");
		JLabel bereich3 = new JLabel(background3);
		bereich3.setBounds(x[d+2], y[d+2], 60, 60);
		add(bereich3);
		repaint();
		
		
	}
	
	
	public Fenstor(Game game) { // Fenster,welches angezeigt wird.

		super("Fenst0r");
		setLayout(null);
		setSize(1200, 900);
		setVisible(true);

		// Spielerkarten:

		newCard(550, 700, "herzacht.png");
		newCard(630, 700, "herzneun.png");
		newCard(50, 400, "blue_back.jpg");
		newCard(120, 400, "blue_back.jpg");
		newCard(550, 50, "blue_back.jpg");
		newCard(620, 50, "blue_back.jpg");
		newCard(1030, 400, "blue_back.jpg");
		newCard(1100, 400, "blue_back.jpg");

		// Gemeinschaftskarten:

		newCard(400, 400, "karosieben.png");

		newCard(470, 400, "karoacht.png");

		newCard(540, 400, "karoass.png");

		newCard(610, 400, "blue_back.jpg");

		newCard(680, 400, "blue_back.jpg");

		// Guthabenstände der Spieler:

		newText(330, 740, 10000);
		newText(50, 530, 10000);
		newText(380, 90, 10000);
		newText(1010, 530, 10000);
		
		//Aktionsbuttons der Spieler: Standard: Call,Raise, Fold: TODO #1
		
	
		final JButton Button = new JButton("Fold");
		Button.setBounds(600, 580, 100, 100);
		add(Button);
		repaint();
		Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//			fold();
			}});
		
		final JButton Button2 = new JButton("Call");
		Button2.setBounds(720, 580, 100, 100);
		add(Button2);
		repaint();
		Button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//			call();
			}});
		
		final JButton Button3 = new JButton("Raise");
		Button3.setBounds(720, 700, 100, 100);
		add(Button3);
		repaint();
		Button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//			raise();
			}});
		
		
		
		//Dealermarke,Small Blind,Big Blind: TODO #2
		
		Dealer(2); 
		

		// Hintergrund bzw. Spieltisch und CommunityCard Hintergrund TODO #3

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
		
		
		
		
		
	

}}


