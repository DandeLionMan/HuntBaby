package com.dandelion.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import com.dandelion.controller.AccountBusinessController;
import com.dandelion.domain.Cheat;
import com.dandelion.domain.CheatItem;
import com.dandelion.domain.User;
import com.dandelion.repository.CheatListRepository;
import com.dandelion.repository.CheatRepository;
import com.dandelion.utils.CommonUtils;

/**
 * 新闻接口实现
 * 
 * @author qing
 *
 */
@Service
public class CheatServiceImpl implements CheatService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountBusinessController.class);
	private CheatRepository cheatRepository;  // 保存单个数据
	private CheatListRepository cheatListRepository;  // 保存列表数据 需要审核
	private UserService userService;

	/**
	 * 注入NewsRepository
	 * 
	 * @param newsRepository
	 * @param userService
	 */
	@Autowired
	public CheatServiceImpl(CheatRepository cheatRepository,CheatListRepository cheatListRepository ,UserService userService) {
		this.cheatRepository = cheatRepository;
		this.cheatListRepository = cheatListRepository;
		this.userService = userService;
	}

	@Override
	public List<Cheat> getCheat() {
		List<Cheat> cheats = (List<Cheat>) cheatRepository.findAll();
		return cheats;
	}

	@Override
	public Cheat getCheatsListforkey(String key) {
		Cheat cheat = cheatRepository.findByCheatwechat(key);
		return cheat;
	}
	
	@Override
	public List<Cheat> saveCheatList(JSONObject cheatJson, List<MultipartFile> files, HttpServletRequest request) {
		User user = userService.getCurrentUser();

		LOGGER.warn("getCurrentUsername " + userService.getCurrentUsername());
		LOGGER.warn("files.size() " + files.size());
		if (user == null) {
			LOGGER.warn("current user null");
		} else {
			CheatItem cheatItem = new CheatItem();
			cheatItem.setCheatwechat(cheatJson.optString("cheatwechat"));
			cheatItem.setLabel(cheatJson.optInt("label"));
			cheatItem.setDetail(cheatJson.optString("detail"));
			cheatItem.setId(cheatJson.optLong("id"));
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			cheatItem.setDatetime(dateFormater.format(new Date()));
			cheatItem.setStatus(1);
			//saveCheat(cheatItem);
			LOGGER.warn("time " + cheatItem.getDatetime());
			List<String> listFiles = CommonUtils.saveFiles(request, files ,  CommonUtils.SAVE_TYPE_CHEAT , true); // 此处可以是flase
			for (int i = 0; i < listFiles.size(); ++i) {
				switch (i) {
				case 0:
					cheatItem.setThumb1(listFiles.get(0));
					break;
				case 1:
					cheatItem.setThumb2(listFiles.get(1));
					break;
				case 2:
					cheatItem.setThumb3(listFiles.get(2));
					break;
				}
				
				LOGGER.warn("current user null" + listFiles.get(i));
			}
			
			cheatListRepository.save(cheatItem);
		}

		return null;
	}

	@Override
	public Cheat saveCheat(CheatItem cheatItem) {
		Cheat cheat = cheatRepository.findByCheatwechat(cheatItem.getCheatwechat());
		if (cheat == null) {
			Cheat newCheat = new Cheat();
			newCheat.setCheatwechat(cheatItem.getCheatwechat());
			newCheat.setDetail(cheatItem.getDetail());
			switch (cheatItem.getLabel()) {
				case 0 :
					newCheat.setCheat1(1);
					break;
				case 1 :
					newCheat.setCheat2(1);
					break;
				case 2 :
					newCheat.setCheat3(1);
					break;
				case 3 :
					newCheat.setCheat4(1);
					break;
				case 4 :
					newCheat.setCheat5(1);
					break;
				case 5 :
					newCheat.setCheat6(1);
					break;
			}
			newCheat.setCheatcount(1);
			cheatRepository.save(newCheat);
			return newCheat;
		} else {
			switch (cheatItem.getLabel()) {
				case 0 :
					int currentCheat1 = cheat.getCheat1();
					currentCheat1++;
					cheat.setCheat1(currentCheat1);
					break;
				case 1 :
					int currentCheat2 = cheat.getCheat2();
					currentCheat2++;
					cheat.setCheat2(currentCheat2);
					break;
				case 2 :
					int currentCheat3 = cheat.getCheat3();
					currentCheat3++;
					cheat.setCheat3(currentCheat3);
					break;
				case 3 :
					int currentCheat4 = cheat.getCheat4();
					currentCheat4++;
					cheat.setCheat4(currentCheat4);
					break;
				case 4 :
					int currentCheat5 = cheat.getCheat5();
					currentCheat5++;
					cheat.setCheat5(currentCheat5);
					break;
				case 5 :
					int currentCheat6 = cheat.getCheat6();
					currentCheat6++;
					cheat.setCheat6(currentCheat6);
					break;
			}
			cheat.setCheatcount(cheat.getCheat1()+cheat.getCheat2()+cheat.getCheat3()+cheat.getCheat4()+cheat.getCheat5()+cheat.getCheat6());
			cheatRepository.save(cheat);
			return cheat;
		}

	}

	@Override
	public List<CheatItem> getCheatItemForKey(String key) {
		List<CheatItem> cheats = cheatListRepository.findByCheatwechat(key);;
		return cheats;
	}
}