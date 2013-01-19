package gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Fenstor extends JFrame {
	Fenstor() {
		super("Fenst0r");
		setLayout(null);
		setSize(1200, 1000);
		setVisible(true);

		ImageIcon background= new ImageIcon("img/keinpenis2.PNG");
		JLabel bereich = new JLabel(background);
		bereich.setBounds(10, 10, 1150, 880);
		add(bereich);
		repaint();
		
		ImageIcon player0card0 = new ImageIcon("img/" + game.activePlayers[0].getCards()[0].getPicture() +".png");
		JLabel karte = new JLabel(player0card0);
		karte.setBounds(550, 50, 610, 150);
		add(bereich);
		repaint();
		
		ImageIcon player0card1 = new ImageIcon("img/" + game.activePlayers[0].getCards()[1].getPicture() +".png");
		JLabel karte2 = new JLabel(player0card1);
		karte2.setBounds(620, 50, 680, 150);
		add(bereich);
		repaint();

		
		ImageIcon playercard2card0 = new ImageIcon("img/" + game.activePlayers[2].getCards()[0].getPicture() +".png");
		JLabel karte5 = new JLabel(playercard2card0);
		karte5.setBounds(450, 850, 510, 950);
		add(bereich);
		repaint();
		
		ImageIcon playercard2card1 = new ImageIcon("img/" + game.activePlayers[2].getCards()[1].getPicture()+".png");
		JLabel karte6 = new JLabel(playercard2card0);
		karte6.setBounds(550, 850, 610, 950);
		add(bereich);
		repaint();
	}

	public static void main(String args[]) {
		new Fenstor();

	}
}
