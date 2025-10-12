package Parse;
import ErrorMsg.ErrorMsg;
import java_cup.runtime.Symbol;

%%
%implements Lexer
%cup
%function nextToken
%type java_cup.runtime.Symbol
%char
%public

%{
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
%}

%eofval{
return tok(sym.EOF, null);
%eofval}

DIGIT = [0-9]
DIGITS = {DIGIT}+
HEXDIGIT = [0-9a-fA-F]
OCTALDIGIT = [0-7]
HEX_LITERAL = 0[xX]{HEXDIGIT}+
OCTAL_LITERAL = 0{OCTALDIGIT}+
LETTER = [a-zA-Z_]
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NEWLINE = \n

%%

<YYINITIAL>"\t"      { /* ignore tab */ }
<YYINITIAL>"\r"      { /* ignore carriage return */ }
<YYINITIAL>"\f"      { /* ignore form feed */ }
<YYINITIAL>" "     { /* ignore space */ }
<YYINITIAL>{NEWLINE}                   { newline(); }
<YYINITIAL>"//".*                      { /* ignore */ }
<YYINITIAL>"/*"([^*]|\*+[^*/])*\*+"/"  { /* ignore */ }

<YYINITIAL>"auto"          { return tok(sym.AUTO); }
<YYINITIAL>"break"         { return tok(sym.BREAK); }
<YYINITIAL>"case"          { return tok(sym.CASE); }
<YYINITIAL>"char"          { return tok(sym.CHAR); }
<YYINITIAL>"const"         { return tok(sym.CONST); }
<YYINITIAL>"continue"      { return tok(sym.CONTINUE); }
<YYINITIAL>"default"       { return tok(sym.DEFAULT); }
<YYINITIAL>"do"            { return tok(sym.DO); }
<YYINITIAL>"double"        { return tok(sym.DOUBLE); }
<YYINITIAL>"else"          { return tok(sym.ELSE); }
<YYINITIAL>"enum"          { return tok(sym.ENUM); }
<YYINITIAL>"extern"        { return tok(sym.EXTERN); }
<YYINITIAL>"float"         { return tok(sym.FLOAT); }
<YYINITIAL>"for"           { return tok(sym.FOR); }
<YYINITIAL>"goto"          { return tok(sym.GOTO); }
<YYINITIAL>"if"            { return tok(sym.IF); }
<YYINITIAL>"int"           { return tok(sym.INT); }
<YYINITIAL>"long"          { return tok(sym.LONG); }
<YYINITIAL>"register"      { return tok(sym.REGISTER); }
<YYINITIAL>"return"        { return tok(sym.RETURN); }
<YYINITIAL>"short"         { return tok(sym.SHORT); }
<YYINITIAL>"signed"        { return tok(sym.SIGNED); }
<YYINITIAL>"sizeof"        { return tok(sym.SIZEOF); }
<YYINITIAL>"static"        { return tok(sym.STATIC); }
<YYINITIAL>"struct"        { return tok(sym.STRUCT); }
<YYINITIAL>"switch"        { return tok(sym.SWITCH); }
<YYINITIAL>"typedef"       { return tok(sym.TYPEDEF); }
<YYINITIAL>"union"         { return tok(sym.UNION); }
<YYINITIAL>"unsigned"      { return tok(sym.UNSIGNED); }
<YYINITIAL>"void"          { return tok(sym.VOID); }
<YYINITIAL>"volatile"      { return tok(sym.VOLATILE); }
<YYINITIAL>"while"         { return tok(sym.WHILE); }
<YYINITIAL>"var"           { return tok(sym.VAR); }
<YYINITIAL>"fun"           { return tok(sym.FUN); }

<YYINITIAL>{IDENTIFIER}    { return tok(sym.ID, yytext()); }
<YYINITIAL>{DIGITS}        { return tok(sym.DECIMAL_LITERAL, Integer.valueOf(yytext())); }
<YYINITIAL>{HEX_LITERAL}   { 
    String hexStr = yytext().substring(2); // Remove "0x" or "0X" prefix
    return tok(sym.HEX_LITERAL, Integer.valueOf(hexStr, 16)); 
}
<YYINITIAL>{OCTAL_LITERAL} { 
    String octalStr = yytext().substring(1); // Remove "0" prefix
    return tok(sym.OCTAL_LITERAL, Integer.valueOf(octalStr, 8)); 
}

