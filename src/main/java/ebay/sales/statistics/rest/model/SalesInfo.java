package ebay.sales.statistics.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SalesInfo {
    private Float totalSalesAmount;
    private Float averageAmountPerOrder;
}
