package nl.futureworks.shopofthefuture.logic;

import android.widget.EditText;
import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.exception.FieldValidationException;

public class FieldValidator {

	private static final String ALPHANUMERIC = "alphanumeric";
	private static final String NUMERIC = "numeric";
	private static final String REQUIRED = "required";
	private static final String MINLENGTH = "minlength";
	private static final String EMAIL = "email";
	private static final int MIN_CHARACTER_COUNT = 6;

	/**
	 * Validates a text field
	 * 
	 * @param editText
	 * @throws FieldValidationException
	 */
	public static void validateTextField(EditText editText)
			throws FieldValidationException {
		String tag = (String) editText.getTag();
		String[] checks = tag.split(",");
		String text = editText.getText().toString();
		for (String check : checks) {
			if (check.equals(REQUIRED)) {
				checkEmpty(text);
			} else if (check.equals(MINLENGTH)) {
				checkMinLength(text);
			} else if (check.equals(ALPHANUMERIC)) {
				checkAlphaNumeric(text);
			} else if (check.equals(NUMERIC)) {
				checkNumeric(text);
			} else if (check.equals(EMAIL)) {
				checkEmail(text);
			}
		}
	}

	/**
	 * @param text
	 * @return void
	 */
	public static void checkEmpty(String text) throws FieldValidationException {
		if (text == null || text.length() == 0)
			throw new FieldValidationException(
					R.string.field_validation_no_input);
	}

	/**
	 * @param text
	 * @return void
	 */
	public static void checkNumeric(String text)
			throws FieldValidationException {
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isDigit(text.charAt(i)))
				throw new FieldValidationException(
						R.string.field_validation_not_numeric);
		}
	}

	/**
	 * @param text
	 * @return void
	 */
	public static void checkAlphaNumeric(String text)
			throws FieldValidationException {
		for (int i = 0; i < text.length(); i++) {
			if (!Character.isLetterOrDigit(text.charAt(i)))
				throw new FieldValidationException(
						R.string.field_validation_not_alphanumeric);
		}
	}

	/**
	 * @param text
	 * @return void
	 */
	public static void checkMinLength(String text)
			throws FieldValidationException {
		if (text.length() < MIN_CHARACTER_COUNT)
			throw new FieldValidationException(
					R.string.field_validation_too_short);
	}

	/**
	 * @param text
	 * @return void
	 */
	public static void checkEmail(String text) throws FieldValidationException {
		if (!text.contains("@"))
			throw new FieldValidationException(
					R.string.field_validation_not_an_email);
	}
}
