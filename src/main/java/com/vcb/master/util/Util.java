package com.vcb.master.util;

import org.jasypt.util.text.BasicTextEncryptor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Util {
	public static String getDate6(String format){
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
	public static String getDateFormat(Date date, String format){
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	public static Date formatDate(String sDate1, String format) {
		try {
			return new SimpleDateFormat(format).parse(sDate1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static Date getDayAgo(int d) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -d);
		return cal.getTime();
	}
	
	public static String encrypt(String passText) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("vietcombank-apicore_encrypt");
		
		return textEncryptor.encrypt(passText);
	}
	
	public static String decrypt(String passText) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("vietcombank-apicore_encrypt");
		
		return textEncryptor.decrypt(passText);
	}
	public static String formatSeq(String p_seqno) {
		String m_seqno = p_seqno;
		if(p_seqno.length()>5) {
			m_seqno = p_seqno.substring(p_seqno.length()-5);
		}
		else {
			m_seqno = p_seqno;
		}
		if(Integer.parseInt(m_seqno) == 0) m_seqno = "10000";
		if(Integer.parseInt(m_seqno) < 10000) {
			m_seqno = String.valueOf( Integer.parseInt(m_seqno)+10000 );
		}
		return m_seqno;
	}
	public static boolean isDDAliasAccount(String acctNo) throws Exception {
        String acctNumRaw = String.valueOf(Long.parseLong(acctNo));
        // Alias
        if(acctNumRaw.length() > 10) {
               return true;
        } else {
               return false;
        }
 }

}
