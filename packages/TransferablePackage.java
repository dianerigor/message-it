/**
 * 
 */
package org.aprs.messageit.packages;

/**
 * @author toshiba
 *
 */
public abstract class TransferablePackage {
	public abstract void parsePackage(DataPackage thePackage);
	public abstract DataPackage toDataPackage();
}
