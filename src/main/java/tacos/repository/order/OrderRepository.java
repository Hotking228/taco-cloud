package tacos.repository.order;

import org.springframework.data.jpa.repository.Query;
import tacos.entity.TacoOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository
        extends CrudRepository<TacoOrder, Long> {

    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    /*
    Операторы Spring data jpa:
        IsAfter, After, IsGreaterThan, GreaterThan;
        IsGreaterThanEqual, GreaterThanEqual;
        IsBefore, Before, IsLessThan, LessThan;
        IsLessThanEqual, LessThanEqual;
        IsBetween, Between;
        IsNull, Null;
        IsNotNull, NotNull;
        IsIn, In;
        IsNotIn, NotIn;
        IsStartingWith, StartingWith, StartsWith;
        IsEndingWith, EndingWith, EndsWith;
        IsContaining, Containing, Contains;
        IsLike, Like;
        IsNotLike, NotLike;
        IsTrue, True;
        IsFalse, False;
        Is, Equals;
        IsNot, Not;
        IgnoringCase, IgnoresCase.
     */

    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(
            String deliveryZip, Date startDate, Date endDate);

    List<TacoOrder> findByDeliveryStreetAndDeliveryCity(
            String deliveryStreet, String deliveryCity);

    List<TacoOrder> findByDeliveryCityOrderByDeliveryCity(
            String deliveryCity);

    @Query("select t_o from TacoOrder t_o where t_o.deliveryCity='Seattle'")
    List<TacoOrder> findOrdersDeliveredInSeattle();

    @Query("delete from TacoOrder")
    void deleteAll();
}
