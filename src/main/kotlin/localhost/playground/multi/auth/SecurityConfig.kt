package localhost.playground.multi.auth

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun getReactiveAuthenticationManager(): ReactiveAuthenticationManager {
        return ReactiveAuthenticationManager { authentication ->
            // simply return the authentication assuming the authentication was already verified in the converter
            Mono.justOrEmpty(authentication)
        }
    }

    companion object {
        const val ROLE_ADMIN_RESOURCE_A = "ADMIN_RESSOURCE_A"
        const val ROLE_ADMIN_RESOURCE_B = "ADMIN_RESSOURCE_B"
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity,
                                  @Qualifier("authFilterResourceA")
                                  authFilterResourceA: AuthenticationWebFilter,
                                  @Qualifier("authFilterResourceB")
                                  authFilterResourceB: AuthenticationWebFilter): SecurityWebFilterChain {

        //configure security for resource a
        http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/ressourceA/**"))
                .addFilterAt(authFilterResourceA, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers("/resourceA/**")
                .hasRole(ROLE_ADMIN_RESOURCE_A)

        //configure security for resource b
        http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/ressourceB/**"))
                .addFilterAt(authFilterResourceB, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers("/resourceB/**")
                .hasRole(ROLE_ADMIN_RESOURCE_B)

        // global config
        http
                .httpBasic()
                .disable()
                .formLogin()
                .disable()
                .csrf()
                .disable()
                .cors()
                .disable()
                .authorizeExchange()
                .anyExchange()
                .authenticated()

        return http.build()
    }

    @Bean("authFilterResourceA")
    fun authFilterResourceA(authManager: ReactiveAuthenticationManager): AuthenticationWebFilter {

        val filter = AuthenticationWebFilter(authManager)

        filter.setServerAuthenticationConverter {

            // simplified dummy token conversion for keeping the example as simple as possible

            Mono.justOrEmpty(it)
                    .map { it.request.headers.getFirst("X-Application-Authentication") ?: "" }
                    .filter { it == "Bearer tokenForA" }
                    .map {
                        UsernamePasswordAuthenticationToken(
                                "userWithAccessRightsToA",
                                it,
                                listOf(SimpleGrantedAuthority(ROLE_ADMIN_RESOURCE_A))
                        )
                    }
        }

        return filter
    }

    @Bean("authFilterResourceB")
    fun authFilterResourceB(authManager: ReactiveAuthenticationManager): AuthenticationWebFilter {

        val filter = AuthenticationWebFilter(authManager)

        filter.setServerAuthenticationConverter {

            // simplified dummy token conversion for keeping the example as simple as possible

            Mono.justOrEmpty(it)
                    .map { it.request.headers.getFirst("X-Application-Authentication") ?: "" }
                    .filter { it == "Bearer tokenForB" }
                    .map {
                        UsernamePasswordAuthenticationToken(
                                "userWithAccessRightsToB",
                                it,
                                listOf(SimpleGrantedAuthority(ROLE_ADMIN_RESOURCE_B))
                        )
                    }
        }

        return filter
    }

}
