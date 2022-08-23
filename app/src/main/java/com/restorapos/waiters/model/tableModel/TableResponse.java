package com.restorapos.waiters.model.tableModel;

import com.google.gson.annotations.SerializedName;

public class TableResponse{

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("data")
	private DataItem data;

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

	public void setData(DataItem data){
		this.data = data;
	}

	public DataItem getData(){
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
				"TableResponse{" +
						"status_code = '" + statusCode + '\'' +
						",data = '" + data + '\'' +
						",message = '" + message + '\'' +
						",status = '" + status + '\'' +
						"}";
	}
}