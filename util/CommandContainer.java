/**
 * 
 */
package org.aprs.messageit.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.aprs.messageit.packages.CommandPackage;

/**
 * @author toshiba
 *
 */
public class CommandContainer implements Iterable<CommandPackage>{
	private LinkedList<CommandPackage> theList = new LinkedList<CommandPackage>();

	public boolean add(CommandPackage arg0) {
		return theList.add(arg0);
	}

	public boolean isEmpty() {
		return theList.isEmpty();
	}

	public Iterator<CommandPackage> iterator() {
		return theList.iterator();
	}

	public CommandPackage pop() {
		return theList.pop();
	}
	
}

