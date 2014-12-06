package engine.imp.render;

public class RenderingException extends RuntimeException {
	private static final long serialVersionUID = 97512299809735004L;

	public RenderingException(String exception) {
		super(exception);
	}

	public RenderingException(String exception, Exception e) {
		super(exception, e);
	}
}
