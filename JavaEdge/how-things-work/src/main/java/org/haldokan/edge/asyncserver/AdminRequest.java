package org.haldokan.edge.asyncserver;

public class AdminRequest implements ServiceRequest {
    private final AdminRequestType type;

    public AdminRequest(AdminRequestType type) {
	this.type = type;
    }

    @Override
    public ServiceName getServiceName() {
	return ServiceName.ADMIN;
    }

    public AdminRequestType getType() {
	return type;
    }

    @Override
    public String toString() {
	return "AdminRequest [type=" + type + "]";
    }
}
