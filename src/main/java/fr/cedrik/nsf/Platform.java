/**
 *
 */
package fr.cedrik.nsf;

import java.util.zip.ZipOutputStream;

import fr.cedrik.inotes.fs.BaseZipWriter;
import fr.cedrik.inotes.fs.maildir.EMLZipWriter;
import fr.cedrik.inotes.fs.mbox.MBoxrdZipWriter;

/**
 * @author C&eacute;drik LIME
 */
public enum Platform {
	MACINTOSH {
		@Override
		public String getMBoxTypeString() {
			return "mbox";
		}
		@Override
		public BaseZipWriter getZipWriter(ZipOutputStream outZip, String baseName) {
			return new MBoxrdZipWriter(outZip, baseName);
		}
	},
	WINDOWS {
		@Override
		public String getMBoxTypeString() {
			return "eml";
		}
		@Override
		public BaseZipWriter getZipWriter(ZipOutputStream outZip, String baseName) {
			return new EMLZipWriter(outZip, baseName);
		}
	};

	public abstract String getMBoxTypeString();
	public abstract BaseZipWriter getZipWriter(ZipOutputStream outZip, String baseName);

	public static Platform getInstance(String platform) {
		return "Macintosh".equalsIgnoreCase(platform) ? MACINTOSH : WINDOWS;
	}
}
