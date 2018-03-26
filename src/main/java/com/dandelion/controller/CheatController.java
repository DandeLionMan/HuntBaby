package com.dandelion.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dandelion.domain.Cheat;
import com.dandelion.domain.CheatItem;
import com.dandelion.domain.SignForm;
import com.dandelion.domain.User;
import com.dandelion.message.Message;
import com.dandelion.repository.UserRepository;
import com.dandelion.service.CheatService;
import com.dandelion.service.UserService;
import com.dandelion.utils.AES;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "/api/v1", name = "举报API")
@Api(description = "举报API")

public class CheatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheatController.class);
	private Message message = new Message();

	private CheatService cheatService;
	private UserService userService;
	private int submitCount = 0;
	public ArrayList<Cheat> cheatList;
	public ArrayList<CheatItem> cheatItemList;
	private UserRepository userRepository;
	@Autowired
	public CheatController(UserRepository userRepository ,CheatService cheatService, UserService userService) {
		this.cheatService = cheatService;
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@ResponseBody
	@PostMapping(value = "i/cheat/create")
	@ApiOperation(value = "创建举报接口", notes = "创建举报，接口提交json格式，字段参见form参数")
	public ResponseEntity<Message> createCheats(@RequestParam List<MultipartFile> files, SignForm form,
			BindingResult bindingResult, HttpServletRequest request) {
		User user = userService.getCurrentUser();
		if (user != null) {
			try {
				String aesString = form.getSign();
				String decodeString = AES.getInstance().decrypt(aesString);
				LOGGER.warn("解密后的字串是：" + decodeString);
				JSONObject jsStr = JSONObject.fromObject(decodeString);
				LOGGER.warn("当前用户可提交次数：" + user.getSubmitcountcheat() );
				if (user.getSubmitcountcheat() > submitCount ) {
					cheatService.saveCheatList(jsStr, files, request);
					message.setMsg(1, "举报信息提交成功,请等待审核");
					user.setSubmitcountcheat(user.getSubmitcountcheat()-1);
					userRepository.save(user);
				}else {
					message.setMsg(0, "您今天已经提交过举报信息");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "cheat/getCheatList")
	public ResponseEntity<Message> getCheatsList(@RequestBody SignForm form, Pageable p, BindingResult bindingResult,
			HttpServletRequest request) {
		if (cheatList == null) {
			cheatList = new ArrayList<Cheat>();
		}
		LOGGER.warn("join..................." + p);
		try {
			String aesString = form.getSign();
			String decodeString = AES.getInstance().decrypt(aesString);
			LOGGER.warn("解密后的字串是：" + decodeString);
			JSONObject jsStr = JSONObject.fromObject(decodeString);

			Long startid = Long.parseLong(jsStr.optString("startid"));
			String key = jsStr.optString("cheatwechat");
			cheatList.clear();
			if (key.equals("null") || key == "") {
				cheatList.addAll(cheatService.getCheat());
				message.setMsg(1, "获取举报列表成功", cheatList);
			} else {
				Cheat cheat = cheatService.getCheatsListforkey(key);
				cheatList.add(cheat);
				message.setMsg(1, "查询成功", cheatList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "cheat/getList")
	public ResponseEntity<Message> getList(@RequestBody SignForm form, Pageable p, BindingResult bindingResult,
			HttpServletRequest request) {
		if (cheatItemList == null) {
			cheatItemList = new ArrayList<CheatItem>();
		}
		cheatItemList.clear();
		try {
			String aesString = form.getSign();
			String decodeString = AES.getInstance().decrypt(aesString);
			LOGGER.warn("解密后的字串是：" + decodeString);
			JSONObject jsStr = JSONObject.fromObject(decodeString);
			String key = jsStr.optString("cheatwechat");
			cheatItemList.addAll(cheatService.getCheatItemForKey(key));
			message.setMsg(1, "获取信息成功", cheatItemList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

}
