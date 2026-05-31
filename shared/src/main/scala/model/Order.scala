package model

import types.Types.Id

/**
  * Class that represents customer orders in the application.
  *
  * @param id unique identifier of the order
  * @param items list of items included in the order
  * @param totalPrice total price of the order, calculated as the sum of item prices
  * 
  * @author Eva Ray
  */
case class Order(
    id: Id,
    items: List[Item],
    totalPrice: Double,
)
