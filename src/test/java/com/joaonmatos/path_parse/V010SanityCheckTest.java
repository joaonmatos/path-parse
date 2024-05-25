package com.joaonmatos.path_parse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class V010SanityCheckTest {
    @Test
    void onlyOneTest() {
        var pathSpec = "/users/:userid/blog-posts";

        var testPath = "/users/joao/blog-posts";

        var expectedParameterValue = new ParseResultParameterValue("userid", "joao", 7, 11);

        var parser = PathParser.create(pathSpec);
        var parseResult = parser.parse(testPath);

        assertNotNull(parseResult);
        assertEquals(testPath, parseResult.input());
        assertSame(parser, parseResult.matchingParser());
        assertEquals(expectedParameterValue, parseResult.parameterValue("userid"));
        assertEquals(
                Map.of("userid", expectedParameterValue),
                parseResult.parameterValues()
        );
    }
}
