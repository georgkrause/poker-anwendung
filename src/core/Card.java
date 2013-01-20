package core;

public class Card {

	public static final String[] allColors = { "Herz", "Kreuz", "Karo", "Piek" };
	public static final String[] allWorths = { "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "Bube", "Dame", "König", "Ass" };
	
	private String name;
	private int worth;
	private int color;
	private boolean visible = false;

	Card(int color, int worth) {
		this.worth = worth;
		this.color = color;
		
		this.name = allColors[color] + " " + allWorths[worth];
	}

	/**
	 * turns card
	 */
	public void discover() {
		this.visible = true;
	}

	/**
	 * @return if the card is visible
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ID of the worth
	 */
	public int getWorthID() {
		return worth;
	}
	
	/**
	 * @return the worth
	 */
	@SuppressWarnings("static-access")
	public String getWorthName() {
		return this.allWorths[this.worth];
	}

	/**
	 * @return the color
	 */
	@SuppressWarnings("static-access")
	public String getColorName() {
		return this.allColors[this.color];
	}

	/**
	 * 
	 * @return the ID of the color
	 */
	public int getColorID() {
		return color;
	}
	
	/** 
	 * @return the picture of the card
	 * TODO #5
	 */
	@SuppressWarnings("static-access")
	public String getPicture() {
		if(this.isVisible())
		{
			return this.allColors[this.color] + "_" + this.allWorths[this.worth];
		} else {
			return "invisible_card.png";
		}
	}

}
