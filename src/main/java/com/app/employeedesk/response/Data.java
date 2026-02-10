package com.app.employeedesk.response;

import lombok.Getter;


@Getter
public class Data {

	private Object object;

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "Data [object=" + object + "]";
	}

}
