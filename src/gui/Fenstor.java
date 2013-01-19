package gui;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import core.Game;

public class Fenstor extends JFrame {
	public Fenstor(Game game) {
		super("Fenst0r");
		setLayout(null);
		setSize(1200, 900);
		setVisible(true);


		ImageIcon player0card0 = new ImageIcon("herzacht.png");
		JLabel karte = new JLabel(player0card0);
		karte.setBounds(550, 700, 60, 100);
		add(karte);
		repaint();
		
		ImageIcon player0card1 = new ImageIcon("herzneun.png");
		JLabel karte2 = new JLabel(player0card1);
		karte2.setBounds(630, 700, 60, 100);
		add(karte2);
		repaint();

		
		ImageIcon playercard2card0 = new ImageIcon("blue_back.jpg");
		JLabel karte5 = new JLabel(playercard2card0);
		karte5.setBounds(550, 50, 60, 100);
		add(karte5);
		repaint();
		
		ImageIcon playercard2card1 = new ImageIcon("blue_back.jpg");
		JLabel karte6 = new JLabel(playercard2card1);
		karte6.setBounds(620, 50, 60, 100);
		add(karte6);
		repaint();
		
		ImageIcon playercard1card0 = new ImageIcon("blue_back.jpg");
		JLabel karte3 = new JLabel(playercard1card0);
		karte3.setBounds(50, 400, 60, 100);
		add(karte3);
		repaint();
		
		ImageIcon playercard1card1 = new ImageIcon("blue_back.jpg");
		JLabel karte4 = new JLabel(playercard1card1);
		karte4.setBounds(120, 400, 60, 100);
		add(karte4);
		repaint();
		
		ImageIcon playercard3card0 = new ImageIcon("blue_back.jpg");
		JLabel karte7 = new JLabel(playercard3card0);
		karte7.setBounds(1030, 400, 60, 100);
		add(karte7);
		repaint();
		
		ImageIcon playercard3card1 = new ImageIcon("blue_back.jpg");
		JLabel karte8 = new JLabel(playercard3card1);
		karte8.setBounds(1100, 400, 60, 100);
		add(karte8);
		repaint();
		
		
		
		
		
		
		
		
		
		
		
		
		ImageIcon background= new ImageIcon("background.png");
		JLabel bereich = new JLabel(background);
		bereich.setBounds(0, 0, 1200, 900);
		add(bereich);
		repaint();
		
	}


}
