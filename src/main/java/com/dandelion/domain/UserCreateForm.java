package com.dandelion.domain;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建用户字段POJO定义
 * 
 * @author qing
 *
 */
public class UserCreateForm {

	@NotEmpty
	private String username;

	@NotEmpty
	private String password;

//	@NotEmpty
//	private String vcode;
	
	private String weixin = null;
	


	private int submitcountbusin = 5;
	private int submitcountmoney = 5;
	private int submitcountrent = 10;
	
	private String sign;
	
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public void setSubmitcountmoney(int submitcountmoney) {
		this.submitcountmoney = submitcountmoney;
	}

	public int getSubmitcountrent() {
		return submitcountrent;
	}

	public void setSubmitcountrent(int submitcountrent) {
		this.submitcountrent = submitcountrent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public String getVcode() {
//		return vcode;
//	}
//
//	public void setVcode(String vcode) {
//		this.vcode = vcode;
//	}

	@Override
	public String toString() {
		return "UserCreateForm [username=" + username + ", password=" + password + ", weixin=" + weixin
				+ ", submitcountbusin=" + submitcountbusin + ", submitcountmoney=" + submitcountmoney
				+ ", submitcountrent=" + submitcountrent + ", sign=" + sign + "]";
	}
}
