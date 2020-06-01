package ebay.sales.statistics;

import ebay.sales.statistics.rest.model.SalesInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest()
class SalesStatisticsApplicationTests {


    @Test
    void contextLoads() {
    }

    @Test
    void testEndpoints() {
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080")
                .build();
        webTestClient
                .post()
                .uri("/sales")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isAccepted();

        webTestClient
                .get()
                .uri("/statistics")
                .exchange()
                .expectStatus().isOk()
                .expectBody(SalesInfo.class);


    }


}
