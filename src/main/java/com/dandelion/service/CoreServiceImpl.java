package com.dandelion.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dandelion.domain.Core;
import com.dandelion.repository.CoreRepository;

/**
 * 短信服务
 * 
 * @author qing
 *
 */
@Service
public class CoreServiceImpl  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreServiceImpl.class);
	private CoreRepository coreRepository;

	@Autowired
	public CoreServiceImpl(CoreRepository coreRepository) {
		this.coreRepository = coreRepository;
	}
	
	/**
	 * 保存核心信息
	 */
	public void saveCore(Core core) {
		if (core != null) {
//			Core coreOld = coreRepository.findOne((long) 1);
//			coreOld = core;
			Core onlyCore = coreRepository.findById(1);
			onlyCore.setNewprice(core.getNewprice());
			onlyCore.setVersion(core.getVersion());
			onlyCore.setVersionprompt1(core.getVersionprompt1());
			onlyCore.setVersionprompt2(core.getVersionprompt2());

			coreRepository.save(onlyCore);
		}
	}

}
