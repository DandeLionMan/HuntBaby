package com.dandelion.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户User POJO定义
 * 
 * @author qing
 *
 */
@Entity
@Table(name = "cheatitem" )
public class CheatItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cheatwechat;
	private int label = 0;
	
	private int count = 0 ; //排名
	
	private String thumb1;
	private String thumb2;
	private String thumb3;
	
	public String datetime;

	private String detail ; 
	
	private int status = 0;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public Long getId() {
		return id;
	}

	public String getCheatwechat() {
		return cheatwechat;
	}

	public void setCheatwechat(String cheatwechat) {
		this.cheatwechat = cheatwechat;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
