package GameOfLife;

/**
 * @version 1.0<p>
 *
 * This class is a container class for a boards metadata<p>
 * Every board in Game Of Life has an associated meta data class<p>
 *
 * @see FixedBoard
 * @see FileManagement.RLEDecoder
 * @see FileManagement.RLEEncoder
 * @see FileManagement.FileLoader
 *
 */
public class MetaData {

	private String author;
	private String name;
	private String comment;
	private String survivalRule;
	private String birthRule;

	/**
	 * Method that returns the author of the pattern
	 * @return String The name of the author associated with the pattern
     */
	public String getAuthor() {
		return author;
	}
	/**
	 * Method that sets the author of the pattern
	 */
	public void setAuthor(String author) {
        this.author = author;
	}
	/**
	 * Method that returns the name of the pattern
	 * @return String The name of the pattern
	 */
	public String getName() {
        return name;
	}
	/**
	 * Method that sets the name of the pattern
	 */
	public void setName(String name) {
        this.name = name;
	}
	/**
	 * Method that returns the comments associated with the pattern
	 * @return String The comments associated with the pattern
	 */
	public String getComment() {
        return comment;
	}
	/**
	 * Method that sets the comments associated with the pattern
	 */
	public void setComment(String comment) {
        this.comment = comment;
	}
	/**
	 * Method that returns the rule set associated with the pattern
	 * @return String[] Array containing the rule set
	 */
	public String[] getRuleString() {
        String[] rules = {
                survivalRule,
                birthRule
        };
		return rules;
	}
	/**
	 * Method that sets the rule set associated with the pattern
	 */
	public void setRuleString(String[] SBrules) {
        System.out.println("RULESTRING IN METADATA = " + SBrules[0] + " " + SBrules[1]);
		this.survivalRule = SBrules[0];
		this.birthRule = SBrules[1];
	}
}
