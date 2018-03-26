package com.dandelion.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.Core;
import com.dandelion.domain.SignForm;
import com.dandelion.domain.User;
import com.dandelion.message.Message;
import com.dandelion.repository.AccountRepository;
import com.dandelion.repository.CoreRepository;
import com.dandelion.repository.UserRepository;
import com.dandelion.service.AccountService;
import com.dandelion.service.UserService;
import com.dandelion.utils.AES;
import com.dandelion.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 核心接口
 * 
 * @author qing
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@RequestMapping(value = "/api/v1/", name = "核心功能API")
@Api(description = "核心功能API")
public class CoreController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreController.class);
	private static AccountRepository accountRespsitory;
	private AccountService accountService;
	private CoreRepository coreRepository;
	private UserService userService;
	private UserRepository userRepository;
	public static AccountBusinessCreateInfo currentAccount ;
	private Message message = new Message();
    
	@Autowired
	public CoreController(CoreRepository coreRepository1 ,UserRepository userRepository ,AccountRepository accountRespsitory, AccountService accountService,UserService userService) {
		this.coreRepository = coreRepository1;
		this.userRepository = userRepository;
		this.accountRespsitory = accountRespsitory;
		this.accountService = accountService;
		this.userService = userService;
	}

	@ResponseBody
	@RequestMapping(value = "i/main/core", method = RequestMethod.POST)
	@ApiOperation(value = "设置核心接口", notes = "参数为SignForm类")
	public ResponseEntity<Message> requestMainCore(@Valid @RequestBody SignForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		if (userService.getCurrentUser() == null) {
			LOGGER.info("用户不存在");
			message.setMsg(0, "用户不存在");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
		if (form != null) {
			try{
				String aesString = form.getSign();
				String decode = AES.getInstance().decrypt(aesString);
				LOGGER.warn("解密后的字串是：" + decode);
				JSONObject jsStr = JSONObject.fromObject(decode);
				User user = userService.getCurrentUser();
				user.setDatetime(CommonUtils.convertCurrentTie());
				Core core = coreRepository.findById(1);
				JSONObject json = JSONObject.fromObject(user);
				String endString = AES.getInstance().encrypt(json.toString());
				message.setMsg(1, "成功", endString);
			} catch (Exception e) {
				LOGGER.warn("数据提交错误，", e);
				message.setMsg(2, "异常"  );
				return new ResponseEntity<Message>(message, HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}

	
	
	
	
	
}
