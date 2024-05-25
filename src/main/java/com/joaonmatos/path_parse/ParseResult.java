package com.joaonmatos.path_parse;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * The result of successfully matching an input against a PathParser.
 */
final public class ParseResult {
    final private String input;
    final private Map<String, ParseResultParameterValue> parameterValues;
    final private PathParser matchingParser;

    ParseResult(
            String input,
            Map<String, ParseResultParameterValue> parameterValues,
            PathParser matchingParser
    ) {
        this.input = input;
        this.parameterValues = Collections.unmodifiableMap(parameterValues);
        this.matchingParser = matchingParser;
    }

    /**
     * The string that was input to the parse method.
     *
     * @return input string
     */
    public String input() {
        return input;
    }

    /**
     * The parser that executed this match.
     *
     * @return the respective PathParser
     */
    public PathParser matchingParser() {
        return matchingParser;
    }

    /**
     * The value of a parameter, if the parameterName exists in the matching path.
     *
     * @param parameterName name of the parameter (without ':')
     * @return the corresponding parameter value or null, if the name does not exist in this parser.
     */
    public ParseResultParameterValue parameterValue(String parameterName) {
        return parameterValues.get(parameterName);
    }

    /**
     * Unmodifiable map view of the captured parameter values.
     *
     * @return map of name-value pairs
     */
    public Map<String, ParseResultParameterValue> parameterValues() {
        return parameterValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParseResult)) return false;
        ParseResult that = (ParseResult) o;
        return Objects.equals(input, that.input) && Objects.equals(parameterValues, that.parameterValues) && Objects.equals(matchingParser, that.matchingParser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, parameterValues, matchingParser);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ParseResult.class.getSimpleName() + "[", "]")
                .add("input='" + input + "'")
                .add("parameterValues=" + parameterValues)
                .add("matchingParser=" + matchingParser)
                .toString();
    }
}
