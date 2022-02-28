package in.bbd.pritesh.handler;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.bbd.pritesh.exception.PartNotFoundException;
import in.bbd.pritesh.exception.UomNotFoundException;
import in.bbd.pritesh.model.ErrorInfo;

@RestControllerAdvice
public class AppErrorHandler {

	/*
	@ExceptionHandler(UomNotFoundException.class)
	public ResponseEntity<String> handleUomNotFounEntity(UomNotFoundException unfe) 
	{
		
		return new ResponseEntity<>(unfe.getMessage(),HttpStatus.NOT_FOUND);
	}*/
	
	@ExceptionHandler(UomNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleUomNotFounEntity(UomNotFoundException unfe) 
	{
		
		return new ResponseEntity<>(
				new ErrorInfo(
						new Date().toString(), 
						"NOT FOUND", 
						404, 
						unfe.getMessage(), 
						"UOM MODULE")
				,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PartNotFoundException.class)
	public ResponseEntity<String> handlePartNotFounEntity(PartNotFoundException unfe) 
	{
		
		return new ResponseEntity<>(unfe.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException unfe) 
	{
		
		return new ResponseEntity<>(unfe.getMessage(),HttpStatus.NOT_FOUND);
	}
}
