/*
 * Copyright (C) 2021 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package photohosting.services;

import java.text.Normalizer;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class SanitizeServiceImpl implements SanitizeService {
	
    /**
	 * This method transforms the text passed as an argument to a text without spaces,
	 * html entities, accents, dots and extranges characters (only %,a-z,A-Z,0-9, ,_ and - are allowed).
	 *
	 * Borrowed from Wordpress: file wp-includes/formatting.php, function sanitize_title_with_dashes
	 * http://core.svn.wordpress.org/trunk/wp-includes/formatting.php
	 */
	@Override
	public String sanitizeWithDashes(String text) {

		if (text == null || text.length() == 0) {
			return "";
		}

		// Preserve escaped octets
		text = text.replaceAll("%([a-fA-F0-9][a-fA-F0-9])", "---$1---");
		text = text.replaceAll("%", "");
		text = text.replaceAll("---([a-fA-F0-9][a-fA-F0-9])---", "%$1");

		// Remove accents
		text = removeAccents(text);

		// To lower case
		text = text.toLowerCase();

		// Kill entities
		text = text.replaceAll("&.+?;", "");

		// Dots -> ''
		text = text.replaceAll("\\.", "");

		// Remove any character except %a-zA-Z0-9 _-
		text = text.replaceAll("[^%a-zA-Z0-9 _-]", "");

		// Trim
		text = text.trim();

		// Spaces -> dashes
		text = text.replaceAll("\\s+", "-");

		// Dashes -> dash
		text = text.replaceAll("-+", "-");

		// It must end in a letter or digit, otherwise we strip the last char
//		if (!text[-1].charAt(0).isLetterOrDigit()) text = text[0..-2]
		while(!Character.isLetterOrDigit(text.charAt(text.length() - 1)) && text.length() > 0) {
			text = text.substring(0, text.length() - 2);
		}

		return text;
	}
	
	/**
	 * Converts all accent characters to ASCII characters.
	 *
	 * If there are no accent characters, then the string given is just returned.
	 *
	 */
	private String removeAccents(String  text) {
		return Normalizer.normalize(text, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	
}
