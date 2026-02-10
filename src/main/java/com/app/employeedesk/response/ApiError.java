package com.app.employeedesk.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus status;
    private String error;
    private Integer count;
    private List<String> errors;

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
    
    

}
