package nl.futureworks.shopofthefuture.exception;

public class ShoppingListModificationException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Constructor
	 * @param i
	 */
	public ShoppingListModificationException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
