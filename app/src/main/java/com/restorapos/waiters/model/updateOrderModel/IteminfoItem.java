package com.restorapos.waiters.model.updateOrderModel;

import java.util.List;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.google.gson.annotations.SerializedName;

public class IteminfoItem{

	public int quantitys=0;
	@SerializedName("Varientid")
	private String varientid;

	@SerializedName("ProductName")
	private String productName;

	@SerializedName("addons")
	private int addons;

	@SerializedName("offerstartdate")
	private String offerstartdate;

	@SerializedName("ProductImage")
	private String productImage;

	@SerializedName("productvat")
	private String productvat;

	@SerializedName("OffersRate")
	private String offersRate;

	@SerializedName("Varientname")
	private String varientname;

	@SerializedName("destcription")
	private String destcription;

	@SerializedName("offerIsavailable")
	private String offerIsavailable;

	@SerializedName("ProductsID")
	private String productsID;

	@SerializedName("component")
	private String component;

	@SerializedName("addonsinfo")
	private List<Addonsinfo> addonsinfo;

	@SerializedName("price")
	private String price;

	@SerializedName("offerendate")
	private String offerendate;

	@SerializedName("Itemqty")
	private String itemqty;

	@SerializedName("itemnotes")
	private String itemnotes;

	public void setVarientid(String varientid){
		this.varientid = varientid;
	}

	public String getVarientid(){
		return varientid;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setAddons(int addons){
		this.addons = addons;
	}

	public int getAddons(){
		return addons;
	}

	public void setOfferstartdate(String offerstartdate){
		this.offerstartdate = offerstartdate;
	}

	public String getOfferstartdate(){
		return offerstartdate;
	}

	public void setProductImage(String productImage){
		this.productImage = productImage;
	}

	public String getProductImage(){
		return productImage;
	}

	public void setProductvat(String productvat){
		this.productvat = productvat;
	}

	public String getProductvat(){
		return productvat;
	}

	public void setOffersRate(String offersRate){
		this.offersRate = offersRate;
	}

	public String getOffersRate(){
		return offersRate;
	}

	public void setVarientname(String varientname){
		this.varientname = varientname;
	}

	public String getVarientname(){
		return varientname;
	}

	public void setDestcription(String destcription){
		this.destcription = destcription;
	}

	public String getDestcription(){
		return destcription;
	}

	public void setOfferIsavailable(String offerIsavailable){
		this.offerIsavailable = offerIsavailable;
	}

	public String getOfferIsavailable(){
		return offerIsavailable;
	}

	public void setProductsID(String productsID){
		this.productsID = productsID;
	}

	public String getProductsID(){
		return productsID;
	}

	public void setComponent(String component){
		this.component = component;
	}

	public String getComponent(){
		return component;
	}

	public void setAddonsinfo(List<Addonsinfo> addonsinfo){
		this.addonsinfo = addonsinfo;
	}

	public List<Addonsinfo> getAddonsinfo(){
		return addonsinfo;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setOfferendate(String offerendate){
		this.offerendate = offerendate;
	}

	public String getOfferendate(){
		return offerendate;
	}

	public void setItemqty(String itemqty){
		this.itemqty = itemqty;
	}

	public String getItemqty(){
		return itemqty;
	}

	public void setItemnotes(String itemnotes){
		this.itemnotes = itemnotes;
	}

	public String getItemnotes(){
		return itemnotes;
	}

	@Override
 	public String toString(){
		return 
			"IteminfoItem{" + 
			"varientid = '" + varientid + '\'' + 
			",productName = '" + productName + '\'' + 
			",addons = '" + addons + '\'' + 
			",offerstartdate = '" + offerstartdate + '\'' + 
			",productImage = '" + productImage + '\'' + 
			",productvat = '" + productvat + '\'' + 
			",offersRate = '" + offersRate + '\'' + 
			",varientname = '" + varientname + '\'' + 
			",destcription = '" + destcription + '\'' + 
			",offerIsavailable = '" + offerIsavailable + '\'' + 
			",productsID = '" + productsID + '\'' + 
			",component = '" + component + '\'' + 
			",addonsinfo = '" + addonsinfo + '\'' + 
			",price = '" + price + '\'' + 
			",offerendate = '" + offerendate + '\'' + 
			",itemqty = '" + itemqty + '\'' + 
			",itemnotes = '" + itemnotes + '\'' + 
			"}";
		}
}