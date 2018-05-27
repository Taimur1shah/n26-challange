package com.n26.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.model.Statistic;
import com.n26.model.Transaction;
import com.n26.service.TransactionService;
import com.n26.util.Util;

/**
 * @author taimur
 * 
 */
@RestController
@RequestMapping("/api")
public class TransactionController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	private TransactionService transactionService;
	
	
	@RequestMapping(value="/transaction", method={ RequestMethod.POST }, consumes = "application/json")
	public ResponseEntity<String> makeTransaction(@Validated final @RequestBody(required=true) Transaction transaction) {
		try {
			if (transaction.getAmount()==null  || (transaction.getTimestamp() == null))
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			
			logger.info("Transaction Time :"+transaction.getTimestamp());
			logger.info("Current Time :"+transaction.getTimestamp());
			
			if(!Util.isValidTransaction(transaction.getTimestamp()))
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			
			logger.info("Adding new transaction in controller");
			transactionService.makeTransaction(transaction);
		
				return new ResponseEntity<String>(HttpStatus.CREATED);
			
		} catch(Exception e) {
			logger.error("Error while making transaction: " + e.getMessage());
			return new ResponseEntity<String>("Error while making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Controller method to get all statistics
	 * @return ResopnseEntity with Statistics object creation having Status 200
	 */
	@RequestMapping(value="/statistics", method={ RequestMethod.GET }, produces="application/json")
	public ResponseEntity<Object> getStatistics() {
		try {
			
			logger.info("Getting statistics in controller");
			Statistic stats = transactionService.getStatistics();
			
			return new ResponseEntity<Object>(stats, HttpStatus.OK);
			
		} catch(Exception e) {
			logger.error("Error while getting statistics: " + e.getMessage());
			return new ResponseEntity<Object>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
