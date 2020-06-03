package ebay.sales.statistics.service.imlp;

import ebay.sales.statistics.rest.model.SalesInfo;
import ebay.sales.statistics.rest.model.SalesRepoModel;
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
//    private final Map<Long, Float> salesData = Collections.synchronizedMap(new LinkedHashMap());
    //we are loosing a lot of functionality by using bare DataStructure
    // I think we can make use of tools like reddis here to improve our tooling and performmance
    private final LinkedList<SalesRepoModel> sales = new LinkedList<>();

    public SaleRepositoryImpl() {
        Timer timer = new Timer();
        timer.schedule(new RepositoryEvictScheduler(), 0, PERIOD);
    }

    @Override
    public void save(Float sale) {
        sales.add(SalesRepoModel.builder().time(new Date().getTime()).amount(sale).build());
//        salesData.put(new Date().getTime(), sale);
    }

    @Override
    @Transactional
    public SalesInfo getSalesInfo() {
        removeExpiredElements();
        if(sales.isEmpty()){
            return SalesInfo.builder().build();
        }

        //since we are using this list somewhere else we have to make a copy of it to make sure
        //we don't mess it up in sorting
        LinkedList<SalesRepoModel> cloned = (LinkedList<SalesRepoModel>) sales.clone();
        double totalSum = cloned.stream()
                //sorting can be improved significantly cause we know much more about the data.
                //  we can also remove the outdated ones too but I wanted to keep it as close to
                //  the original source as possible
                .sorted((t1, t2) -> (int) (t2.getTime() - t1.getTime()))
                .map(SalesRepoModel::getAmount)
                .mapToDouble(Float::floatValue)
                .average()
                .orElse(0);
        double avg = totalSum / cloned.size();
        return SalesInfo.builder()
                .averageAmountPerOrder(avg)
                .totalSalesAmount(totalSum)
                .build();
    }

    public void removeExpiredElements() {
        Date expirationTime = new Date();
        expirationTime.setTime(new Date().getTime() - EXPIRATION_TIME * 1000L);
        boolean allFresh = false;
        //since it's a queue too we can use a reversed iterator and reduce the time of looping
        // AFAIK it now starts from the head which is usually ok
        // but needs to start from end
        Iterator<SalesRepoModel> iterator = sales.iterator();
        while (iterator.hasNext() && !allFresh) {
            if (iterator.next().getTime() < expirationTime.getTime()) {
                iterator.remove();
            } else {
                allFresh = true;
                log.debug("Skip housekeeping as the rest of {} elements are fresh.", sales.size());
            }
        }
    }

    class RepositoryEvictScheduler extends TimerTask {
        public void run() {
            removeExpiredElements();
        }
    }
}


