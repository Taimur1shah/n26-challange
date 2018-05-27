package com.n26.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author taimur
 * 
 */
public class Util {
	
	/**
	 * @param currentTransactionTimestamp
	 * @return boolean
	 */
	public static boolean isValidTransaction(long currentTransactionTimestamp) {
		TimeZone.setDefault(TimeZone.getDefault());
		Long currentTime = new Date().getTime() / 1000;

		if (currentTransactionTimestamp >= currentTime)
			return true;
		return false;
	}

	public static LocalDateTime convertToLocalDateTime(Long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}
	
	public static Long converToTimeStamp(LocalDateTime localDateTime) {
		return localDateTime.toEpochSecond(ZoneOffset.UTC) * 1000;
	}
	
	
	
}
