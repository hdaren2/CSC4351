package Parse;
import ErrorMsg.ErrorMsg;
import java_cup.runtime.Symbol;


public class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final char YYEOF = '\uFFFF';

private void newline() {
  errorMsg.newline(yychar);
}
private void err(int pos, String s) {
  errorMsg.error(pos, s);
}
private void err(String s) {
  err(yychar, s);
}
private java_cup.runtime.Symbol tok(int kind) {
    return tok(kind, null);
}
private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar + yylength(), value);
}
private ErrorMsg errorMsg;
public Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg = e;
}
private String processEscapeSequences(String str) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == '\\' && i + 1 < str.length()) {
            char nextChar = str.charAt(i + 1);
            switch (nextChar) {
                case 'n': result.append('\n'); break;
                case 't': result.append('\t'); break;
                case 'r': result.append('\r'); break;
                case 'b': result.append('\b'); break;
                case 'f': result.append('\f'); break;
                case 'a': result.append('\007'); break;
                case 'v': result.append('\013'); break;
                case '\\': result.append('\\'); break;
                case '\'': result.append('\''); break;
                case '\"': result.append('\"'); break;
                case '0': result.append('\0'); break;
                case 'x': 
                    // Handle hex escape sequence \xHH
                    if (i + 3 < str.length()) {
                        try {
                            String hexStr = str.substring(i + 2, i + 4);
                            int hexValue = Integer.parseInt(hexStr, 16);
                            result.append((char) hexValue);
                            i += 3; // Skip the x and two hex digits
                            continue;
                        } catch (NumberFormatException e) {
                            // Invalid hex, treat as literal
                            result.append('\\');
                            result.append(nextChar);
                        }
                    } else {
                        result.append('\\');
                        result.append(nextChar);
                    }
                    break;
                default:
                    // Handle octal escape sequence \OOO (1-3 octal digits)
                    if (nextChar >= '0' && nextChar <= '7') {
                        StringBuilder octalStr = new StringBuilder();
                        int j = i + 1;
                        while (j < str.length() && j < i + 4 && str.charAt(j) >= '0' && str.charAt(j) <= '7') {
                            octalStr.append(str.charAt(j));
                            j++;
                        }
                        if (octalStr.length() > 0) {
                            try {
                                int octalValue = Integer.parseInt(octalStr.toString(), 8);
                                result.append((char) octalValue);
                                i = j - 1; // Skip the octal digits
                                continue;
                            } catch (NumberFormatException e) {
                                // Invalid octal, treat as literal
                                result.append('\\');
                                result.append(nextChar);
                            }
                        } else {
                            result.append('\\');
                            result.append(nextChar);
                        }
                    } else {
                        result.append('\\');
                        result.append(nextChar);
                    }
                    break;
            }
            i++;
        } else {
            result.append(str.charAt(i));
        }
    }
    return result.toString();
}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private char yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_start () {
		++yychar;
		++yy_buffer_start;
	}
	private void yy_pushback () {
		--yy_buffer_end;
	}
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
private int [][] unpackFromString(int size1, int size2, String st)
    {
      int colonIndex = -1;
      String lengthString;
      int sequenceLength = 0;
      int sequenceInteger = 0;
      int commaIndex;
      String workString;
      int res[][] = new int[size1][size2];
      for (int i= 0; i < size1; i++)
	for (int j= 0; j < size2; j++)
	  {
	    if (sequenceLength == 0) 
	      {	
		commaIndex = st.indexOf(',');
		if (commaIndex == -1)
		  workString = st;
		else
		  workString = st.substring(0, commaIndex);
		st = st.substring(commaIndex+1);
		colonIndex = workString.indexOf(':');
		if (colonIndex == -1)
		  {
		    res[i][j] = Integer.parseInt(workString);
		  }
		else 
		  {
		    lengthString = workString.substring(colonIndex+1);  
		    sequenceLength = Integer.parseInt(lengthString);
		    workString = workString.substring(0,colonIndex);
		    sequenceInteger = Integer.parseInt(workString);
		    res[i][j] = sequenceInteger;
		    sequenceLength--;
		  }
	      }
	    else 
	      {
		res[i][j] = sequenceInteger;
		sequenceLength--;
	      }
	  }
      return res;
    }
	private int yy_acpt[] = {
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR
	};
	private int yy_cmap[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 1, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		2, 3, 4, 0, 0, 5, 6, 7,
		8, 9, 10, 11, 12, 13, 14, 15,
		16, 17, 17, 17, 17, 17, 17, 17,
		17, 17, 18, 19, 20, 21, 22, 23,
		0, 24, 24, 24, 24, 24, 24, 25,
		25, 25, 25, 25, 25, 25, 25, 25,
		25, 25, 25, 25, 25, 25, 25, 25,
		26, 25, 25, 27, 28, 29, 30, 25,
		0, 31, 32, 33, 34, 35, 36, 37,
		38, 39, 25, 40, 41, 42, 43, 44,
		45, 25, 46, 47, 48, 49, 50, 51,
		52, 53, 54, 55, 56, 57, 58, 0
		
	};
	private int yy_rmap[] = {
		0, 1, 1, 1, 2, 3, 4, 1,
		1, 5, 6, 1, 7, 8, 9, 10,
		1, 1, 11, 12, 13, 1, 14, 1,
		1, 15, 1, 16, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1,
		1, 17, 1, 18, 1, 1, 1, 19,
		1, 1, 1, 1, 20, 14, 1, 1,
		1, 1, 21, 1, 1, 14, 14, 14,
		14, 1, 14, 14, 14, 14, 14, 14,
		14, 14, 14, 14, 14, 14, 14, 14,
		14, 14, 14, 14, 14, 14, 14, 14,
		14, 14, 14, 14, 14, 14, 22, 22,
		23, 24, 25, 26, 27, 28, 29, 30,
		31, 32, 33, 34, 35, 36, 21, 37,
		38, 39, 40, 41, 42, 43, 44, 45,
		46, 47, 48, 49, 50, 51, 52, 53,
		54, 55, 56, 57, 58, 59, 60, 61,
		62, 63, 64, 65, 66, 67, 68, 69,
		70, 71, 72, 73, 74, 75, 76, 77,
		78, 79, 80, 81, 82, 83, 84, 85,
		86, 87, 88, 89, 90, 91, 92, 93,
		94, 95, 96, 97, 98, 99, 100, 101,
		102, 103, 104, 105, 106, 107, 108, 109,
		110, 111, 112, 113, 114, 115, 116, 117,
		118, 119, 120, 121, 122, 123, 124, 125,
		126, 127, 128, 129, 130, 131, 132, 133,
		134, 135, 136, 137, 138, 139, 140, 141,
		142 
	};
	private int yy_nxt[][] = unpackFromString(143,59,
"1,2,3,4,95,5,6,99,7,8,9,10,11,12,13,14,15,96,16,17,18,19,20,21,22:3,23,102,24,25,170,195,171,97,172,140,173,22,100,22,174,22:4,210,196,216,197,141,198,22:3,26,27,28,29,-1:80,30,-1:58,32,-1:43,33,-1:14,34,-1:58,35,-1:48,36,-1:9,37,-1:50,38,-1:7,39,40,-1:50,106,-1:54,108,-1:4,41,-1:5,42,-1:53,96:2,-1:8,110,-1:25,110,-1:26,43,44,-1:58,45,-1:58,46,47,-1:52,22:2,-1:6,22:3,-1:4,22:24,-1:25,51,-1:58,54,-1:34,55,-1:2,41,-1,41:57,-1:21,59,-1:58,60,-1:53,22:2,-1:6,22:3,-1:4,22:18,181,22:5,-1:20,58:2,-1:6,58,-1:6,58:6,-1:22,94:4,31,94:23,98,94:30,-1:16,96:2,-1:57,22:2,-1:6,22:3,-1:4,22:4,211,22:8,52,22:10,-1:8,94,-1:2,94,-1:20,94,-1:14,94,-1:2,94,-1,94,-1:10,101:7,-1,101:20,104,101:30,-1:16,22:2,-1:6,22:3,-1:4,22:5,53,22:6,107,22:11,-1:11,56,-1:87,48,-1:9,49,-1,50,-1:26,22:2,-1:6,22:3,-1:4,22:15,61,22:8,-1:8,101,-1:2,101,-1:20,101,-1:14,101,-1:2,101,-1,101,-1:26,22:2,-1:6,22:3,-1:4,22:12,62,22:11,-1:18,57,-1:60,22:2,-1:6,22:3,-1:4,22:17,63,22:6,-1:4,108:10,112,108:48,-1:16,22:2,-1:6,22:3,-1:4,22:15,64,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:13,66,22:10,-1:4,108:10,112,108:4,65,108:43,-1:16,22:2,-1:6,22:3,-1:4,22:4,67,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:15,68,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:4,69,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:11,70,22:12,-1:20,22:2,-1:6,22:3,-1:4,22:13,71,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:6,72,22:17,-1:20,22:2,-1:6,22:3,-1:4,22:3,73,22:20,-1:20,22:2,-1:6,22:3,-1:4,22:9,74,22:14,-1:20,22:2,-1:6,22:3,-1:4,22:17,75,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:17,76,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:17,77,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:12,78,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:4,79,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:4,80,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:12,81,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:12,82,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:3,83,22:20,-1:20,22:2,-1:6,22:3,-1:4,22:5,84,22:18,-1:20,22:2,-1:6,22:3,-1:4,22:2,85,22:21,-1:20,22:2,-1:6,22:3,-1:4,22:17,86,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:7,87,22:16,-1:20,22:2,-1:6,22:3,-1:4,22:17,88,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:5,89,22:18,-1:20,22:2,-1:6,22:3,-1:4,22:4,90,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:15,91,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:3,92,22:20,-1:20,22:2,-1:6,22:3,-1:4,22:4,93,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:10,177,22:2,103,22:4,105,22:5,-1:20,22:2,-1:6,22:3,-1:4,109,22:12,149,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:17,111,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:16,113,22:7,-1:20,22:2,-1:6,22:3,-1:4,114,22:23,-1:20,22:2,-1:6,22:3,-1:4,22:16,115,22:7,-1:20,22:2,-1:6,22:3,-1:4,22:18,116,22:5,-1:20,22:2,-1:6,22:3,-1:4,22:17,117,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:12,118,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:8,119,22,215,22:13,-1:20,22:2,-1:6,22:3,-1:4,120,22:23,-1:20,22:2,-1:6,22:3,-1:4,22:16,121,206,22:6,-1:20,22:2,-1:6,22:3,-1:4,122,22:23,-1:20,22:2,-1:6,22:3,-1:4,22:15,123,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:13,124,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:10,125,22:13,-1:20,22:2,-1:6,22:3,-1:4,22:10,126,22:13,-1:20,22:2,-1:6,22:3,-1:4,22:15,127,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:15,128,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:4,129,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:13,130,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:8,131,22:15,-1:20,22:2,-1:6,22:3,-1:4,22:2,132,22:21,-1:20,22:2,-1:6,22:3,-1:4,22:2,133,22:21,-1:20,22:2,-1:6,22:3,-1:4,22:10,134,22:13,-1:20,22:2,-1:6,22:3,-1:4,22:4,135,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:18,136,22:5,-1:20,22:2,-1:6,22:3,-1:4,22:4,137,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:4,138,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:10,139,22:13,-1:20,22:2,-1:6,22:3,-1:4,22:18,142,22:5,-1:20,22:2,-1:6,22:3,-1:4,143,22:6,144,22:5,176,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:10,145,22,146,22:8,199,22:2,-1:20,22:2,-1:6,22:3,-1:4,22:13,147,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:13,148,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:4,150,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:12,151,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:13,152,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:13,153,22:10,-1:20,22:2,-1:6,22:3,-1:4,22:8,154,22:7,214,22:7,-1:20,22:2,-1:6,22:3,-1:4,22:8,155,22:15,-1:20,22:2,-1:6,22:3,-1:4,22,156,22:22,-1:20,22:2,-1:6,22:3,-1:4,22:4,157,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:18,158,22:5,-1:20,22:2,-1:6,22:3,-1:4,22:12,159,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:4,160,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:17,161,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:18,162,22:5,-1:20,22:2,-1:6,22:3,-1:4,22:17,163,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:18,164,22:5,-1:20,22:2,-1:6,22:3,-1:4,22:3,165,22:20,-1:20,22:2,-1:6,22:3,-1:4,22:12,166,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:17,167,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:12,168,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:8,169,22:15,-1:20,22:2,-1:6,22:3,-1:4,22:15,175,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:7,178,201,22:8,202,22:2,203,22:3,-1:20,22:2,-1:6,22:3,-1:4,22:12,179,22:11,-1:20,22:2,-1:6,22:3,-1:4,22:7,180,22:16,-1:20,22:2,-1:6,22:3,-1:4,22:17,182,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:6,213,22:10,183,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:6,184,22:16,185,-1:20,22:2,-1:6,22:3,-1:4,186,22:14,187,22:8,-1:20,22:2,-1:6,22:3,-1:4,22:8,188,22:15,-1:20,22:2,-1:6,22:3,-1:4,189,22:23,-1:20,22:2,-1:6,22:3,-1:4,22:4,190,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:8,191,22:15,-1:20,22:2,-1:6,22:3,-1:4,22:16,192,22:7,-1:20,22:2,-1:6,22:3,-1:4,22:6,193,22:17,-1:20,22:2,-1:6,22:3,-1:4,22:17,194,22:6,-1:20,22:2,-1:6,22:3,-1:4,22:4,200,22:19,-1:20,22:2,-1:6,22:3,-1:4,22:5,204,22:18,-1:20,22:2,-1:6,22:3,-1:4,22:14,205,22:9,-1:20,22:2,-1:6,22:3,-1:4,22:8,207,22:15,-1:20,22:2,-1:6,22:3,-1:4,22:8,208,22:15,-1:20,22:2,-1:6,22:3,-1:4,209,22:23,-1:20,22:2,-1:6,22:3,-1:4,22:22,212,22,-1:4");
	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		char yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			if (YYEOF != yy_lookahead) {
				yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YYEOF == yy_lookahead && true == yy_initial) {

return tok(sym.EOF, null);
				}
				else if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_to_mark();
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_pushback();
					}
					if (0 != (YY_START & yy_anchor)) {
						yy_move_start();
					}
					switch (yy_last_accept_state) {
					case 1:
						{ err("Illegal character: '" + yytext() + "'"); }
					case -2:
						break;
					case 2:
						{ newline(); }
					case -3:
						break;
					case 3:
						{ /* ignore space */ }
					case -4:
						break;
					case 4:
						{ return tok(sym.NOT); }
					case -5:
						break;
					case 5:
						{ return tok(sym.MODULUS); }
					case -6:
						break;
					case 6:
						{ return tok(sym.BITWISEAND); }
					case -7:
						break;
					case 7:
						{ return tok(sym.LPAREN); }
					case -8:
						break;
					case 8:
						{ return tok(sym.RPAREN); }
					case -9:
						break;
					case 9:
						{ return tok(sym.TIMES); }
					case -10:
						break;
					case 10:
						{ return tok(sym.PLUS); }
					case -11:
						break;
					case 11:
						{ return tok(sym.COMMA); }
					case -12:
						break;
					case 12:
						{ return tok(sym.MINUS); }
					case -13:
						break;
					case 13:
						{ return tok(sym.PERIOD); }
					case -14:
						break;
					case 14:
						{ return tok(sym.DIVIDE); }
					case -15:
						break;
					case 15:
						{ return tok(sym.DECIMAL_LITERAL, Integer.valueOf(yytext())); }
					case -16:
						break;
					case 16:
						{ return tok(sym.COLON); }
					case -17:
						break;
					case 17:
						{ return tok(sym.SEMICOLON); }
					case -18:
						break;
					case 18:
						{ return tok(sym.LT); }
					case -19:
						break;
					case 19:
						{ return tok(sym.ASSIGN); }
					case -20:
						break;
					case 20:
						{ return tok(sym.GT); }
					case -21:
						break;
					case 21:
						{ return tok(sym.QUESTION); }
					case -22:
						break;
					case 22:
						{ return tok(sym.ID, yytext()); }
					case -23:
						break;
					case 23:
						{ return tok(sym.LBRACK); }
					case -24:
						break;
					case 24:
						{ return tok(sym.RBRACK); }
					case -25:
						break;
					case 25:
						{ return tok(sym.BWISEXOR); }
					case -26:
						break;
					case 26:
						{ return tok(sym.LBRACE); }
					case -27:
						break;
					case 27:
						{ return tok(sym.BWISEOR); }
					case -28:
						break;
					case 28:
						{ return tok(sym.RBRACE); }
					case -29:
						break;
					case 29:
						{ return tok(sym.TILDE); }
					case -30:
						break;
					case 30:
						{ return tok(sym.NEQ); }
					case -31:
						break;
					case 31:
						{
    String content = yytext().substring(1, yytext().length() - 1);
    String processed = processEscapeSequences(content);
    return tok(sym.STRING_LITERAL, processed);
}
					case -32:
						break;
					case 32:
						{ return tok(sym.MODASSIGN); }
					case -33:
						break;
					case 33:
						{ return tok(sym.AND); }
					case -34:
						break;
					case 34:
						{ return tok(sym.BWISEANDASSIGN); }
					case -35:
						break;
					case 35:
						{ return tok(sym.MULASSIGN); }
					case -36:
						break;
					case 36:
						{ return tok(sym.INCREMENT); }
					case -37:
						break;
					case 37:
						{ return tok(sym.ADDASSIGN); }
					case -38:
						break;
					case 38:
						{ return tok(sym.DECREMENT); }
					case -39:
						break;
					case 39:
						{ return tok(sym.SUBASSIGN); }
					case -40:
						break;
					case 40:
						{ return tok(sym.ARROW); }
					case -41:
						break;
					case 41:
						{ /* ignore */ }
					case -42:
						break;
					case 42:
						{ return tok(sym.DIVASSIGN); }
					case -43:
						break;
					case 43:
						{ return tok(sym.LSHIFT); }
					case -44:
						break;
					case 44:
						{ return tok(sym.LE); }
					case -45:
						break;
					case 45:
						{ return tok(sym.EQ); }
					case -46:
						break;
					case 46:
						{ return tok(sym.GE); }
					case -47:
						break;
					case 47:
						{ return tok(sym.RSHIFT); }
					case -48:
						break;
					case 48:
						{ /* ignore form feed */ }
					case -49:
						break;
					case 49:
						{ /* ignore carriage return */ }
					case -50:
						break;
					case 50:
						{ /* ignore tab */ }
					case -51:
						break;
					case 51:
						{ return tok(sym.BWISEXORASSIGN); }
					case -52:
						break;
					case 52:
						{ return tok(sym.DO); }
					case -53:
						break;
					case 53:
						{ return tok(sym.IF); }
					case -54:
						break;
					case 54:
						{ return tok(sym.BWISEORASSIGN); }
					case -55:
						break;
					case 55:
						{ return tok(sym.OR); }
					case -56:
						break;
					case 56:
						{
    String content = yytext().substring(1, yytext().length() - 1);
    String processed = processEscapeSequences(content);
    return tok(sym.CHAR_LITERAL, processed);
}
					case -57:
						break;
					case 57:
						{ return tok(sym.ELIPSES); }
					case -58:
						break;
					case 58:
						{ 
    String hexStr = yytext().substring(2); // Remove "0x" or "0X" prefix
    return tok(sym.HEX_LITERAL, Integer.valueOf(hexStr, 16)); 
}
					case -59:
						break;
					case 59:
						{ return tok(sym.LSHIFTASSIGN); }
					case -60:
						break;
					case 60:
						{ return tok(sym.RSHIFTASSIGN); }
					case -61:
						break;
					case 61:
						{ return tok(sym.FOR); }
					case -62:
						break;
					case 62:
						{ return tok(sym.FUN); }
					case -63:
						break;
					case 63:
						{ return tok(sym.INT); }
					case -64:
						break;
					case 64:
						{ return tok(sym.VAR); }
					case -65:
						break;
					case 65:
						{ /* ignore */ }
					case -66:
						break;
					case 66:
						{ return tok(sym.AUTO); }
					case -67:
						break;
					case 67:
						{ return tok(sym.CASE); }
					case -68:
						break;
					case 68:
						{ return tok(sym.CHAR); }
					case -69:
						break;
					case 69:
						{ return tok(sym.ELSE); }
					case -70:
						break;
					case 70:
						{ return tok(sym.ENUM); }
					case -71:
						break;
					case 71:
						{ return tok(sym.GOTO); }
					case -72:
						break;
					case 72:
						{ return tok(sym.LONG); }
					case -73:
						break;
					case 73:
						{ return tok(sym.VOID); }
					case -74:
						break;
					case 74:
						{ return tok(sym.BREAK); }
					case -75:
						break;
					case 75:
						{ return tok(sym.CONST); }
					case -76:
						break;
					case 76:
						{ return tok(sym.FLOAT); }
					case -77:
						break;
					case 77:
						{ return tok(sym.SHORT); }
					case -78:
						break;
					case 78:
						{ return tok(sym.UNION); }
					case -79:
						break;
					case 79:
						{ return tok(sym.WHILE); }
					case -80:
						break;
					case 80:
						{ return tok(sym.DOUBLE); }
					case -81:
						break;
					case 81:
						{ return tok(sym.EXTERN); }
					case -82:
						break;
					case 82:
						{ return tok(sym.RETURN); }
					case -83:
						break;
					case 83:
						{ return tok(sym.SIGNED); }
					case -84:
						break;
					case 84:
						{ return tok(sym.SIZEOF); }
					case -85:
						break;
					case 85:
						{ return tok(sym.STATIC); }
					case -86:
						break;
					case 86:
						{ return tok(sym.STRUCT); }
					case -87:
						break;
					case 87:
						{ return tok(sym.SWITCH); }
					case -88:
						break;
					case 88:
						{ return tok(sym.DEFAULT); }
					case -89:
						break;
					case 89:
						{ return tok(sym.TYPEDEF); }
					case -90:
						break;
					case 90:
						{ return tok(sym.CONTINUE); }
					case -91:
						break;
					case 91:
						{ return tok(sym.REGISTER); }
					case -92:
						break;
					case 92:
						{ return tok(sym.UNSIGNED); }
					case -93:
						break;
					case 93:
						{ return tok(sym.VOLATILE); }
					case -94:
						break;
					case 95:
						{ err("Illegal character: '" + yytext() + "'"); }
					case -95:
						break;
					case 96:
						{ return tok(sym.DECIMAL_LITERAL, Integer.valueOf(yytext())); }
					case -96:
						break;
					case 97:
						{ return tok(sym.ID, yytext()); }
					case -97:
						break;
					case 99:
						{ err("Illegal character: '" + yytext() + "'"); }
					case -98:
						break;
					case 100:
						{ return tok(sym.ID, yytext()); }
					case -99:
						break;
					case 102:
						{ err("Illegal character: '" + yytext() + "'"); }
					case -100:
						break;
					case 103:
						{ return tok(sym.ID, yytext()); }
					case -101:
						break;
					case 105:
						{ return tok(sym.ID, yytext()); }
					case -102:
						break;
					case 107:
						{ return tok(sym.ID, yytext()); }
					case -103:
						break;
					case 109:
						{ return tok(sym.ID, yytext()); }
					case -104:
						break;
					case 111:
						{ return tok(sym.ID, yytext()); }
					case -105:
						break;
					case 113:
						{ return tok(sym.ID, yytext()); }
					case -106:
						break;
					case 114:
						{ return tok(sym.ID, yytext()); }
					case -107:
						break;
					case 115:
						{ return tok(sym.ID, yytext()); }
					case -108:
						break;
					case 116:
						{ return tok(sym.ID, yytext()); }
					case -109:
						break;
					case 117:
						{ return tok(sym.ID, yytext()); }
					case -110:
						break;
					case 118:
						{ return tok(sym.ID, yytext()); }
					case -111:
						break;
					case 119:
						{ return tok(sym.ID, yytext()); }
					case -112:
						break;
					case 120:
						{ return tok(sym.ID, yytext()); }
					case -113:
						break;
					case 121:
						{ return tok(sym.ID, yytext()); }
					case -114:
						break;
					case 122:
						{ return tok(sym.ID, yytext()); }
					case -115:
						break;
					case 123:
						{ return tok(sym.ID, yytext()); }
					case -116:
						break;
					case 124:
						{ return tok(sym.ID, yytext()); }
					case -117:
						break;
					case 125:
						{ return tok(sym.ID, yytext()); }
					case -118:
						break;
					case 126:
						{ return tok(sym.ID, yytext()); }
					case -119:
						break;
					case 127:
						{ return tok(sym.ID, yytext()); }
					case -120:
						break;
					case 128:
						{ return tok(sym.ID, yytext()); }
					case -121:
						break;
					case 129:
						{ return tok(sym.ID, yytext()); }
					case -122:
						break;
					case 130:
						{ return tok(sym.ID, yytext()); }
					case -123:
						break;
					case 131:
						{ return tok(sym.ID, yytext()); }
					case -124:
						break;
					case 132:
						{ return tok(sym.ID, yytext()); }
					case -125:
						break;
					case 133:
						{ return tok(sym.ID, yytext()); }
					case -126:
						break;
					case 134:
						{ return tok(sym.ID, yytext()); }
					case -127:
						break;
					case 135:
						{ return tok(sym.ID, yytext()); }
					case -128:
						break;
					case 136:
						{ return tok(sym.ID, yytext()); }
					case -129:
						break;
					case 137:
						{ return tok(sym.ID, yytext()); }
					case -130:
						break;
					case 138:
						{ return tok(sym.ID, yytext()); }
					case -131:
						break;
					case 139:
						{ return tok(sym.ID, yytext()); }
					case -132:
						break;
					case 140:
						{ return tok(sym.ID, yytext()); }
					case -133:
						break;
					case 141:
						{ return tok(sym.ID, yytext()); }
					case -134:
						break;
					case 142:
						{ return tok(sym.ID, yytext()); }
					case -135:
						break;
					case 143:
						{ return tok(sym.ID, yytext()); }
					case -136:
						break;
					case 144:
						{ return tok(sym.ID, yytext()); }
					case -137:
						break;
					case 145:
						{ return tok(sym.ID, yytext()); }
					case -138:
						break;
					case 146:
						{ return tok(sym.ID, yytext()); }
					case -139:
						break;
					case 147:
						{ return tok(sym.ID, yytext()); }
					case -140:
						break;
					case 148:
						{ return tok(sym.ID, yytext()); }
					case -141:
						break;
					case 149:
						{ return tok(sym.ID, yytext()); }
					case -142:
						break;
					case 150:
						{ return tok(sym.ID, yytext()); }
					case -143:
						break;
					case 151:
						{ return tok(sym.ID, yytext()); }
					case -144:
						break;
					case 152:
						{ return tok(sym.ID, yytext()); }
					case -145:
						break;
					case 153:
						{ return tok(sym.ID, yytext()); }
					case -146:
						break;
					case 154:
						{ return tok(sym.ID, yytext()); }
					case -147:
						break;
					case 155:
						{ return tok(sym.ID, yytext()); }
					case -148:
						break;
					case 156:
						{ return tok(sym.ID, yytext()); }
					case -149:
						break;
					case 157:
						{ return tok(sym.ID, yytext()); }
					case -150:
						break;
					case 158:
						{ return tok(sym.ID, yytext()); }
					case -151:
						break;
					case 159:
						{ return tok(sym.ID, yytext()); }
					case -152:
						break;
					case 160:
						{ return tok(sym.ID, yytext()); }
					case -153:
						break;
					case 161:
						{ return tok(sym.ID, yytext()); }
					case -154:
						break;
					case 162:
						{ return tok(sym.ID, yytext()); }
					case -155:
						break;
					case 163:
						{ return tok(sym.ID, yytext()); }
					case -156:
						break;
					case 164:
						{ return tok(sym.ID, yytext()); }
					case -157:
						break;
					case 165:
						{ return tok(sym.ID, yytext()); }
					case -158:
						break;
					case 166:
						{ return tok(sym.ID, yytext()); }
					case -159:
						break;
					case 167:
						{ return tok(sym.ID, yytext()); }
					case -160:
						break;
					case 168:
						{ return tok(sym.ID, yytext()); }
					case -161:
						break;
					case 169:
						{ return tok(sym.ID, yytext()); }
					case -162:
						break;
					case 170:
						{ return tok(sym.ID, yytext()); }
					case -163:
						break;
					case 171:
						{ return tok(sym.ID, yytext()); }
					case -164:
						break;
					case 172:
						{ return tok(sym.ID, yytext()); }
					case -165:
						break;
					case 173:
						{ return tok(sym.ID, yytext()); }
					case -166:
						break;
					case 174:
						{ return tok(sym.ID, yytext()); }
					case -167:
						break;
					case 175:
						{ return tok(sym.ID, yytext()); }
					case -168:
						break;
					case 176:
						{ return tok(sym.ID, yytext()); }
					case -169:
						break;
					case 177:
						{ return tok(sym.ID, yytext()); }
					case -170:
						break;
					case 178:
						{ return tok(sym.ID, yytext()); }
					case -171:
						break;
					case 179:
						{ return tok(sym.ID, yytext()); }
					case -172:
						break;
					case 180:
						{ return tok(sym.ID, yytext()); }
					case -173:
						break;
					case 181:
						{ return tok(sym.ID, yytext()); }
					case -174:
						break;
					case 182:
						{ return tok(sym.ID, yytext()); }
					case -175:
						break;
					case 183:
						{ return tok(sym.ID, yytext()); }
					case -176:
						break;
					case 184:
						{ return tok(sym.ID, yytext()); }
					case -177:
						break;
					case 185:
						{ return tok(sym.ID, yytext()); }
					case -178:
						break;
					case 186:
						{ return tok(sym.ID, yytext()); }
					case -179:
						break;
					case 187:
						{ return tok(sym.ID, yytext()); }
					case -180:
						break;
					case 188:
						{ return tok(sym.ID, yytext()); }
					case -181:
						break;
					case 189:
						{ return tok(sym.ID, yytext()); }
					case -182:
						break;
					case 190:
						{ return tok(sym.ID, yytext()); }
					case -183:
						break;
					case 191:
						{ return tok(sym.ID, yytext()); }
					case -184:
						break;
					case 192:
						{ return tok(sym.ID, yytext()); }
					case -185:
						break;
					case 193:
						{ return tok(sym.ID, yytext()); }
					case -186:
						break;
					case 194:
						{ return tok(sym.ID, yytext()); }
					case -187:
						break;
					case 195:
						{ return tok(sym.ID, yytext()); }
					case -188:
						break;
					case 196:
						{ return tok(sym.ID, yytext()); }
					case -189:
						break;
					case 197:
						{ return tok(sym.ID, yytext()); }
					case -190:
						break;
					case 198:
						{ return tok(sym.ID, yytext()); }
					case -191:
						break;
					case 199:
						{ return tok(sym.ID, yytext()); }
					case -192:
						break;
					case 200:
						{ return tok(sym.ID, yytext()); }
					case -193:
						break;
					case 201:
						{ return tok(sym.ID, yytext()); }
					case -194:
						break;
					case 202:
						{ return tok(sym.ID, yytext()); }
					case -195:
						break;
					case 203:
						{ return tok(sym.ID, yytext()); }
					case -196:
						break;
					case 204:
						{ return tok(sym.ID, yytext()); }
					case -197:
						break;
					case 205:
						{ return tok(sym.ID, yytext()); }
					case -198:
						break;
					case 206:
						{ return tok(sym.ID, yytext()); }
					case -199:
						break;
					case 207:
						{ return tok(sym.ID, yytext()); }
					case -200:
						break;
					case 208:
						{ return tok(sym.ID, yytext()); }
					case -201:
						break;
					case 209:
						{ return tok(sym.ID, yytext()); }
					case -202:
						break;
					case 210:
						{ return tok(sym.ID, yytext()); }
					case -203:
						break;
					case 211:
						{ return tok(sym.ID, yytext()); }
					case -204:
						break;
					case 212:
						{ return tok(sym.ID, yytext()); }
					case -205:
						break;
					case 213:
						{ return tok(sym.ID, yytext()); }
					case -206:
						break;
					case 214:
						{ return tok(sym.ID, yytext()); }
					case -207:
						break;
					case 215:
						{ return tok(sym.ID, yytext()); }
					case -208:
						break;
					case 216:
						{ return tok(sym.ID, yytext()); }
					case -209:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
					}
				}
			}
		}
	}
}
