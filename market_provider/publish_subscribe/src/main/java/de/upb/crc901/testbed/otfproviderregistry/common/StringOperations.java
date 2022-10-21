package de.upb.crc901.testbed.otfproviderregistry.common;

public class StringOperations {
	public static String lcs(String a, String b) {
		int[][] lengths = new int[a.length() + 1][b.length() + 1];

		// row 0 and column 0 are initialized to 0 already

		for (int i = 0; i < a.length(); i++)
			for (int j = 0; j < b.length(); j++)
				if (a.charAt(i) == b.charAt(j))
					lengths[i + 1][j + 1] = lengths[i][j] + 1;
				else
					lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j], lengths[i][j + 1]);

		// read the substring out from the matrix
		StringBuffer sb = new StringBuffer();
		for (int x = a.length(), y = b.length(); x != 0 && y != 0;) {
			if (lengths[x][y] == lengths[x - 1][y])
				x--;
			else if (lengths[x][y] == lengths[x][y - 1])
				y--;
			else {
				assert a.charAt(x - 1) == b.charAt(y - 1);
				sb.append(a.charAt(x - 1));
				x--;
				y--;
			}
		}

		return sb.reverse().toString();
	}

    public static String longestCommonPrefix(String... values) {
        if (values == null) {
            return null;
        }
        //find shortest string
        int shortestIndex = 0, len = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() < len) {
                len = values[i].length();
                shortestIndex = i;
            }
        }
        //iterate thru shortest string and try to find prefixes in the rest of the array
        for (int i = 0; i < values.length; i++) {
            if (i == shortestIndex) {
                continue; //skip same
            }
            int newLength = len;
            while (!values[i].startsWith(values[shortestIndex].substring(0, newLength)) && newLength > 0) {
                newLength--;
                len = newLength;
            }
        }
        return values[shortestIndex].substring(0, len);

    }

}
