package com.dandelion.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import com.dandelion.domain.AccountBusinessCreateInfo;

/**
 * 用户服务网接口定义
 * 
 * @author qing
 *
 */
public interface AccountService {

	boolean createAccount(JSONObject accountCreateForm , List<MultipartFile> files, HttpServletRequest request);
	
	List<AccountBusinessCreateInfo> getAccountList(Long startid, int serviceIndex , int vipIndex ,  Pageable p);
	
	Object uploadThumb(MultipartFile file, HttpServletRequest request);
	
	
}
