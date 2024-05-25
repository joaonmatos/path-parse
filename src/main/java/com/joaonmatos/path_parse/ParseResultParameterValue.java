package com.joaonmatos.path_parse;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * The value of a named parameter in a parse result.
 */
final public class ParseResultParameterValue {
    final private String name;
    final private String value;
    final private int startsAt;
    final private int endsAt;

    ParseResultParameterValue(String name, String value, int startsAt, int endsAt) {
        this.name = name;
        this.value = value;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }


    /**
     * The name of the paramater corresponding to this value.
     *
     * @return Name of named parameter (without ':')
     */
    public String name() {
        return name;
    }

    /**
     * The value of this parameter. Can be a blank string if the option `allowEmptyParameterValues` is set to true.
     *
     * @return value
     */
    public String value() {
        return value;
    }

    /**
     * Position in the input string where this value starts.
     *
     * @return 0-based index
     */
    public int startsAt() {
        return startsAt;
    }

    /**
     * Position inthe input string where this value ends.
     *
     * @return 0-based index to the character that follows after the value ends
     */
    public int endsAt() {
        return endsAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParseResultParameterValue)) return false;
        ParseResultParameterValue that = (ParseResultParameterValue) o;
        return startsAt == that.startsAt && endsAt == that.endsAt && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, startsAt, endsAt);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ParseResultParameterValue.class.getSimpleName() + "[", "]")
                .add("value='" + value + "'")
                .add("startsAt=" + startsAt)
                .add("endsAt=" + endsAt)
                .toString();
    }
}
