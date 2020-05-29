package ebay.sales.statistics.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SalesStatisticsController {
   /* private final SalesService salesService;
    private final StatisticsService statisticsService;
*/
    @PostMapping(path = "/sales",consumes = {APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity sale(@RequestBody @NotNull @NotBlank String salesAmount) {
        log.debug("salesAmount received : {}", salesAmount);
        return ResponseEntity.accepted().build();
    }

}
