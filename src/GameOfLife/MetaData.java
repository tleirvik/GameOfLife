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
	private String rules;

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
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
}
