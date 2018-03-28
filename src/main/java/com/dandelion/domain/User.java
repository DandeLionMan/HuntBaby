package com.dandelion.domain;


import javax.persistence.*;

/**
 * 用户User POJO定义
 * 
 * @author qing
 *
 */
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	private String username;
	private String password;
	private String image;
	private int client;  // 1是IOS  2是Androi
	private String weixin = null;
	private int submitcountbusin = 5;
	private int submitcountmoney = 5;
	private int submitcountrent = 5;
	private int submitcountmaterial = 5;
	private int submitcountcheat= 1;
	private String role = null;
	private int successbusin = 0; // 成功购买的账号
	
	private String datetime;
	
	private int enabled;
	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return "http://localhost:8080/" + image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getEnabled() {
		return enabled;
	}
	
	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public int getSubmitcountbusin() {
		return submitcountbusin;
	}

	public void setSubmitcountbusin(int submitcountbusin) {
		this.submitcountbusin = submitcountbusin;
	}

	public int getSubmitcountmoney() {
		return submitcountmoney;
	}
	
	public int getSuccessbusin() {
		return successbusin;
	}

	public void setSuccessbusin(int successbusin) {
		this.successbusin = successbusin;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSubmitcountmoney(int submitcountmoney) {
		this.submitcountmoney = submitcountmoney;
	}

	public int getSubmitcountrent() {
		return submitcountrent;
	}

	public void setSubmitcountrent(int submitcountrent) {
		this.submitcountrent = submitcountrent;
	}

	public int getSubmitcountmaterial() {
		return submitcountmaterial;
	}

	public void setSubmitcountmaterial(int submitcountmaterial) {
		this.submitcountmaterial = submitcountmaterial;
	}

	public int getSubmitcountcheat() {
		return submitcountcheat;
	}

	public void setSubmitcountcheat(int submitcountcheat) {
		this.submitcountcheat = submitcountcheat;
	}
	
	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", weixin=" + weixin + ", submitcountbusin="
				+ submitcountbusin + ", submitcountmoney=" + submitcountmoney + ", submitcountrent=" + submitcountrent
				+ ", submitcountmaterial=" + submitcountmaterial + ", submitcountcheat=" + submitcountcheat + "]";
	}

}
