package com.restorapos.waiters.model.customerModel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("CustomerID")
	private String customerID;

	@SerializedName("CustomerName")
	private String customerName;
	@SerializedName("TypeID")
	private String typeID;

	@SerializedName("TypeName")
	private String typeName;

	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}

	public String getCustomerID(){
		return customerID;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return customerName;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"customerID = '" + customerID + '\'' + 
			",customerName = '" + customerName + '\'' + 
			"}";
		}
}