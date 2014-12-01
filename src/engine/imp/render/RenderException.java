package engine.imp.render;

public class RenderException extends RuntimeException {
	private static final long serialVersionUID = 97512299809735004L;

	public RenderException(String exception) {
		super(exception);
	}

	public RenderException(String exception, Exception e) {
		super(exception, e);
	}
}
