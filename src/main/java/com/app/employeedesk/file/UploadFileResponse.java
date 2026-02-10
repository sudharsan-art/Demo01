package com.app.employeedesk.file;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class UploadFileResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setSize(long size) {
		this.size = size;
	}

    
}
