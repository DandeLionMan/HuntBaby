package com.dandelion.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 创建账号信息
 * 
 * @author qing
 */
@Entity
@Table(name = "material" )
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private User user;
	
	@Column(insertable=false,updatable=false)
	private Long user_id;
	
	private String wechat; // 卖家微信
	
	private String buywechat; // 买家微信

	private int materialindex;  // 材料
	
	private int count ; // 数量
	
	private double materialprice = 0; //商品价格
	
	private double materiaforpricesell = 0; //卖家服务价格
	
	private double materiaforpricebuy = 0; //买家服务价格
	
	private int service = 0 ;// 服务器
	
	private String datetime;
	
	private String detail; // 描述
	
	private int materialforresult = 0 ;  //交易状态
	
	public String getBuywechat() {
		return buywechat;
	}

	public void setBuywechat(String buywechat) {
		this.buywechat = buywechat;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getService() {
		return service;
	}

	public void setService(int service) {
		this.service = service;
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public double getMaterialprice() {
		return materialprice;
	}

	public void setMaterialprice(double materialprice) {
		this.materialprice = materialprice;
	}

	public double getMateriaforpricesell() {
		return materiaforpricesell;
	}

	public void setMateriaforpricesell(double materiaforpricesell) {
		this.materiaforpricesell = materiaforpricesell;
	}

	public double getMateriaforpricebuy() {
		return materiaforpricebuy;
	}

	public void setMateriaforpricebuy(double materiaforpricebuy) {
		this.materiaforpricebuy = materiaforpricebuy;
	}

	public int getMaterialforresult() {
		return materialforresult;
	}

	public void setMaterialforresult(int materialforresult) {
		this.materialforresult = materialforresult;
	}

	public int getMaterialindex() {
		return materialindex;
	}

	public void setMaterialindex(int materialindex) {
		this.materialindex = materialindex;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
	
	
	
}
