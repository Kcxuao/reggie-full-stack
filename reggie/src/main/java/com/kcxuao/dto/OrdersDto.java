package com.kcxuao.dto;


import com.kcxuao.domain.OrderDetail;
import com.kcxuao.domain.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
	
}
