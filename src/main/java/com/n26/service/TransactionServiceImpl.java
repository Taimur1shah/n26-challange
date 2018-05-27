package com.n26.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.n26.model.Statistic;
import com.n26.model.Transaction;

/**
 * @author taimur
 * 
 */

@Service
public class TransactionServiceImpl implements TransactionService {
	public static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
	private static SortedMap<Long, LinkedList<Transaction>> transactionMap = Collections.synchronizedSortedMap(new TreeMap<Long, LinkedList<Transaction>>());
	
    private Statistic statistic; 
    
	/**
	 * @return Statistic
	 */
	private Statistic initilizeStatistic() {
		Statistic statistic = new Statistic();
		statistic.setMax(Double.MIN_VALUE);
		statistic.setMin(Double.MAX_VALUE);
		statistic.setSum(0.0);
		statistic.setCount(0l);
		return statistic;
	}
	

	/**
	 * @param transaction
	 * @return 
	 */
	@Override
	public void makeTransaction(Transaction transaction) {
		LinkedList<Transaction> newList = new LinkedList<>();
		newList.add(transaction);

		if (transactionMap.size() > 60)
			transactionMap.remove(transactionMap.firstKey());
		if (!transactionMap.containsKey(transaction.getTimestamp())) {
			transactionMap.put(transaction.getTimestamp(), newList);
			log.info("Added transaction for timestamp: " + transaction.getTimestamp());
			if (statistic == null)
				statistic = initilizeStatistic();
			statistic.setCount((long) transactionMap.size());
			statistic.setMax(transaction.getAmount() > Double.MAX_VALUE ? Double.MAX_VALUE : transaction.getAmount());
			statistic.setMin(transaction.getAmount() < Double.MIN_VALUE ? Double.MIN_VALUE : transaction.getAmount());
			statistic.setSum(statistic.getSum() + transaction.getAmount());
			statistic.setAvg(statistic.getSum() / statistic.getCount());

		}
	}

 

	/**
	 * @return Statistic
	 */
	@Override
	public Statistic getStatistics() {
		
		return this.statistic;
	}
	
	

}
