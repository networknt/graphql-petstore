package com.networknt.starwars;

import com.networknt.config.Config;
import com.networknt.starwars.schema.MutationSchema;
import graphql.GraphQL;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Jackson3CompatibilityTest {
    private final ObjectMapper mapper = Config.getInstance().getMapper();

    @Test
    void applicationGraphqlQueryResultSerializesWithFrameworkMapper() {
        var result = GraphQL.newGraphQL(new MutationSchema().getSchema()).build()
            .execute("{ numberHolder { theNumber } }");
        assertTrue(result.getErrors().isEmpty(), result.getErrors().toString());
        Map<?, ?> decoded = mapper.readValue(mapper.writeValueAsBytes(result.toSpecification()), Map.class);
        assertEquals(Map.of("data", Map.of("numberHolder", Map.of("theNumber", 6))), decoded);
    }

    @Test
    void graphqlErrorsKeepTheirStructuredShape() {
        var result = GraphQL.newGraphQL(new MutationSchema().getSchema()).build().execute("{ missing }");
        var json = mapper.readTree(mapper.writeValueAsBytes(result.toSpecification()));
        assertTrue(json.path("errors").isArray());
        assertTrue(json.path("errors").get(0).path("message").isString());
    }

    @Test
    void runtimeDoesNotContainJackson2Databind() {
        assertThrows(ClassNotFoundException.class, () -> Class.forName("com.fasterxml.jackson.databind.ObjectMapper"));
    }
}
