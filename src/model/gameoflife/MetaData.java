package model.gameoflife;

/**
 * @author tleirvik, rlundh, srrogeberg
 * @version 1.0
 *
 * This class is a container class for a boards metadata
 *
 * @see model.gameoflife.boards.Board
 * @see model.filemanagement.decoders.Decoder
 * @see model.filemanagement.encoders.Encoder
 *
 */
public class MetaData {

	private String author;
	private String name;
	private String comment;
	private String survivalRule;
	private String birthRule;

	/**
	 * Constructs a new, empty  meta data object
	 */
	public MetaData() {
		author = "";
		name = "";
		comment = "";
		survivalRule = "";
		birthRule = "";
	}

	/**
	 * Returns the author name
	 * @return The authors name
     */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the author of the pattern
	 * @param author The name of the author
     */
	public void setAuthor(String author) {
        this.author = author;
	}

	/**
	 * Returns the name of the pattern
	 * @return The name of the pattern
     */
	public String getName() {
        return name;
	}

	/**
	 * Sets the name of the pattern
	 * @param name The name of the pattern
     */
	public void setName(String name) {
        this.name = name;
	}

	/**
	 * Returns the boards comments
	 * @return The boards comments
     */
	public String getComment() {
        return comment;
	}

	/**
	 * Sets the boards comments
	 * @param comment The boards comments
     */
	public void setComment(String comment) {
        this.comment = comment;
	}

	/**
	 * Returns the game rules
	 * @return The game rules
     */
	public String[] getRuleString() {
        String[] rules = {
                survivalRule,
                birthRule
        };
		return rules;
	}

	/**
	 * Sets the rules used on the game board
	 * @param SBrules The game rules
     */
	public void setRuleString(String[] SBrules) {
		this.survivalRule = SBrules[0];
		this.birthRule = SBrules[1];
	}

	/**
	 * This method is inherited from {@link Object} and returns a clone of the meta data object
	 * @return A clone of the meta data object
     */
	@Override
	public MetaData clone() {
		MetaData clone = new MetaData();
		clone.setAuthor(author);
		clone.setComment(comment);
		clone.setName(name);
		clone.setRuleString(getRuleString());
		return clone;
	}
}