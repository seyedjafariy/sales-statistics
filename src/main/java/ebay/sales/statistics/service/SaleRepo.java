package ebay.sales.statistics.service;
import ebay.sales.statistics.rest.model.SalesInfo;

public interface SaleRepo {
    void save(Float sale);
    SalesInfo getSalesInfo();

}
