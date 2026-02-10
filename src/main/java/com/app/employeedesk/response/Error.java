package com.app.employeedesk.response;


import lombok.Getter;

@Getter
public class Error {

	private String code;
	private String Reason;

	public void setCode(String code) {
		this.code = code;
	}

	public void setReason(String reason) {
		Reason = reason;
	}
	@Override
	public String toString() {
		return "Error [code=" + code + ", Reason=" + Reason + "]";
	}
	
	
}
