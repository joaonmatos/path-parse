package com.joaonmatos.path_parse;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Options controlling the parsers sensitivity and delimiters.
 */
final public class PathParserOptions {
    final private static boolean DEFAULT_CASE_SENSITIVE = false;
    final private static boolean DEFAULT_MATCH_TRAILING_DELIMITER = true;
    final private static boolean DEFAULT_ALLOW_EMPTY_PARAMETER_VALUES = false;
    final private static boolean DEFAULT_COLLAPSE_EMPTY_PATH_SEGMENTS = false;

    final private boolean caseSensitive;
    final private boolean matchTrailingDelimiter;
    final private boolean allowEmptyParameterValues;
    final private boolean collapseEmptyPathSegments;

    /**
     * Create a new PathParserOptions object
     *
     * @param caseSensitive             whether the parser should ignore case or not. Default: false
     * @param matchTrailingDelimiter    should the parser succeed even if the path has a trailing delimiter and the input not, or vice-versa. Default: true
     * @param allowEmptyParameterValues should the parser accept an empty input path segment where there is a named parameter. Default: false
     * @param collapseEmptyPathSegments when this option is true, the parser will be insensitive to multiple delimiters in a row. Default: false
     */
    public PathParserOptions(boolean caseSensitive, boolean matchTrailingDelimiter, boolean allowEmptyParameterValues, boolean collapseEmptyPathSegments) {
        this.caseSensitive = caseSensitive;
        this.matchTrailingDelimiter = matchTrailingDelimiter;
        this.allowEmptyParameterValues = allowEmptyParameterValues;
        this.collapseEmptyPathSegments = collapseEmptyPathSegments;
    }

    /**
     * Preconfigured default options.
     *
     * @return caseSensitive=false, matchTrailingDelimiter=true, defaultDelimiter='/'
     */
    public static PathParserOptions getDefault() {
        return new PathParserOptions(
                DEFAULT_CASE_SENSITIVE,
                DEFAULT_MATCH_TRAILING_DELIMITER,
                DEFAULT_ALLOW_EMPTY_PARAMETER_VALUES,
                DEFAULT_COLLAPSE_EMPTY_PATH_SEGMENTS
        );
    }

    /**
     * Create a builder to construct this object fluently.
     *
     * @return a Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Whether the parser should ignore case or not. Default: false.
     *
     * @return value of field
     */
    public boolean caseSensitive() {
        return caseSensitive;
    }

    /**
     * Should the parser succeed even if the path has a trailing delimiter and the input not, or vice-versa. Default: true.
     *
     * @return value of field
     */
    public boolean matchTrailingDelimiter() {
        return matchTrailingDelimiter;
    }

    /**
     * Should the parser accept an empty input path segment where there is a named parameter. Default: false
     *
     * @return value of field
     */
    public boolean allowEmptyParameterValues() {
        return allowEmptyParameterValues;
    }

    /**
     * When this option is true, the parser will be insensitive to multiple delimiters in a row. This is not standard URI, since in URIs path segments can be empty, but in many cases paths get like that accidentally, e.g. from joining paths carelessly.
     * Default: false
     *
     * @return the value
     */
    public boolean collapseEmptyPathSegments() {
        return collapseEmptyPathSegments;
    }

