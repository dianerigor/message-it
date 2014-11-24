/**
 * 
 */
package org.aprs.messageit.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.aprs.messageit.listener.APRSListener;
import org.aprs.messageit.listener.BaseListener;
import org.aprs.messageit.sender.APRSSender;
import org.aprs.messageit.sender.BaseSender;

/**
 * @author toshiba
 *
 */
public class SenderContainer implements Iterable<BaseSender> {
	private LinkedList<BaseSender> theList = new LinkedList<BaseSender>();

	public BaseSender findSender(String name){
		for(BaseSender s : theList){
			if(s.getName() == name){
				return s;
			}
		}
		return null;
	}
	
	public boolean add(BaseSender arg0) {
		return theList.add(arg0);
	}

	public boolean isEmpty() {
		return theList.isEmpty();
	}


	public Iterator<BaseSender> iterator() {
		return theList.iterator();
	}

	public BaseSender pop() {
		return theList.pop();
	}
	
}
