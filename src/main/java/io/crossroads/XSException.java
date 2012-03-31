package io.crossroads;

public class XSException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int errorCode = 0;

  public XSException(String message, int errorCode) {
    super(message);

    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
