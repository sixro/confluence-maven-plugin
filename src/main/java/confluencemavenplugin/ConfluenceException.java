package confluencemavenplugin;

public class ConfluenceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfluenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfluenceException(String message) {
		super(message);
	}

	public ConfluenceException(Throwable cause) {
		super(cause);
	}

}
