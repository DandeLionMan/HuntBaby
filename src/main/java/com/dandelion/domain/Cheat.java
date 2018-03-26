package com.dandelion.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 用户User POJO定义
 * 
 * @author qing
 *
 */
@Entity
public class Cheat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cheatwechat;
	private int label = 0;

	private int cheatcount = 0; // 举报次数

	private int cheat1 = 0;

	private int cheat2 = 0;

	private int cheat3 = 0;

	private int cheat4 = 0;

	private int cheat5 = 0;
	
	private int cheat6 = 0;

	private String detail;

	public int getCheatcount() {
		return cheatcount;
	}

	public void setCheatcount(int cheatcount) {
		this.cheatcount = cheatcount;
	}

	public int getCheat1() {
		return cheat1;
	}

	public void setCheat1(int cheat1) {
		this.cheat1 = cheat1;
	}

	public int getCheat2() {
		return cheat2;
	}

	public void setCheat2(int cheat2) {
		this.cheat2 = cheat2;
	}

	public int getCheat3() {
		return cheat3;
	}

	public void setCheat3(int cheat3) {
		this.cheat3 = cheat3;
	}

	public int getCheat4() {
		return cheat4;
	}

	public void setCheat4(int cheat4) {
		this.cheat4 = cheat4;
	}

	public int getCheat5() {
		return cheat5;
	}

	public void setCheat5(int cheat5) {
		this.cheat5 = cheat5;
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

	public int getCheat6() {
		return cheat6;
	}

	public void setCheat6(int cheat6) {
		this.cheat6 = cheat6;
	}
	
	

}
