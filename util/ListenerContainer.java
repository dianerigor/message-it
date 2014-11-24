/**
 * 
 */
package org.aprs.messageit.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.aprs.messageit.listener.APRSListener;
import org.aprs.messageit.listener.BaseListener;
import org.aprs.messageit.packages.DataPackage;

/**
 * @author toshiba
 *
 */
public class ListenerContainer implements Iterable<BaseListener>{
	private LinkedList<BaseListener> theList = new LinkedList<BaseListener>();

	public BaseListener findListener(String name){
		for(BaseListener s : theList){
			if(s.getName() == name){
				return s;
			}
		}
		return null;
	}
	
	public boolean add(BaseListener arg0) {
		return theList.add(arg0);
	}

	public boolean isEmpty() {
		return theList.isEmpty();
	}

	public Iterator<BaseListener> iterator() {
		return theList.iterator();
	}

	public BaseListener pop() {
		return theList.pop();
	}
	
}
