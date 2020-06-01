package ebay.sales.statistics.service.imlp;

import ebay.sales.statistics.rest.model.SalesInfo;
import ebay.sales.statistics.service.SaleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Repository
@Slf4j
public class SaleRepositoryImpl implements SaleRepo {
    private static final long EXPIRATION_TIME = 60;
    private final int PERIOD = 1000;
    private final Map<Long, Float> salesData = Collections.synchronizedMap(new LinkedHashMap());

    public SaleRepositoryImpl() {
        Timer timer = new Timer();
        timer.schedule(new RepositoryEvictScheduler(), 0, PERIOD);
    }

    @Override
    public void save(Float sale) {
        salesData.put(new Date().getTime(), sale);
    }

    @Override
    @Transactional
    public SalesInfo getSalesInfo() {
        removeExpiredElements();
        if(salesData.isEmpty()){
            return SalesInfo.builder().build();
        }
        double totalSum = salesData.entrySet().stream()
                .map(Map.Entry::getValue)
                .mapToDouble(Float::floatValue).sum();
        double avg = totalSum / salesData.size();
        return SalesInfo.builder()
                .averageAmountPerOrder(avg)
                .totalSalesAmount(totalSum)
                .build();
    }

    public void removeExpiredElements() {
        Date expirationTime = new Date();
        expirationTime.setTime(new Date().getTime() - EXPIRATION_TIME * 1000L);
        boolean allFresh = false;
        Iterator<Map.Entry<Long, Float>> iterator = salesData.entrySet().iterator();
        while (iterator.hasNext() && !allFresh) {
            if (iterator.next().getKey().compareTo(expirationTime.getTime()) < 0) {
                iterator.remove();
            } else {
                allFresh = true;
                log.debug("Skip housekeeping as the rest of {} elements are fresh.", salesData.size());
            }
        }
    }

    class RepositoryEvictScheduler extends TimerTask {
        public void run() {
            removeExpiredElements();
        }
    }
}


