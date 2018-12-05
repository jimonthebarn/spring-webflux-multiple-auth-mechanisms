package localhost.playground.multi.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@AutoConfigureWebTestClient
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    lateinit var webClient : WebTestClient

    @Test
    fun contextLoads() {
    }

    @Test
    fun `test successful access to resource A with token for A`() {
        webClient.get().uri("/resourceA/")
                .header("X-Application-Authentication", "Bearer tokenForA")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith {
                    assertThat(String(it.responseBody!!)).isEqualTo("Accessed resource A")
                }
    }


    @Test
    fun `test unauthorized access to resource A with token for B`() {
        webClient.get().uri("/resourceA/")
                .header("X-Application-Authentication", "Bearer tokenForB")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isForbidden
    }

    @Test
    fun `test unauthorized access to resource A without a token`() {
        webClient.get().uri("/resourceA/")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `test successful access to resource B with token for B`() {
        webClient.get().uri("/resourceB/")
                .header("X-Application-Authentication", "Bearer tokenForB")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith {
                    assertThat(String(it.responseBody!!)).isEqualTo("Accessed resource B")
                }
    }

    @Test
    fun `test unauthorized access to resource B with token for A`() {
        webClient.get().uri("/resourceB/")
                .header("X-Application-Authentication", "Bearer tokenForA")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isForbidden
    }

    @Test
    fun `test unauthorized access to resource B without a token`() {
        webClient.get().uri("/resourceB/")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isUnauthorized
    }
}
