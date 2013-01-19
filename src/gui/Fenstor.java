package gui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
    repaint();}  //Methode. für die Anzeige der Karten
	
	private void newText(int x,int y,int money){
		
		final JTextField Text = new JTextField(money+" $");
		Text.setBounds(x, y, 150, 50);
		Text.setBorder(BorderFactory.createEmptyBorder());
		Text.setHorizontalAlignment(JTextField.CENTER);
		Text.setEditable(false);
		add(Text);
		
	}
	
	
	public Fenstor(Game game) { //Fenster,welches angezeigt wird
		
		
		super("Fenst0r");
		setLayout(null);
		setSize(1200, 900);
		setVisible(true);
		
		//Spielerkarten:

	    newCard(550, 700, "herzacht.png");
        newCard(630, 700, "herzneun.png");
        newCard(50, 400, "blue_back.jpg");
        newCard(120, 400, "blue_back.jpg");
        newCard(550, 50, "blue_back.jpg");
        newCard(620, 50, "blue_back.jpg");
        newCard(1030, 400, "blue_back.jpg");
        newCard(1100, 400, "blue_back.jpg");
		
		//Gemeinschaftskarten:
		
        newCard(400, 400, "karosieben.png");

        newCard(470, 400, "karoacht.png");
		
        newCard(540, 400, "karoass.png");
		
        newCard(610, 400, "blue_back.jpg");
		
        newCard(680, 400, "blue_back.jpg");
		
		//Guthabenstände der Spieler:

		newText(330,740,10000);
		newText(50,530,10000);
		newText(380,90,10000);
		newText(1010,530,10000);
		

		//Hintergrund bzw. Spieltisch und CommunityCard Hintergrund
		
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

//Penishoden 123 imer schön aufs Maul dabei
