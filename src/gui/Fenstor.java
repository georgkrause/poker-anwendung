package gui;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import core.Game;

public class Fenstor extends JFrame {
	public Fenstor(Game game) {
		super("Fenst0r");
		setLayout(null);
		setSize(1200, 900);
		setVisible(true);
		
		//Spielerkarten (aus Sicht des Spielers)

		ImageIcon player0card0 = new ImageIcon("img/herzacht.png");
		JLabel karte = new JLabel(player0card0);
		karte.setBounds(550, 700, 60, 100);
		add(karte);
		repaint();

		ImageIcon player0card1 = new ImageIcon("img/herzneun.png");
		JLabel karte2 = new JLabel(player0card1);
		karte2.setBounds(630, 700, 60, 100);
		add(karte2);
		repaint();

		ImageIcon playercard1card0 = new ImageIcon("img/blue_back.jpg");
		JLabel karte3 = new JLabel(playercard1card0);
		karte3.setBounds(50, 400, 60, 100);
		add(karte3);
		repaint();

		ImageIcon playercard1card1 = new ImageIcon("img/blue_back.jpg");
		JLabel karte4 = new JLabel(playercard1card1);
		karte4.setBounds(120, 400, 60, 100);
		add(karte4);
		repaint();
		
		ImageIcon playercard2card0 = new ImageIcon("img/blue_back.jpg");
		JLabel karte5 = new JLabel(playercard2card0);
		karte5.setBounds(550, 50, 60, 100);
		add(karte5);
		repaint();

		ImageIcon playercard2card1 = new ImageIcon("img/blue_back.jpg");
		JLabel karte6 = new JLabel(playercard2card1);
		karte6.setBounds(620, 50, 60, 100);
		add(karte6);
		repaint();

		ImageIcon playercard3card0 = new ImageIcon("img/blue_back.jpg");
		JLabel karte7 = new JLabel(playercard3card0);
		karte7.setBounds(1030, 400, 60, 100);
		add(karte7);
		repaint();

		ImageIcon playercard3card1 = new ImageIcon("img/blue_back.jpg");
		JLabel karte8 = new JLabel(playercard3card1);
		karte8.setBounds(1100, 400, 60, 100);
		add(karte8);
		repaint();
		
		
		//Gemeinschaftskarten:
		
		ImageIcon flopcard1 = new ImageIcon("img/herzzehn.png");
		JLabel flop1 = new JLabel(flopcard1);
		flop1.setBounds(400, 400, 60, 100);
		add(flop1);
		repaint();

		ImageIcon flopcard2 = new ImageIcon("img/kreuzass.png");
		JLabel flop2 = new JLabel(flopcard2);
		flop2.setBounds(470, 400, 60, 100);
		add(flop2);
		repaint();
		
		ImageIcon flopcard3 = new ImageIcon("img/karoass.png");
		JLabel flop3 = new JLabel(flopcard3);
		flop3.setBounds(540, 400, 60, 100);
		add(flop3);
		repaint();
		
		ImageIcon turncard = new ImageIcon("img/blue_back.jpg");
		JLabel turncard1 = new JLabel(turncard);
		turncard1.setBounds(610, 400, 60, 100);
		add(turncard1);
		repaint();
		
		ImageIcon rivercard = new ImageIcon("img/blue_back.jpg");
		JLabel rivercard1 = new JLabel(rivercard);
		rivercard1.setBounds(680, 400, 60, 100);
		add(rivercard1);
		repaint();
		
		//Guthabenstände der Spieler:

		final JTextField moneyplayer0 = new JTextField("10000 $");
		moneyplayer0.setBounds(330, 740, 150, 50);
		moneyplayer0.setBorder(BorderFactory.createEmptyBorder());
		moneyplayer0.setHorizontalAlignment(JTextField.CENTER);
		add(moneyplayer0);

		final JTextField moneyplayer1 = new JTextField("10000 $");
		moneyplayer1.setBounds(50, 530, 150, 50);
		moneyplayer1.setBorder(BorderFactory.createEmptyBorder());
		moneyplayer1.setHorizontalAlignment(JTextField.CENTER);
		add(moneyplayer1);

		final JTextField moneyplayer2 = new JTextField("10000 $");
		moneyplayer2.setBounds(380, 90, 150, 50);
		moneyplayer2.setBorder(BorderFactory.createEmptyBorder());
		moneyplayer2.setHorizontalAlignment(JTextField.CENTER);
		add(moneyplayer2);

		final JTextField moneyplayer3 = new JTextField("10000 $");
		moneyplayer3.setBounds(1010, 530, 150, 50);
		moneyplayer3.setBorder(BorderFactory.createEmptyBorder());
		moneyplayer3.setHorizontalAlignment(JTextField.CENTER);
		add(moneyplayer3);

		//Hintergrund bzw. Spieltisch
		
		ImageIcon background = new ImageIcon("img/background.png");
		JLabel bereich = new JLabel(background);
		bereich.setBounds(0, 0, 1200, 900);
		add(bereich);
		repaint();

	}

}

//Penishoden 123 imer schön aufs Maul dabei
