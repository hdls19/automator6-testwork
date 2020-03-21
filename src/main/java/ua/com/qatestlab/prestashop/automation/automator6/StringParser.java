package ua.com.qatestlab.prestashop.automation.automator6;

public class StringParser {

	public static String parseString(String content, int from, String before, String... after) {
		if (content == null) throw new IllegalArgumentException("Content must be not null");
		if (before == null) throw new IllegalArgumentException("Before must be not null");
		if (after == null) throw new IllegalArgumentException("After must be not null");

		int start = from;
		int size = after.length;
		for (int i = 0; i < size; i++) {
			start = content.indexOf(after[i], start);
			if (start == -1) return "";
			start += after[i].length();
		}

		int end = content.indexOf(before, start);
		if (end == -1) return "";

		return content.substring(start, end);
	}

	public static String parseString(String content, String before, String... after) {
		return parseString(content, 0, before, after);
	}
}
