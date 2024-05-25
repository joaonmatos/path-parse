package com.joaonmatos.path_parse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class PathParserFactory {
    private static final Set<Character> RESERVED_CHARACTERS = Set.of('?', '#', '[', ']', '@', '!', '{', '}', '+', '*');

    private static final Set<Character> HEX_CHARACTERS = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F');

    private static final Pattern REGEX_SPECIAL_CHARACTERS = Pattern.compile("([.+*?=^!:${}()\\[\\]|/\\\\])");

    /**
     * Parse the path and options and create a correctly configured implementation of PathParser
     *
     * @param matchingPath this path will match against inputs
     * @param options      use this to configure aspects of the parser's behaviour
     * @return configured PathParser
     * @throws IllegalArgumentException when the matching path is not correct
     */
    static PathParser createParser(String matchingPath, PathParserOptions options) {
        if (options == null) {
            options = PathParserOptions.getDefault();
        }
        if (matchingPath == null || matchingPath.isBlank()) {
            throw new IllegalArgumentException("Can't build PathParser: null or blank matchingPath");
        }
        matchingPath = matchingPath.trim();

        var state = LexerStates.START;
        var patternBuilder = new StringBuilder("^");
        var localBuilder = new StringBuilder();
        var paramNames = new HashSet<String>();
        for (int i = 0; i < matchingPath.length(); i++) {
            var c = matchingPath.charAt(i);
            if (RESERVED_CHARACTERS.contains(c)) {
                throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - character " + c + " is reserved by the URI standard or by the developer.");
            }
            var escapedSequence = escapeCharForRegex(c);
            switch (state) {
                case START: {
                    if (c != '/') {
                        throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - expected character '/' but got " + c);
                    }
                    patternBuilder.append(escapedSequence);
                    state = LexerStates.DELIMITER;
                    break;
                }
                case DELIMITER: {
                    if (c == '/') {
                        patternBuilder.append(escapedSequence);
                        if (options.collapseEmptyPathSegments()) {
                            patternBuilder.append('?');
                        }
                    } else if (c == ':') {
                        localBuilder = new StringBuilder();
                        state = LexerStates.COLON;
                    } else if (c == '%') {
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.PERCENT;
                    } else {
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.LITERAL;
                    }
                    break;
                }
                case LITERAL: {
                    if (c == '/') {
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.DELIMITER;
                    } else if (c == ':') {
                        localBuilder = new StringBuilder();
                        state = LexerStates.COLON;
                    } else if (c == '%') {
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.PERCENT;
                    } else {
                        patternBuilder.append(escapedSequence);
                    }
                    break;
                }
                case COLON: {
                    if (Character.isLetterOrDigit(c)) {
                        localBuilder.append(c);
                        state = LexerStates.ALPHANUM;
                    } else {
                        throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - expected a character in 0-9,a-z,A-z but got" + c);
                    }
                    break;
                }
                case ALPHANUM: {
                    if (Character.isLetterOrDigit(c)) {
                        localBuilder.append(c);
                    } else if (c == '/') {
                        var paramName = localBuilder.toString();
                        if (paramNames.contains(paramName)) {
                            throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - the parameter name " + paramName + " has already been used in the path");
                        }
                        paramNames.add(paramName);
                        patternBuilder.append(buildNamedCaptureGroup(paramName, options.allowEmptyParameterValues()));
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.DELIMITER;
                    } else if (c == ':') {
                        var paramName = localBuilder.toString();
                        if (paramNames.contains(paramName)) {
                            throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - the parameter name " + paramName + " has already been used in the path");
                        }
                        paramNames.add(paramName);
                        patternBuilder.append(buildNamedCaptureGroup(paramName, options.allowEmptyParameterValues()));
                        localBuilder = new StringBuilder();
                        state = LexerStates.COLON;
                    } else {
                        throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " expected a character in 0-9,a-z,A-z,'/',':' but got" + c);
                    }
                    break;
                }
                case PERCENT: {
                    if (!HEX_CHARACTERS.contains(c)) {
                        throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - the first character after a '%' must be a hexadecimal digit but is " + c);
                    }
                    patternBuilder.append(c);
                    state = LexerStates.PERCENT_N1;
                    break;
                }
                case PERCENT_N1: {
                    if (!HEX_CHARACTERS.contains(c)) {
                        throw new IllegalArgumentException("Can't build PathParser: invalid input at position " + i + " - the second character after a '%' must be a hexadecimal digit but is " + c);
                    }
                    patternBuilder.append(c);
                    state = LexerStates.PERCENT_N2;
                    break;
                }
                case PERCENT_N2: {
                    if (c == '/') {
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.DELIMITER;
                    } else if (c == ':') {
                        localBuilder = new StringBuilder();
                        state = LexerStates.COLON;
                    } else if (c == '%') {
                        patternBuilder.append(c);
                        state = LexerStates.PERCENT;
                    } else {
                        patternBuilder.append(escapedSequence);
                        state = LexerStates.LITERAL;
                    }
                    break;
                }
            }
        }
        if (state == LexerStates.COLON || state == LexerStates.PERCENT) {
            throw new IllegalArgumentException("Can't build PathParser: invalid input at the end of the path. The string cannot end with ':' or '%'");
        }
        if (state == LexerStates.ALPHANUM) {
            var paramName = localBuilder.toString();
            if (paramNames.contains(paramName)) {
                throw new IllegalArgumentException("Can't build PathParser: invalid input at end of path - the parameter name " + paramName + " has already been used in the path");
            }
            paramNames.add(paramName);
            patternBuilder.append(buildNamedCaptureGroup(paramName, options.allowEmptyParameterValues()));
        }
        if (options.matchTrailingDelimiter()) {
            if (patternBuilder.toString().endsWith("/")) {
                patternBuilder.append('?');
            } else {
                patternBuilder.append("\\/?");
            }
        }

        patternBuilder.append('$');

        return new PathParserImpl(matchingPath, Pattern.compile(patternBuilder.toString(), options.caseSensitive() ? 0 : Pattern.CASE_INSENSITIVE), List.copyOf(paramNames));
    }

    private static String escapeCharForRegex(char c) {
        var s = Character.toString(c);
        var matcher = REGEX_SPECIAL_CHARACTERS.matcher(s);
        if (matcher.matches()) {
            return "\\" + c;
        } else {
            return s;
        }
    }

    private static String buildNamedCaptureGroup(String name, boolean allowEmptyParameterValues) {
        var quantifier = allowEmptyParameterValues ? '*' : '+';
        var disallowedCharacterClass = "[^\\/#\\?]";
        return "(?<" + name + ">" + disallowedCharacterClass + quantifier + ")";
    }

    private enum LexerStates {
        START, DELIMITER, LITERAL, COLON, ALPHANUM, PERCENT, PERCENT_N1, PERCENT_N2,
    }
}
