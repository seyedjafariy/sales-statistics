package ebay.sales.statistics.service.imlp;

import ebay.sales.statistics.service.SaleRepository;
import ebay.sales.statistics.service.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesServiceImpl implements SalesService {

    private final SaleRepository saleRepository;

    @Override
    public void saveSale(String sale) {
        Float salesAmount = Float.valueOf(sale.replace("sales_amount=", ""));
        saleRepository.save(salesAmount);

    }
}
