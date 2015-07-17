package org.haldokan.edge.validator;

import javax.validation.Payload;

public class ViolationSeverity {
	public interface Info extends Payload {
		
	}
	
	public interface Warn extends Payload {
		
	}
	
	public interface Error extends Payload {
		
	}

}
