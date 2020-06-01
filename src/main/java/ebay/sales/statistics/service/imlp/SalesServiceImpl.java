package ebay.sales.statistics.service.imlp;

import ebay.sales.statistics.rest.model.SalesInfo;
import ebay.sales.statistics.service.SaleRepo;
import ebay.sales.statistics.service.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesServiceImpl implements SalesService {
    private final SaleRepo saleRepository;

    @Override
    public void saveSale(String sale) {
        try {
            Float salesAmount = Float.valueOf(sale);
            saleRepository.save(salesAmount);
        } catch (Exception ex) {
            log.error("Invalid value for sale {}", sale, ex.getCause());
            return;
        }

    }

    @Override
    public SalesInfo getSalesInfo() {
        return saleRepository.getSalesInfo();
    }


}
