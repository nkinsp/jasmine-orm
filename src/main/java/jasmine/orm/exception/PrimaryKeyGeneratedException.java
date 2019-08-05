package jasmine.orm.exception;

public class PrimaryKeyGeneratedException extends RuntimeException{
	
	private static final long serialVersionUID = -4835244772356021707L;

	public PrimaryKeyGeneratedException(String message) {
		super(message);
	}

	public PrimaryKeyGeneratedException() {
		super();
	}

	public PrimaryKeyGeneratedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PrimaryKeyGeneratedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PrimaryKeyGeneratedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
