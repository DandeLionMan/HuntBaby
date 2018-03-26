package com.dandelion.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 核心定义
 * 
 * @author qing
 *
 */
@Entity
@Table(name = "core", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class Core {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	private double newprice ; // 价格差价

	private int version;

    private int versionurl;
    
    private String versionprompt1; // 新版本描述
    
    private String versionprompt2; // 新版本描述
    
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersionurl() {
		return versionurl;
	}

	public void setVersionurl(int versionurl) {
		this.versionurl = versionurl;
	}

	public String getVersionprompt1() {
		return versionprompt1;
	}

	public void setVersionprompt1(String versionprompt1) {
		this.versionprompt1 = versionprompt1;
	}

	public String getVersionprompt2() {
		return versionprompt2;
	}

	public void setVersionprompt2(String versionprompt2) {
		this.versionprompt2 = versionprompt2;
	}

	public double getNewprice() {
		return newprice;
	}

	public void setNewprice(double newprice) {
		this.newprice = newprice;
	}

	@Override
	public String toString() {
		return "Core [id=" + id + ", version=" + version + ", versionurl=" + versionurl + ", versionprompt1="
				+ versionprompt1 + ", versionprompt2=" + versionprompt2 + ", newprice=" + newprice + "]";
	}
    
}
