package com.app.employeedesk.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

@Component
public class ResponseGenerator {

	private static final Logger logger = Logger.getLogger(ResponseGenerator.class);

	public ResponseEntity<Response> successResponse(TransactionContext context, Object object, HttpStatus httpStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		headers.add("Content-Type", "application/json");
		Response response = new Response();
		response.setData(object);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		logger.debug("response class is " + Data.class);
		logger.debug("response status is " + httpStatus.toString());
		ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers, httpStatus);
		return responseEntity;
	}
	public ResponseEntity<byte[]> successResponses(TransactionContext context, byte[] reportBytes, HttpStatus httpStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename("attendance report.xls").build());
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		logger.debug("response status is " + httpStatus.toString());
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(reportBytes, headers, httpStatus);
		return responseEntity;
	}

	public ResponseEntity<Response> successGetResponse(TransactionContext context, String message, Object object, HttpStatus httpStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		headers.add("Content-Type", "application/json");
		Response response = new Response();
		response.setData(object);
		response.setMessage(message);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		logger.debug("response class is " + Data.class);
		logger.debug("response status is " + httpStatus.toString());
		ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers, httpStatus);
		return responseEntity;
	} 

	public ResponseEntity<Response> errorResponse(TransactionContext context, String errorMessage,HttpStatus httpStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		headers.add("Content-Type", "application/json");
		Error error = new Error();
		error.setCode(httpStatus.toString() + "0001");
		error.setReason(errorMessage);
		Response response = new Response();
		response.setError(error);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers, httpStatus);
		return responseEntity;
	}
	public ResponseEntity<byte[]> errorResponses(TransactionContext context, String errorMessage, HttpStatus httpStatus) {
		String errorResponse = errorMessage + " - " + httpStatus.toString();
		byte[] errorBytes = errorResponse.getBytes();
		HttpHeaders headers = new HttpHeaders();
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<>(errorBytes, headers, httpStatus);
	}


		public ResponseEntity<Response> errorResponses(TransactionContext context, List<String> errorMessage, HttpStatus httpStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		headers.add("Content-Type", "application/json");
		Response response = new Response();
		response.setErrorMessages(errorMessage);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers, httpStatus);
		return responseEntity;
	}

	public TransactionContext generateTransationContext(HttpHeaders httpHeaders) {

		TransactionContext context = new TransactionContext();
		
		if(null == httpHeaders) {
			context.setCorrelationId("demo");
			context.setApplicationLabel("demo");
			return context;
		}
		if (httpHeaders.get("correlationId") != null) {
			context.setCorrelationId(httpHeaders.get("correlationId").toString());
		} else {
			context.setCorrelationId("demo");
		}
		if (httpHeaders.get("ApplicationLabel") != null) {
			context.setApplicationLabel(httpHeaders.get("ApplicationLabel").toString());
		} else {
			context.setApplicationLabel("demo");
		}
		return context;
	}
	public ResponseEntity<Response> successResponses(TransactionContext context, Object object, String otp,HttpStatus httpStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("correlationId", context.getCorrelationId());
		headers.add("ApplicationLabel", context.getApplicationLabel());
		headers.add("Content-Type", "application/json");
		Response response = new Response();
		response.setData(object);
		response.setTimeStamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
		logger.debug("response class is " + Data.class);
		logger.debug("response status is " + httpStatus.toString());
		ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(response, headers, httpStatus);
		return responseEntity;
	} 

}
