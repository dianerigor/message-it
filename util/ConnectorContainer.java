/**
 * 
 */
package org.aprs.messageit.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.aprs.messageit.connector.BaseConnector;
import org.aprs.messageit.packages.DataPackage;

/**
 * @author toshiba
 *
 */
public class ConnectorContainer implements Iterable<BaseConnector> {
	private LinkedList<BaseConnector> theList = new LinkedList<BaseConnector>();

	public boolean add(BaseConnector arg0) {
		return theList.add(arg0);
	}

	public boolean isEmpty() {
		return theList.isEmpty();
	}

	public Iterator<BaseConnector> iterator() {
		return theList.iterator();
	}

	public BaseConnector pop() {
		return theList.pop();
	}
	
}
