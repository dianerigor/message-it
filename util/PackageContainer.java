/**
 * 
 */
package org.aprs.messageit.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.sender.BaseSender;

/**
 * @author toshiba
 *
 */
public class PackageContainer implements Iterable<DataPackage> {
	private LinkedList<DataPackage> theList = new LinkedList<DataPackage>();

	public boolean add(DataPackage arg0) {
		return theList.add(arg0);
	}

	public boolean isEmpty() {
		return theList.isEmpty();
	}

	public boolean remove(Object arg0) {
		return theList.remove(arg0);
	}

	public Iterator<DataPackage> iterator() {
		return theList.iterator();
	}

	public DataPackage pop() {
		return theList.pop();
	}
	
}
