package com.restorapos.waiters.model;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class AddonsinfoItem{

	@SerializedName("addonsquantity")
	private String addonsQty;

	@SerializedName("addonsprice")
	private String addonsPrice;

	@SerializedName("addonsid")
	private String addonsId;


	@SerializedName("add_on_name")
	private String addonsName;

	public void setAddonsQty(String addonsQty){
		this.addonsQty = addonsQty;
	}

	public String getAddonsQty(){
		return addonsQty;
	}

	public void setAddonsPrice(String addonsPrice){
		this.addonsPrice = addonsPrice;
	}

	public String getAddonsPrice(){
		return addonsPrice;
	}

	public void setAddonsId(String addonsId){
		this.addonsId = addonsId;
	}

	public String getAddonsId(){
		return addonsId;
	}

	public void setAddonsName(String addonsName){
		this.addonsName = addonsName;
	}

	public String getAddonsName(){
		return addonsName;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"AddonsinfoItem{" + 
			"addonsquantity = '" + addonsQty + '\'' +
			",addons_price = '" + addonsPrice + '\'' + 
			",addons_id = '" + addonsId + '\'' + 
			",addons_name = '" + addonsName + '\'' + 
			"}";
		}
}