package com.app.employeedesk.response;

import lombok.Getter;


@Getter
public class TransactionContext {

	private String correlationId;
	private String ApplicationLabel;

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public void setApplicationLabel(String applicationLabel) {
		ApplicationLabel = applicationLabel;
	}

	@Override
	public String toString() {
		return "TransactionContext [correlationId=" + correlationId + ", ApplicationLabel=" + ApplicationLabel + "]";
	}

}
