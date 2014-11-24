/**
 * 
 */
package org.aprs.messageit.util;

import org.aprs.messageit.packages.DataPackage;

/**
 * @author toshiba
 *
 */
public interface ListenerCallback {
	public void uploadDataPackage(DataPackage thePackage);
}
