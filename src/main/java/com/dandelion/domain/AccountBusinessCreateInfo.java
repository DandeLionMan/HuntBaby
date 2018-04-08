package com.dandelion.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 创建账号信息
 *
 * @author qing
 */
@Entity
@Table(name = "account")
@DynamicUpdate // 更新时间
@Data
public class AccountBusinessCreateInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;

//    @OneToOne(targetEntity = User.class, optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "userid", referencedColumnName = "id", insertable = false, updatable = false)
//    public Long userid;


    public User user;


    public User getUser() {
        return user;
    }


    @JsonProperty("platform")
    public int platform; // 平台
    private String wechat;
    private String buywechat; // 买家微信
    private String nickname;
    private double accountforprice = 0; //商品价格
    private double accountforpricesell = 0; //卖家服务价格

    private double accountforpricebuy = 0; //买家服务价格

    private int server = 0;// 服务器

    private int anthortype = 0; // 账号类型

    private int recharge = 0; //充值

    private int redress = 0; //救济

    private int ordnance = 0; //炮台

    private int macordnance = 0; //机械炮台

    private int vip;

    private int level;

    private int isconvert = 0;

    private int ishat = 0;

    private int hattime = 0; // 帽子时间

    private int goldmoney; // 金币

    private int backmoney = 0;  // 返利

    private int redpackage = 0;  //红包

    private int goldscale = 0; // 金币比例

    private int ismaterial = 0; // 是否带材料

    private int iswarrant = 0;// 是否担保

    private int diamond = 0; // 钻石

    private String thumb1;
    private String thumb2;
    private String thumb3;
    private String thumb4;
    private String thumb5;

    private String datetime;

    private String detail; // 描述

    private int accountforresult = 0;  //交易状态

    private String report; // 交易描述


    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getBuywechat() {
        return buywechat;
    }

    public void setBuywechat(String buywechat) {
        this.buywechat = buywechat;
    }

    public int getRedress() {
        return redress;
    }

    public void setRedress(int redress) {
        this.redress = redress;
    }


    public void setUser(User user) {
        this.user = user;
    }

//    public Long getUserid() {
//        return userid;
//    }

//    public void setUserid(Long userid) {
//        this.userid = userid;
//    }

    public int getMacordnance() {
        return macordnance;
    }

    public void setMacordnance(int macordnance) {
        this.macordnance = macordnance;
    }

    public int getRedpackage() {
        return redpackage;
    }

    public void setRedpackage(int redpackage) {
        this.redpackage = redpackage;
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

    public int getRecharge() {
        return recharge;
    }

    public void setRecharge(int recharge) {
        this.recharge = recharge;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getOrdnance() {
        return ordnance;
    }

    public void setOrdnance(int ordnance) {
        this.ordnance = ordnance;
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

    public String getNickname() {
        return nickname;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getAnthortype() {
        return anthortype;
    }

    public void setAnthortype(int anthortype) {
        this.anthortype = anthortype;
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

    public int getAccountforresult() {
        return accountforresult;
    }

    public void setAccountforresult(int accountforresult) {
        this.accountforresult = accountforresult;
    }

    public int getHattime() {
        return hattime;
    }

    public void setHattime(int hattime) {
        this.hattime = hattime;
    }

    public int getGoldscale() {
        return goldscale;
    }

    public void setGoldscale(int goldscale) {
        this.goldscale = goldscale;
    }

    public String getThumb4() {
        return thumb4;
    }

    public void setThumb4(String thumb4) {
        this.thumb4 = thumb4;
    }

    public String getThumb5() {
        return thumb5;
    }

    public void setThumb5(String thumb5) {
        this.thumb5 = thumb5;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getIsmaterial() {
        return ismaterial;
    }

    public void setIsmaterial(int ismaterial) {
        this.ismaterial = ismaterial;
    }

    public int getIswarrant() {
        return iswarrant;
    }

    public void setIswarrant(int iswarrant) {
        this.iswarrant = iswarrant;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }


}
