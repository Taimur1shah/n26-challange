package com.n26.service;

import com.n26.model.Statistic;
import com.n26.model.Transaction;

/**
 * @author taimur
 * 
 */
public interface TransactionService {
	
	
	
	public void makeTransaction(Transaction transaction) ;
	

	public Statistic getStatistics();
	
	
}
