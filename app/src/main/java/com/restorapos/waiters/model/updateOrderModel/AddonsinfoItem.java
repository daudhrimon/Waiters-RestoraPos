package com.restorapos.waiters.model.updateOrderModel;

import com.google.gson.annotations.SerializedName;

public class AddonsinfoItem{

	@SerializedName("add_on_id")
	private String addOnId;

	@SerializedName("price")
	private String price;

	@SerializedName("addonsName")
	private String addonsName;

	@SerializedName("add_on_qty")
	private String addOnQty;

	public void setAddOnId(String addOnId){
		this.addOnId = addOnId;
	}

	public String getAddOnId(){
		return addOnId;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setAddonsName(String addonsName){
		this.addonsName = addonsName;
	}

	public String getAddonsName(){
		return addonsName;
	}

	public void setAddOnQty(String addOnQty){
		this.addOnQty = addOnQty;
	}

	public String getAddOnQty(){
		return addOnQty;
	}

	@Override
 	public String toString(){
		return 
			"AddonsinfoItem{" + 
			"add_on_id = '" + addOnId + '\'' + 
			",price = '" + price + '\'' + 
			",addonsName = '" + addonsName + '\'' + 
			",add_on_qty = '" + addOnQty + '\'' + 
			"}";
		}
}