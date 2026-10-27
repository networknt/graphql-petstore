# graphql-petstore
GraphQL petstore example for testing


## Jackson 3 source build

This application uses Jackson 3.2.3 and the framework's immutable mapper.
Jackson annotation imports remain `com.fasterxml.jackson.annotation` by design.
The application has no direct classic SnakeYAML parser to replace; its packaged
runtime includes SnakeYAML Engine through the Jackson 3/framework stack.
Classic SnakeYAML may still be supplied by upstream compatibility dependencies;
this change does not claim to remove every transitive classic library.

Run `mvn clean verify` against a compatible source-built framework stack. A clean
build matters because the shade plugin replaces the main JAR. The migration tests
exercise application binding/results and reject Jackson 2 databind on the runtime
classpath. CI additionally checks the packaged JAR and applies the strict artifact
audit, allowing the shared Jackson annotations artifact.

`.github/workflows/jackson3-source-build.yml` builds the SHA-pinned prerequisites
in `ci/jackson3-source-refs.json` in an isolated Maven repository and retains test,
source and dependency-audit evidence. These pins describe an intentionally qualified
stack, not the current heads of every upstream repository. Update them as a reviewed
batch when adopting a new baseline. This application migration does not move the
qualified core/policy tuple or claim deployment/native qualification.
