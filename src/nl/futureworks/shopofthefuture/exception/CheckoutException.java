package nl.futureworks.shopofthefuture.exception;

public class CheckoutException extends Exception{

	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * Constructor
	 * @param i
	 */
	public CheckoutException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
