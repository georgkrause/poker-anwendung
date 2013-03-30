package core;

public class Card {

	public static final String[] allColors = { "herz", "kreuz", "karo", "pik" };
	public static final String[] allWorths = { "zwei", "drei", "vier", "funf",
			"sechs", "sieben", "acht", "neun", "zehn", "bube", "dame",
			"koenig", "ass" };

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
	 */
	@SuppressWarnings("static-access")
	public String getPicture() {
		if (this.isVisible()) {
			return this.allColors[this.color] + this.allWorths[this.worth]
					+ ".png";
		} else {
			return "blue_back.jpg";
		}
	}

}