<YYINITIAL>\'([^\\']|\\[ntr\\'\"])\' {
    String content = yytext().substring(1, yytext().length() - 1);
    String processed = processEscapeSequences(content);
    return tok(sym.CHAR_LITERAL, processed);
}

<YYINITIAL>\"([^\\\"]|\\[ntr\\'\"])*\" {
    String content = yytext().substring(1, yytext().length() - 1);
    String processed = processEscapeSequences(content);
    return tok(sym.STRING_LITERAL, processed);
}

<YYINITIAL>"=="            { return tok(sym.EQ); }
<YYINITIAL>"!="            { return tok(sym.NEQ); }
<YYINITIAL>"<="            { return tok(sym.LE); }
<YYINITIAL>">="            { return tok(sym.GE); }
<YYINITIAL>"&&"            { return tok(sym.AND); }
<YYINITIAL>"||"            { return tok(sym.OR); }
<YYINITIAL>"++"            { return tok(sym.INCREMENT); }
<YYINITIAL>"--"            { return tok(sym.DECREMENT); }
<YYINITIAL>"+="            { return tok(sym.ADDASSIGN); }
<YYINITIAL>"-="            { return tok(sym.SUBASSIGN); }
<YYINITIAL>"*="            { return tok(sym.MULASSIGN); }
<YYINITIAL>"/="            { return tok(sym.DIVASSIGN); }
<YYINITIAL>"%="            { return tok(sym.MODASSIGN); }
<YYINITIAL>"<<"            { return tok(sym.LSHIFT); }
<YYINITIAL>">>"            { return tok(sym.RSHIFT); }
<YYINITIAL>"&="            { return tok(sym.BWISEANDASSIGN); }
<YYINITIAL>"|="            { return tok(sym.BWISEORASSIGN); }
<YYINITIAL>"^="            { return tok(sym.BWISEXORASSIGN); }
<YYINITIAL>"<<="           { return tok(sym.LSHIFTASSIGN); }
<YYINITIAL>">>="           { return tok(sym.RSHIFTASSIGN); }
<YYINITIAL>"->"            { return tok(sym.ARROW); }
<YYINITIAL>"..."           { return tok(sym.ELIPSES); }

<YYINITIAL>"+"             { return tok(sym.PLUS); }
<YYINITIAL>"-"             { return tok(sym.MINUS); }
<YYINITIAL>"*"             { return tok(sym.TIMES); }
<YYINITIAL>"/"             { return tok(sym.DIVIDE); }
<YYINITIAL>"%"             { return tok(sym.MODULUS); }
<YYINITIAL>"="             { return tok(sym.ASSIGN); }
<YYINITIAL>"<"             { return tok(sym.LT); }
<YYINITIAL>">"             { return tok(sym.GT); }
<YYINITIAL>"!"             { return tok(sym.NOT); }
<YYINITIAL>"&"             { return tok(sym.BITWISEAND); }
<YYINITIAL>"|"             { return tok(sym.BWISEOR); }
<YYINITIAL>"^"             { return tok(sym.BWISEXOR); }
<YYINITIAL>"~"             { return tok(sym.TILDE); }
<YYINITIAL>"("             { return tok(sym.LPAREN); }
<YYINITIAL>")"             { return tok(sym.RPAREN); }
<YYINITIAL>"["             { return tok(sym.LBRACK); }
<YYINITIAL>"]"             { return tok(sym.RBRACK); }
<YYINITIAL>"{"             { return tok(sym.LBRACE); }
<YYINITIAL>"}"             { return tok(sym.RBRACE); }
<YYINITIAL>";"             { return tok(sym.SEMICOLON); }
<YYINITIAL>","             { return tok(sym.COMMA); }
<YYINITIAL>"."             { return tok(sym.PERIOD); }
<YYINITIAL>":"             { return tok(sym.COLON); }
<YYINITIAL>"?"             { return tok(sym.QUESTION); }

<YYINITIAL>.               { err("Illegal character: '" + yytext() + "'"); }
