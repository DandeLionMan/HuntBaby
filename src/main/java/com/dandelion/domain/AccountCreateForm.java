package com.dandelion.domain;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建账号
 * 
 * @author qing
 *
 */
public class AccountCreateForm {
	
	private String wechat;

	private String nickname;
	
	private double accountforprice = 0; //商品价格
	
	private double accountforpricesell = 0; //卖家服务价格
	
	private double accountforpricebuy = 0; //买家服务价格
	
	private int server = 0 ;// 服务器
	
	private int anthortype = 0; // 账号类型
	
	private int recharge = 0; //充值
	
	private int ordnance = 0; //炮台
	
	private int vip;
	
	private int level;

	private int isconvert = 0;
	
	private int ishat = 0;
	
	private int goldmoney; // 金币
	
	private int backmoney = 0;  // 返利
	
	private String thumb1;
	private String thumb2;
	private String thumb3;
	
	private Date datetime;
	
	private String detail; // 描述
	
	private int accountforresult = 0 ;  //交易状态

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public double getAccountforprice() {
		return accountforprice;
	}

	public void setAccountforprice(double accountforprice) {
		this.accountforprice = accountforprice;
	}

	public double getAccountforpricesell() {
		return accountforpricesell;
	}

	public void setAccountforpricesell(double accountforpricesell) {
		this.accountforpricesell = accountforpricesell;
	}

	public double getAccountforpricebuy() {
		return accountforpricebuy;
	}

	public void setAccountforpricebuy(double accountforpricebuy) {
		this.accountforpricebuy = accountforpricebuy;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

	public int getAnthortype() {
		return anthortype;
	}

	public void setAnthortype(int anthortype) {
		this.anthortype = anthortype;
	}

	public int getRecharge() {
		return recharge;
	}

	public void setRecharge(int recharge) {
		this.recharge = recharge;
	}

	public int getOrdnance() {
		return ordnance;
	}

	public void setOrdnance(int ordnance) {
		this.ordnance = ordnance;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIsconvert() {
		return isconvert;
	}

	public void setIsconvert(int isconvert) {
		this.isconvert = isconvert;
	}

	public int getIshat() {
		return ishat;
	}

	public void setIshat(int ishat) {
		this.ishat = ishat;
	}

	public int getGoldmoney() {
		return goldmoney;
	}

	public void setGoldmoney(int goldmoney) {
		this.goldmoney = goldmoney;
	}

	public int getBackmoney() {
		return backmoney;
	}

	public void setBackmoney(int backmoney) {
		this.backmoney = backmoney;
	}

	public String getThumb1() {
		return thumb1;
	}

	public void setThumb1(String thumb1) {
		this.thumb1 = thumb1;
	}

	public String getThumb2() {
		return thumb2;
	}

	public void setThumb2(String thumb2) {
		this.thumb2 = thumb2;
	}

	public String getThumb3() {
		return thumb3;
	}

	public void setThumb3(String thumb3) {
		this.thumb3 = thumb3;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getAccountforresult() {
		return accountforresult;
	}

	public void setAccountforresult(int accountforresult) {
		this.accountforresult = accountforresult;
	}
	
}

