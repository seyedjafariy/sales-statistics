package ebay.sales.statistics.service;

import ebay.sales.statistics.rest.model.SalesInfo;

public interface SalesService {
    void saveSale(String  sale);
    SalesInfo getSalesInfo();
}
