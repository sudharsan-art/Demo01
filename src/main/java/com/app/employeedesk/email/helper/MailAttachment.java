package com.app.employeedesk.email.helper;

import lombok.Getter;

@Getter
public class MailAttachment {

	private String fileName;
	private byte[] inputStreamSource;

	public MailAttachment(String fileName, byte[] inputStreamSource) {
		super();
		this.fileName = fileName;
		this.inputStreamSource = inputStreamSource;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setInputStreamSource(byte[] inputStreamSource) {
		this.inputStreamSource = inputStreamSource;
	}

}
