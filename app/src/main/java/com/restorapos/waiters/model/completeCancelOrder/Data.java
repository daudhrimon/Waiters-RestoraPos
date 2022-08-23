package com.restorapos.waiters.model.completeCancelOrder;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("orderinfo")
	private List<OrderinfoItem> orderinfo;

	@SerializedName("totalorder")
	private int totalorder;

	public void setOrderinfo(List<OrderinfoItem> orderinfo){
		this.orderinfo = orderinfo;
	}

	public List<OrderinfoItem> getOrderinfo(){
		return orderinfo;
	}

	public void setTotalorder(int totalorder){
		this.totalorder = totalorder;
	}

	public int getTotalorder(){
		return totalorder;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"orderinfo = '" + orderinfo + '\'' + 
			",totalorder = '" + totalorder + '\'' + 
			"}";
		}
}