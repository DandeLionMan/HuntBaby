package com.dandelion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.ResetPasswordForm;
import com.dandelion.domain.SignForm;
import com.dandelion.domain.User;
import com.dandelion.domain.UserCreateForm;
import com.dandelion.message.Message;
import com.dandelion.repository.AccountRepository;
import com.dandelion.repository.UserRepository;
import com.dandelion.service.SendSMSService;
import com.dandelion.service.UserService;
import com.dandelion.utils.AES;
import com.dandelion.utils.CommonUtils;
import com.dandelion.utils.TimeManager;
import com.dandelion.validator.UserCreateFormValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 处理用户类接口
 * 
 * @author jiekechoo
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@RequestMapping(value = "/api/v1/", name = "用户API")
@Api(description = "用户API")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	private UserCreateFormValidator userCreateFormValidator;
	private UserRepository userRepository;
	private AccountRepository accountRepository;
	private SendSMSService sendSMSService;

	private Message message = new Message();

	@Autowired
	public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator,AccountRepository accountRepository ,
			UserRepository userRepository, SendSMSService sendSMSService) {
		this.userService = userService;
		this.accountRepository = accountRepository;
		this.userCreateFormValidator = userCreateFormValidator;
		this.userRepository = userRepository;
		this.sendSMSService = sendSMSService;
	}

	

	// 装载用户认证Manager
	@Autowired
	protected AuthenticationManager authenticationManager;

	/**
	 * 创建用户验证表单
	 * 
	 * @param binder
	 */
	@InitBinder("userCreateForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userCreateFormValidator);
	}

	/**
	 * APP登录用接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "i/userLogin", method = RequestMethod.POST)
	@ApiOperation(value = "用户登录接口", notes = "用户登录，接口POST请求，采用HttpBasic认证方式")
	public ResponseEntity<Message> userLogin(HttpServletRequest request) {
		User user = userService.getCurrentUser();
		if (user == null) {
			message.setMsg(0, "用户登录失败");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		} else {
			message.setMsg(1, "用户登录成功", user);
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}

	}

	/**
	 * 创建用户接口
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ApiOperation(value = "创建用户接口", notes = "创建用户，接口POST请求，提交用户创建json  @Valid")
	public ResponseEntity<Message> handleUserCreateForm(@Valid @RequestBody UserCreateForm form,
			BindingResult bindingResult, HttpServletRequest request) {

		LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
			if (form != null) {

			String aesString = form.getSign();
			String decodeString = null;
			try {
				decodeString = AES.getInstance().decrypt(aesString);
				LOGGER.warn("解密后的字串是：" + decodeString);
				JSONObject jsStr = JSONObject.fromObject(decodeString);
				User currentUser = userService.getUserByUsername(jsStr.optString("username"));
				LOGGER.warn("当前用户名：" + jsStr.optString("username"));
				form.setUsername(jsStr.optString("username"));
				form.setPassword(jsStr.optString("password"));
				if (currentUser == null) {
					try {
						userService.create(form);
					} catch (DataIntegrityViolationException e) {
						LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate username", e);
						bindingResult.reject("username.exists", "username already exists");
						message.setMsg(0, "创建用户失败：用户名已存在");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
					// 编码
					//User current = userService.getCurrentUser();
					User current = userService.getUserByUsername(jsStr.optString("username"));
					if (current != null) {
						LOGGER.warn("currentUSer：" + current);
						JSONObject json = JSONObject.fromObject(current);
						LOGGER.warn("json：" + json.toString());
						String endString = AES.getInstance().encrypt(json.toString());
						message.setMsg(1, "用户创建成功", endString);
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
					message.setMsg(0, "用户创建失败");
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				} else {
					SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					currentUser.setDatetime(dateFormater.format(new Date()));
					userRepository.save(currentUser);
					
					JSONObject json = JSONObject.fromObject(currentUser);
					String endString = AES.getInstance().encrypt(json.toString());
					
					message.setMsg(1, "用户登陆成功", endString);
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	List<AccountBusinessCreateInfo> accounts;

	@ResponseBody
	@RequestMapping(value = "i/user/submitWeixin", method = RequestMethod.POST)
	@ApiOperation(value = "设置微信接口", notes = "设置微信，接口POST请求，参数为SignForm类")
	public ResponseEntity<Message> resetPassword(@Valid @RequestBody SignForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		if (userService.getCurrentUser() == null) {
			LOGGER.info("用户不存在");
			message.setMsg(0, "用户不存在");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
		if (form != null) {
			try {
				String aesString = form.getSign();
				String decode = AES.getInstance().decrypt(aesString);
				LOGGER.warn("解密后的字串是：" + decode);
				JSONObject jsStr = JSONObject.fromObject(decode);
				String currentUserWeixin = jsStr.optString("weixin");
				User user = userService.getCurrentUser();
				user.setDatetime(CommonUtils.convertCurrentTie());
				user.setWeixin(currentUserWeixin);
				userRepository.save(user);
				if (accounts == null) {
					accounts = Lists.newArrayList();
				}
				LOGGER.warn("当前用户的ID：" + user.getId());
				accounts.clear();
				accounts = accountRepository.findByUserid(user.getId());
				LOGGER.warn("账号：" + accounts.size());
				if (accounts.size() > 0) {
					for (int i = 0 ; i < accounts.size() ; i++) {
						accounts.get(i).setWechat(user.getWeixin());
						accountRepository.save(accounts);
					}
				}
				JSONObject json = JSONObject.fromObject(user);
				String endString = AES.getInstance().encrypt(json.toString());
				message.setMsg(1, "用户设置微信成功", endString);
				return new ResponseEntity<Message>(message, HttpStatus.OK);
			} catch (DataIntegrityViolationException e) {
				LOGGER.warn("数据提交错误，请遵守验证规则", e);
				message.setMsg(2, "数据提交错误，请遵守验证规则");
				return new ResponseEntity<Message>(message, HttpStatus.OK);
			} catch (Exception e) {
				LOGGER.warn("数据提交错误，", e);
				message.setMsg(2, "异常"  );
				return new ResponseEntity<Message>(message, HttpStatus.OK);
			}
		}
		message.setMsg(2, "重置微信失败");
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}



	@RequestMapping(value = "i/user/resetPassword", method = RequestMethod.POST)
	@ApiOperation(value = "重置密码接口", notes = "重置密码，接口POST请求，参数为ResetPasswordForm类")
	public ResponseEntity<Message> resetPassword(@Valid @RequestBody ResetPasswordForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
		if (userService.getUserByUsername(form.getMobile()) == null) {
			LOGGER.info("用户不存在");
			message.setMsg(0, "用户不存在");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
		if (sendSMSService.findByUsernameAndVcode(form.getMobile(), form.getVcode()) == null) {
			LOGGER.info("验证码错误，或找不到");
			message.setMsg(0, "验证码错误或过期");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}

		try {
			User user = userService.resetPassword(form);
			authenticateUserAndSetSession(form.getMobile(), form.getPassword(), request);
			message.setMsg(1, "用户修改、重置密码成功", user);
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			LOGGER.warn("数据提交错误，请遵守验证规则", e);
			message.setMsg(2, "数据提交错误，请遵守验证规则");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}

	}

	/**
	 * 使用 ResponseBody作为结果 200
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "i/user/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户信息接口", notes = "获取用户信息，接口GET请求，{id}参数为用户id号")
	public ResponseEntity<Message> findByUserId(@PathVariable long id) {
		User user = userRepository.findOne(id);
		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

		if (user == null) {
			message.setMsg(0, "未找到用户");
		} else {
			message.setMsg(1, "用户信息", user);
		}
		return new ResponseEntity<Message>(message, status);
	}

	/**
	 * 上传用户头像
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "i/uploadImage", method = RequestMethod.POST)
	@ApiOperation(value = "上传用户头像接口", notes = "上传用户头像，接口采用MultipartFile上传图片文件")
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file, HttpServletRequest request) {
		message.setMsg(1, "用户上传头像成功", userService.uploadImage(file, request));
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}

	/**
	 * 获取用户列表
	 * 
	 * @param current
	 * @param rowCount
	 * @param searchPhrase
	 * @return
	 */
	@RequestMapping(value = "getUserList", method = RequestMethod.POST)
	public Object getUserList(@RequestParam(required = false) int current, @RequestParam(required = false) int rowCount,
			@RequestParam(required = false) String searchPhrase) {
		return userService.getUserList(current, rowCount, searchPhrase);

	}

	/**
	 * 自动登录，获得session和x-auth-token
	 * 
	 * @param username
	 * @param password
	 * @param request
	 */
	private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

		// generate session if one doesn't exist
		request.getSession();

		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authenticatedUser = authenticationManager.authenticate(token);
		LOGGER.info("Auto login with {}", authenticatedUser.getPrincipal());

		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	}

}
