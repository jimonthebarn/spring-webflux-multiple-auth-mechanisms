# spring-webflux-multiple-auth-mechanisms

## Goal of this project:
Illustrate how to configure security in a project reactor based spring application with multiple custom authentication methods. _Disclaimer: The configuration does not claim to be complete with regards to production readiness._ 

The application should support multiple (currently two) authentication methods (lets call them A and B. Both consume the credentials via the _X-Application-Authentication_ header) which are protecting different resources (for simplicity also named A and B) the application will expose.

Desired security aspects:
- Resource A should be protected by authentication method A
- Resource B should be protected by authentication method B
- All other resources require authentication or should at least be inaccessible for that matter

The [tests](./src/test/kotlin/localhost/playground/multi/auth/ApplicationTests.kt) verify the intended behaviour.

Take a look at the [security config](./src/main/kotlin/localhost/playground/multi/auth/SecurityConfig.kt) for details.
