package com.dandelion.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 修改、重置密码POJO表单
 * 
 * @author jiekechoo
 *
 */
public class SignForm {

	// 手机号码
	@NotEmpty
	@Size(min = 11, max = 11)
	private String mobile;

	private String sign;

	public String getMobile() {
		return mobile;
	}

	public String getPassword() {
		return sign;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

		
}
