package com.joaonmatos.path_parse;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

final class PathParserImpl implements PathParser {
    final private String matchingPath;
    final private Pattern regex;
    final private List<String> namedParameters;
    final private int prefixLength;

    PathParserImpl(String matchingPath, Pattern regex, List<String> namedParameters) {
        this.matchingPath = matchingPath;
        this.regex = regex;
        this.namedParameters = namedParameters;
        var firstColonInPath = matchingPath.indexOf(":");
        this.prefixLength = firstColonInPath == -1
                ? matchingPath.length()
                : firstColonInPath;
    }

    @Override
    public String matchingPath() {
        return matchingPath;
    }

    @Override
    public int prefixLength() {
        return prefixLength;
    }

    @Override
    public List<String> namedParameters() {
        return namedParameters;
    }

    @Override
    public ParseResult parse(String input) {
        var matcher = regex.matcher(input);
        if (!matcher.matches()) {
            return null;
        }
        var parameterValues = new HashMap<String, ParseResultParameterValue>();
        for (var param : namedParameters) {
            var value = matcher.group(param);
            var start = matcher.start(param);
            var end = matcher.end(param);
            var paramValue = new ParseResultParameterValue(
                    param,
                    value,
                    start,
                    end
            );
            parameterValues.put(param, paramValue);
        }
        return new ParseResult(
                input,
                parameterValues,
                this
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PathParserImpl.class.getSimpleName() + "[", "]")
                .add("matchingPath='" + matchingPath + "'")
                .add("regex=" + regex)
                .add("namedParameters=" + namedParameters)
                .add("prefixLength=" + prefixLength)
                .toString();
    }
}
