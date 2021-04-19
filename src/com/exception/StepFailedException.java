package com.exception;

public class StepFailedException extends RuntimeException{
	    private static final long serialVersionUID = 1L;

	    public StepFailedException(String message) {
	        super(message);
	    }
	    
	    public StepFailedException(Throwable t) {
	        //super(.getMessageForThrowable(t), t);
	    }
	    
	    public StepFailedException(String message, Throwable t) {
	        super(message, t);
	    }	
	
}
