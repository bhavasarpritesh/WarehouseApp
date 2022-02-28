package in.bbd.pritesh.exception;

public class PurchaseOrderNotFound extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PurchaseOrderNotFound() {
		super();
	}
	
	public PurchaseOrderNotFound(String message) {
		super(message);
	}
}
