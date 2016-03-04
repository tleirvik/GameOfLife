package GameOfLife;

/**
 * @author tleirvik, rlundh<p>
 * @version 1.0<p>
 *
 * This class is a container class for a boards metadata<p>
 *
 * @see Board.java
 * @see RLEDecoder.java
 * @see RLEEncoder.java
 *
 */
public class MetaData {

	private String author;
	private String name;
	private String comment;
	private String survivalRules;
	private String birthRules;

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String[] getRules() {
		String[] rules = { survivalRules, birthRules};
		return rules;
	}
	public void setRules(String[] SBrules) {
		this.survivalRules = SBrules[0];
		this.birthRules = SBrules[1];
	}
}
