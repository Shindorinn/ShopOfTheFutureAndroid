package nl.futureworks.shopofthefuture.exception;

public class FieldValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	private int index;

	/**
	 * Constructor
	 * @param i
	 */
	public FieldValidationException(int i) {
		this.index = i;
	}

	@Override
	public String getMessage() {
		return "invalid input";
	}

	/**
	 * Gets the index
	 * @return int i
	 */
	public int getIndex() {
		return index;
	}
}
