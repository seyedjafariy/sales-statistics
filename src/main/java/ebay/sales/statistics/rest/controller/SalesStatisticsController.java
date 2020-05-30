package ebay.sales.statistics.rest.controller;

import ebay.sales.statistics.rest.model.SalesInfo;
import ebay.sales.statistics.service.SalesService;
import ebay.sales.statistics.service.StatisticsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SalesStatisticsController {
    private final SalesService salesService;
    private final StatisticsService statisticsService;

    @PostMapping(path = "/sales", consumes = {APPLICATION_FORM_URLENCODED_VALUE})
    public Mono<ResponseEntity> sale(Sales sales) {
        salesService.saveSale(sales.getSales_amount());
        return Mono.just(ResponseEntity.accepted().build());
    }

    @GetMapping(path = "/statistics")
    public Mono<ResponseEntity<SalesInfo>> getSalesInfo() {
        SalesInfo salesInfo = statisticsService.getSalesInfo();
        return Mono.just(ResponseEntity.ok(salesInfo));
    }

    @Data
    class Sales{
        String sales_amount;
    }
}
