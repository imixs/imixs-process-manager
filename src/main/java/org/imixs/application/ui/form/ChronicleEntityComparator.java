package org.imixs.application.ui.form;

import java.text.Collator;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Compares two ChronicleEntities by its date value.
 * 
 * @author rsoika
 * 
 */
public class ChronicleEntityComparator implements Comparator<ChronicleEntity> {
	private final boolean ascending;

	public ChronicleEntityComparator(boolean ascending, Locale locale) {
		this.ascending = ascending;
	}

	/**
	 * This method sorts by date value
	 * 
	 * @param ascending
	 */
	public ChronicleEntityComparator(boolean ascending) {
		this.ascending = ascending;

	}

	/**
	 * This method sorts by date value ascending
	 */
	public ChronicleEntityComparator() {
		this.ascending = true;

	}

        @Override
	public int compare(ChronicleEntity a, ChronicleEntity b) {

		Date dateA = a.getDate();
		Date dateB = b.getDate();
		if (dateA == null && dateB != null) {
			return 1;
		}
		if (dateB == null && dateA != null) {
			return -1;
		}
		if (dateB == null && dateA == null) {
			return 0;
		}

		int result = dateB.compareTo(dateA);
		if (!this.ascending) {
			result = -result;
		}
		return result;

	}

}
