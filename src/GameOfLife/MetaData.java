package GameOfLife;

/**
 * @author tleirvik, rlundh, srrogeberg<p>
 * @version 1.0<p>
 *
 * This class is a container class for a boards metadata<p>
 *
 * @see Board
 * @see FileManagement.RLEDecoder
 * @see FileManagement.RLEEncoder
 *
 */
public class MetaData {

	private String author;
	private String name;
	private String comment;
	private String survivalRule;
	private String birthRule;

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

	public String[] getRuleString() {
        String[] rules = {
                survivalRule,
                birthRule
        };
		return rules;
	}
	public void setRuleString(String[] SBrules) {
		this.survivalRule = SBrules[0];
		this.birthRule = SBrules[1];
	}
        
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
