package com.dandelion.service;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.dandelion.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import com.dandelion.domain.Material;
import com.dandelion.repository.MaterialRepository;

/**
 * 用户服务接口实现
 * 
 * @author qing
 *
 */
@Service
public class MaterialServiceImpl implements MaterialService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialServiceImpl.class);
	private UserService userService;
	private static MaterialRepository materialRepository;

	@Autowired
	private Environment env;

	/**
	 * 装载userRepository
	 * 
	 */
	@Autowired
	public MaterialServiceImpl(UserService userService ,MaterialRepository materialRepository) {
		this.userService = userService;
		this.materialRepository = materialRepository;
	}



	@Override
	public boolean saveMaterial(JSONObject json) {
		
		User user = userService.getCurrentUser();

		LOGGER.warn("getCurrentUsername " + userService.getCurrentUsername());
		if (user == null) {
			LOGGER.warn("current user null");
		}
		if (user != null && json != null) {
			Material material = new Material();
			material.setWechat(user.getWeixin());
			material.setBuywechat("");
			material.setMaterialindex(json.getInt("materialindex"));
			material.setCount(json.getInt("count"));
			material.setUser(user);
			material.setMaterialprice(json.getDouble("materialprice"));
			material.setMateriaforpricesell(json.getDouble("materiaforpricesell"));
			material.setMateriaforpricebuy(json.getDouble("materiaforpricebuy"));
			material.setService(0);
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			material.setDatetime(dateFormater.format(new Date()));
			material.setDetail(json.getString("detail"));
			material.setMaterialforresult(json.getInt("materialforresult"));
			materialRepository.save(material);
			return true;
		}
		
		return false;
	}
}
