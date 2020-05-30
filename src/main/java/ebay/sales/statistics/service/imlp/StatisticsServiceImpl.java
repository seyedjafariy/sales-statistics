package ebay.sales.statistics.service.imlp;

import ebay.sales.statistics.rest.model.SalesInfo;
import ebay.sales.statistics.service.SaleRepository;
import ebay.sales.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final SaleRepository saleRepository;
    @Override
    public SalesInfo getSalesInfo() {
        return saleRepository.getSalesInfo();
    }
}
