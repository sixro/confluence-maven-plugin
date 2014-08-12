package confluencemavenplugin;

public class DeployException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeployException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeployException(String message) {
		super(message);
	}

	public DeployException(Throwable cause) {
		super(cause);
	}

}
