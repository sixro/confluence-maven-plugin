package confluencemavenplugin;

import java.io.IOException;

public class NoSuchReadmeException extends IOException {

	private static final long serialVersionUID = 1L;

	public NoSuchReadmeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchReadmeException(String message) {
		super(message);
	}

	public NoSuchReadmeException(Throwable cause) {
		super(cause);
	}

}
