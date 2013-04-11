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
	 * macht die Karte sichtbar
	 */
	public void discover() {
		this.visible = true;
	}

	/**
	 * @return gibt zurück, ob die Karte sichtbar ist
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * @return gibt den Namen der Karte zurück
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return gibt den Wert der Karte zurück
	 */
	public int getWorthID() {
		return worth;
	}

	/**
	 * @return gibt den Namen des Wertes der Karte zurück
	 */
	@SuppressWarnings("static-access")
	public String getWorthName() {
		return this.allWorths[this.worth];
	}

	/**
	 * @return gibt den Namen der Farbe der Karte zurück
	 */
	@SuppressWarnings("static-access")
	public String getColorName() {
		return this.allColors[this.color];
	}

	/**
	 * 
	 * @return gibt die Farbe der Karte zurück
	 */
	public int getColorID() {
		return color;
	}

	/**
	 * @return gibt das Bild der Karte zurück
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
