package com.restorapos.waiters.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CartResponse {

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("data")
	private List<Object> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public void setData(List<Object> data){
		this.data = data;
	}

	public List<Object> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"CartResponse{" +
			"status_code = '" + statusCode + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}