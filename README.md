# spring-webflux-multiple-auth-mechanisms

**This is currently not working code used as an example to illustrate the issue to others.**

## Goal of this project:
Im trying to configure the security for a spring webflux/reactor based application. The application should support multiple (currently two) authentication methods (lets call them A and B. Both consume the credentials via the _X-Application-Authentication_ header) which are protecting different resources (for simplicity also named A and B) the application will expose.

I am desiring the following security aspects:
- Resource A should be protected by authentication method A
- Resource B should be protected by authentication method B
- All other resources require authentication or should at least be inacessible for that matter

I have added [tests](./src/test/kotlin/localhost/playground/multi/auth/ApplicationTests.kt) that verify this behaviour. Some of the tests are passing, some of them are failing and I assume it is due to a misconfiguration (e.g. not properly registering filters.)

Take a look at the [security config](./src/main/kotlin/localhost/playground/multi/auth/SecurityConfig.kt) to see with what I have come up so far.
 
 The idea im following here is that for every authentication method I register a _AuthenticationWebFilter_ that would ideally only be processed on the resources it it supposed to be protecting (if thats even possible). 
  
