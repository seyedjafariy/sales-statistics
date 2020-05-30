package ebay.sales.statistics.service.imlp;

import ebay.sales.statistics.rest.model.SalesInfo;
import ebay.sales.statistics.service.SaleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class SaleRepositoryImpl implements SaleRepository {
    private static final long EXPIRATION_TIME = 60;
    private final Map<Long, Float> salesData = Collections.synchronizedMap(new LinkedHashMap());
    private Float avg = 0.0F;

    public SaleRepositoryImpl() {
        final int PERIOD = 1000;
        Timer timer = new Timer();
        timer.schedule(new HouseKeepingScheduler(), 0, PERIOD);
    }

    @Override
    public void save(Float sale) {
        log.debug("sales amount : {}", sale);
        if (isNull(avg)) {
            avg = sale;
        } else {
            avg = (avg * salesData.size() + sale) / (salesData.size() + 1);
        }
        log.debug("avg sale: {}", avg);
        log.debug("totalSale: {}", avg * salesData.size());
        salesData.put(new Date().getTime(), sale);
    }

    @Override
    public SalesInfo getSalesInfo() {
        //removeExpiredElements(salesData); is it required?
        return SalesInfo
                .builder()
                .totalSalesAmount(avg * salesData.size())
                .averageAmountPerOrder(avg)
                .build();
    }

    public void removeExpiredElements(Map<Long, Float> salesData) {
        Date expirationTime = new Date();
        expirationTime.setTime(new Date().getTime() - EXPIRATION_TIME * 1000L);
        boolean allFresh = false;
        Iterator<Map.Entry<Long, Float>> iterator = salesData.entrySet().iterator();
        while (iterator.hasNext() && !allFresh) {
            Map.Entry<Long, Float> entry = iterator.next();

            if (entry.getKey().compareTo(expirationTime.getTime()) < 0) {
                if (salesData.size() > 1) {
                    avg = ((avg * salesData.size()) - entry.getValue()) / (salesData.size() - 1);
                } else {
                    avg = 0.0F;
                }
                iterator.remove();
            } else {
                allFresh = true;
                log.debug("The rest of {} elements are fresh.", salesData.size());
            }

        }
    }

    class HouseKeepingScheduler extends TimerTask {
        public void run() {
            removeExpiredElements(salesData);

        }
    }
}


