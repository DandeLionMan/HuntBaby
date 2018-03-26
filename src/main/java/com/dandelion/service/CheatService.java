package com.dandelion.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import com.dandelion.domain.Cheat;
import com.dandelion.domain.CheatItem;

/**
 * 新闻接口
 * 
 * @author qing
 *
 */
public interface CheatService {

	List<Cheat> getCheat();
	
	Cheat getCheatsListforkey(String key);
	
	List<Cheat> saveCheatList(JSONObject accountCreateForm , List<MultipartFile> files , HttpServletRequest request);
	
	Cheat saveCheat(CheatItem cheat);
	
	
	List<CheatItem> getCheatItemForKey(String key);

}