    /**
     * Creates a builder preset to the instance's options.
     *
     * @return builder
     */
    public Builder toBuilder() {
        return new Builder()
                .caseSensitive(caseSensitive)
                .matchTrailingDelimiter(matchTrailingDelimiter)
                .allowEmptyParameterValues(allowEmptyParameterValues)
                .collapseEmptyPathSegments(collapseEmptyPathSegments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathParserOptions)) return false;
        PathParserOptions that = (PathParserOptions) o;
        return caseSensitive == that.caseSensitive && matchTrailingDelimiter == that.matchTrailingDelimiter && allowEmptyParameterValues == that.allowEmptyParameterValues && collapseEmptyPathSegments == that.collapseEmptyPathSegments;
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseSensitive, matchTrailingDelimiter, allowEmptyParameterValues);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PathParserOptions.class.getSimpleName() + "[", "]").add("caseSensitive=" + caseSensitive).add("matchTrailingDelimiter=" + matchTrailingDelimiter).add("allowEmptyParameterValues=" + allowEmptyParameterValues).add("collapseEmptyPathSegments=" + collapseEmptyPathSegments).toString();
    }

    public static class Builder {
        private boolean caseSensitive = DEFAULT_CASE_SENSITIVE;
        private boolean matchTrailingDelimiter = DEFAULT_MATCH_TRAILING_DELIMITER;
        private boolean allowEmptyParameterValues = DEFAULT_ALLOW_EMPTY_PARAMETER_VALUES;
        private boolean collapseEmptyPathSegments = DEFAULT_COLLAPSE_EMPTY_PATH_SEGMENTS;

        private Builder() {
        }

        /**
         * Whether the parser should ignore case or not. Default: false.
         *
         * @return value of field
         */
        public boolean caseSensitive() {
            return caseSensitive;
        }

        /**
         * Sets whether the parser should ignore case or not.
         *
         * @param caseSensitive new value
         * @return same Builder instance
         */
        public Builder caseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        /**
         * Should the parser succeed even if the path has a trailing delimiter and the input not, or vice-versa. Default: true.
         *
         * @return value of field
         */
        public boolean matchTrailingDelimiter() {
            return matchTrailingDelimiter;
        }

        /**
         * Sets if the parser should succeed even if the path has a trailing delimiter and the input not, or vice-versa.
         *
         * @param matchTrailingDelimiter new value
         * @return same Builder instance
         */
        public Builder matchTrailingDelimiter(boolean matchTrailingDelimiter) {
            this.matchTrailingDelimiter = matchTrailingDelimiter;
            return this;
        }

        /**
         * Should the parser accept an empty input path segment where there is a named parameter. Default: false.
         *
         * @return the value
         */
        public boolean allowEmptyParameterValues() {
            return allowEmptyParameterValues;
        }

        /**
         * Sets if the parser should accept an empty input path segment where there is a named parameter.
         *
         * @param allowEmptyParameterValues new value
         * @return same Builder instance
         */
        public Builder allowEmptyParameterValues(boolean allowEmptyParameterValues) {
            this.allowEmptyParameterValues = allowEmptyParameterValues;
            return this;
        }

        /**
         * When this option is true, the parser will be insensitive to multiple delimiters in a row. This is not standard URI, since in URIs path segments can be empty, but in many cases paths get like that accidentally, e.g. from joining paths carelessly.
         * Default: false
         *
         * @return the value
         */
        public boolean collapseEmptyPathSegments() {
            return collapseEmptyPathSegments;
        }

        /**
         * When this option is true, the parser will be insensitive to multiple delimiters in a row. This is not standard URI, since in URIs path segments can be empty, but in many cases paths get like that accidentally, e.g. from joining paths carelessly.
         *
         * @param collapseEmptyPathSegments new value
         * @return same Builder instance
         */
        public Builder collapseEmptyPathSegments(boolean collapseEmptyPathSegments) {
            this.collapseEmptyPathSegments = collapseEmptyPathSegments;
            return this;
        }

        /**
         * Build the PathParserOptions object.
         *
         * @return the built configuration
         */
        public PathParserOptions build() {
            return new PathParserOptions(caseSensitive, matchTrailingDelimiter, allowEmptyParameterValues, collapseEmptyPathSegments);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Builder.class.getSimpleName() + "[", "]").add("caseSensitive=" + caseSensitive).add("matchTrailingDelimiter=" + matchTrailingDelimiter).add("allowEmptyParameterValues=" + allowEmptyParameterValues).add("collapseEmptyPathSegments=" + collapseEmptyPathSegments).toString();
        }
    }
}
