package com.joaonmatos.path_parse;

import java.util.List;
import java.util.function.Consumer;

/**
 * Parser that is able to match against arbitrary input strings and determine if they match against the known path.
 */
public interface PathParser {
    /**
     * Create a PathParser with the default configuration.
     *
     * @param matchingPath the path against which to match inputs
     * @return PathParser
     * @throws IllegalArgumentException when the matching path is not correct
     */
    static PathParser create(String matchingPath) {
        return create(matchingPath, PathParserOptions.getDefault());
    }

    /**
     * Create a PathParser, using a closure to configure it.
     *
     * @param matchingPath the path against which to match inputs
     * @param buildOptions a closure consuming the options builder class, which is then used to inject the wanted configuration
     * @return PathParser
     * @throws IllegalArgumentException when the matching path is not correct
     */
    static PathParser create(String matchingPath, Consumer<PathParserOptions.Builder> buildOptions) {
        var builder = PathParserOptions.builder();
        buildOptions.accept(builder);
        return create(matchingPath, builder.build());
    }

    /**
     * Create a PathParser, passing in a customized configuration.
     *
     * @param matchingPath the path against which to match inputs
     * @param options      configuration
     * @return PathParser
     * @throws IllegalArgumentException when the matching path is not correct
     */
    static PathParser create(String matchingPath, PathParserOptions options) {
        return PathParserFactory.createParser(matchingPath, options);
    }

    /**
     * The path against which this parser is matching inputs.
     *
     * @return original path
     */
    String matchingPath();

    /**
     * The length of the literal string appearing before the first named parameter.
     *
     * @return number of characters before first named parameter
     */
    int prefixLength();

    /**
     * The list of keys that appear in the matching path, that will parse into named parameters
     *
     * @return list of keys (without the ':' preceding it)
     */
    List<String> namedParameters();

    /**
     * Test the input against this parser's path and see if it matches.
     *
     * @param input unkown path we want to test against our matching path
     * @return ParseResult if there's a match, null otherwise
     */
    ParseResult parse(String input);
}
