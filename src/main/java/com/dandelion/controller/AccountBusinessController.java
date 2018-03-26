package com.dandelion.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.SignForm;
import com.dandelion.domain.User;
import com.dandelion.message.Message;
import com.dandelion.repository.AccountRepository;
import com.dandelion.repository.UserRepository;
import com.dandelion.service.AccountService;
import com.dandelion.service.UserService;
import com.dandelion.utils.AES;
import com.dandelion.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 处理用户类接口
 * 
 * @author qing
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@RequestMapping(value = "/api/v1/", name = "账号API")
@Api(description = "账号API")
public class AccountBusinessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountBusinessController.class);
	private static AccountRepository accountRespsitory;
	private AccountService accountService;
	private UserService userService;
	public int accountBusin = 0;
	public int accountMoney = 0;
	public int accountRent = 0;
	public int currentCreateBusin ;
	private int lockDelay = 1000 * 60 * 6 ;
	private UserRepository userRepository;
	public static AccountBusinessCreateInfo currentAccount ;
	private Message message = new Message();
	public boolean isSuccess;
	public List<AccountBusinessCreateInfo> accounts;
    protected static final String APP_KEY ="b6785d21dc3d87a369d6c897";
    protected static final String MASTER_SECRET = "19792e40b153d10315829d61";
    
	@Autowired
	public AccountBusinessController(UserRepository userRepository ,AccountRepository accountRespsitory, AccountService accountService,UserService userService) {
		this.accountRespsitory = accountRespsitory;
		this.userRepository = userRepository;
		this.accountService = accountService;
		this.userService = userService;
	}

	// // 装载用户认证Manager
	// @Autowired
	// protected AuthenticationManager authenticationManager;

	/**
	 * 创建账号接口
	 * 
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "i/account/createAccount", method = RequestMethod.POST)
	@ApiOperation(value = "创建账号接口", notes = "创建账号，接口POST请求，提交用户创建json")
	public ResponseEntity<Message> handleAccountCreateForm(@RequestParam List<MultipartFile> files,
			HttpServletRequest request, SignForm form, BindingResult bindingResult) {
		LOGGER.warn("join...................");
		if (!files.isEmpty()) {
			LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate username");
		}

		LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
		
		User user = userService.getCurrentUser();
		if (user != null) {
			try {
				String aesString = form.getSign();
				String decodeString = AES.getInstance().decrypt(aesString);
				LOGGER.warn("createAccount解密后的字串是：" + decodeString);
				JSONObject jsStr = JSONObject.fromObject(decodeString);

				JSONObject.toBean(jsStr, form.getClass());
				LOGGER.warn("当前类型" + jsStr.optString("anthortype"));
				
				int currentAccountType = Integer.parseInt(jsStr.optString("anthortype"));
				
				if (currentAccountType == 0) {
					LOGGER.warn("交易号当前可交易次数：" + "" + user.getSubmitcountbusin());
					if (user.getSubmitcountbusin() > accountBusin ) {
						isSuccess = accountService.createAccount(jsStr, files, request);
						if (isSuccess){
							user.setSubmitcountbusin(user.getSubmitcountbusin()-1);
							userRepository.save(user);
							message.setMsg(1, "账号创建成功");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						} else {
							message.setMsg(0, "账号创建失败");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						}
					} else {
						message.setMsg(0, "您提交的次数已达上限");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
				} else if(currentAccountType == 1){
					LOGGER.warn("金币号当前可交易次数：" + "" + user.getSubmitcountmoney());
					if (user.getSubmitcountmoney() > accountMoney) {
						isSuccess = accountService.createAccount(jsStr, files, request);
						if (isSuccess) {
							user.setSubmitcountmoney(user.getSubmitcountmoney()-1);
							userRepository.save(user);
							message.setMsg(1, "账号创建成功");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						} else {
							message.setMsg(0, "账号创建失败");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						}
					} else {
						message.setMsg(0, "您提交的次数已达上限");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
				} else if (currentAccountType == 2) {
					LOGGER.warn("租借号当前可交易次数：" + "" + user.getSubmitcountrent());
					if (user.getSubmitcountrent() > accountRent) {
						isSuccess = accountService.createAccount(jsStr, files, request);
						if (isSuccess) {
							user.setSubmitcountrent(user.getSubmitcountrent()-1);
							userRepository.save(user);
							message.setMsg(1, "账号创建成功");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						}else {
							message.setMsg(0, "账号创建失败");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						}
					} else {
						message.setMsg(0, "您提交的次数已达上限");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
				}
			} catch (DataIntegrityViolationException e) {
				LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate username", e);
				bindingResult.reject("username.exists", "username already exists");
				message.setMsg(0, "创建账号失败：账号已存在");
				return new ResponseEntity<Message>(message, HttpStatus.OK);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		message.setMsg(0, "账号创建失败");
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@GetMapping(value = "accounts/getAccountList")
	@ApiOperation(value = "获取账号列表接口", notes = "获取账号列表，接口startid默认0，下拉时根据最大值获取后续列表，可分页查询")
	public ResponseEntity<Message> getNewsList(@RequestParam(defaultValue = "0") Long startid, int serviceIndex , int vipIndex , Pageable p) {
//		User user = userService.getCurrentUser();
//		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		user.setDatetime(dateFormater.format(new Date()));
//		userRepository.save(user);
		//testSendPushWithCustomConfig();
		if (accounts == null) {
			accounts = Lists.newArrayList();
		}
		message.setMsg(1, "获取账号列表成功", accountService.getAccountList(startid, serviceIndex , vipIndex , p));
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	public static PushPayload buildPushObject_all_all_alert() {
	    return PushPayload.alertAll("有人点锁定啦");
	}
	
	public static PushPayload buildPush(){
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias("15252007007","13668159397" , "13551169829" ,"17190181105" ,"17190181607","17191206764"))
				.setNotification(Notification.alert("有人点锁定啦"))
                .build();
	}
	
	public void testSendPushWithCustomConfig() {
        ClientConfig config = ClientConfig.getInstance();
        // Setup the custom hostname
        config.setPushHostName("https://api.jpush.cn");

        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, config);

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPush();

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOGGER.info("Got result - " + result);

        } catch (APIConnectionException e) {
        	LOGGER.error("Connection error. Should retry later. ", e);

        } catch (APIRequestException e) {
        	LOGGER.error("Error response from JPush server. Should review and fix it. ", e);
        	LOGGER.info("HTTP Status: " + e.getStatus());
            LOGGER.info("Error Code: " + e.getErrorCode());
            LOGGER.info("Error Message: " + e.getErrorMessage());
            LOGGER.info("Msg ID: " + e.getMsgId());
        }
    }
	
	/**
	 * 获取个人发布的账号信息
	 * @return
	 */
	@GetMapping(value = "accounts/getPersonalList")
	@ApiOperation(value = "获取个人的账号信息", notes = "")
	public ResponseEntity<Message> getAccountsForPersonal(@RequestParam(defaultValue = "0") Long startid,  Pageable p) {
		message.setMsg(1, "获取个人列表成功", accountRespsitory.findByUserid(startid));
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	/**
	 * 锁定账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "i/account/lock", method = RequestMethod.POST)
	@ApiOperation(value = "锁定账号接口", notes = "用户点击锁定,更改账号状态")
	public ResponseEntity<Message> handleLockAccount(@Valid @RequestBody SignForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		if (userService.getCurrentUser() == null) {
			LOGGER.info("用户不存在");
			message.setMsg(0, "用户不存在");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
		User user = userService.getCurrentUser();
		if (form != null) {
			
			try {
				String dataSource = form.getSign();
				String decodeString = AES.getInstance().decrypt(dataSource);
				LOGGER.warn("lock解密后的字串是：" + decodeString);
				JSONObject jsStr = JSONObject.fromObject(decodeString);

				JSONObject.toBean(jsStr, form.getClass());
				
				//String buywechatStr = jsStr.optString("buywechat");
				String buywechatStr = user.getWeixin();
				if (buywechatStr != null && buywechatStr != ""){
					AccountBusinessCreateInfo clearAccount = accountRespsitory.findByBuywechat(buywechatStr); // 查询buywechat字段是否存在当前的购买人  如果存在则清空
					if (clearAccount != null && clearAccount.getAccountforresult() == CommonUtils.RESULT_TYPE_LOCK) {
						clearAccount.setBuywechat("");
						clearAccount.setAccountforresult(CommonUtils.RESULT_TYPE_BUSIN);
						accountRespsitory.save(clearAccount);
					}
				}
				Long currentId = jsStr.optLong("id");
				currentAccount = accountRespsitory.findOne(currentId);
				if (currentAccount != null) {
					if (currentAccount.getAccountforresult() == 3) {
						message.setMsg(0, "当前账号已被锁定,请稍后重试");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
					if (currentAccount.getAccountforresult() == 4) {
						message.setMsg(0, "此账号已经交易结束,请刷新");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
					LOGGER.warn("找到的账号" + currentAccount.getNickname());
					currentAccount.setAccountforresult(3);  //设置成已锁定
					currentAccount.setBuywechat(user.getWeixin());
					accountRespsitory.save(currentAccount);
					
					testSendPushWithCustomConfig();
					
					Timer timer = new Timer();     
			        timer.schedule(new MyTask(), lockDelay); //测试阶段   没10秒重置状态
					message.setMsg(1, "账号已经锁定,请联系服务人员");
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				} else  {
					message.setMsg(0, "没有找到账号！");
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		//message.setMsg(2, "锁定账号失败");
		return new ResponseEntity<Message>(message, HttpStatus.OK);

	}

	/**
	 * 上传用户头像
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "accounts/uploadImage", method = RequestMethod.POST)
	@ApiOperation(value = "上传用户头像接口", notes = "上传用户头像，接口采用MultipartFile上传图片文件")
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file, HttpServletRequest request) {
		message.setMsg(1, "用户上传头像成功", accountService.uploadThumb(file, request));
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	static class MyTask extends java.util.TimerTask{      
        public void run(){     
        	currentAccount.setAccountforresult(2); 
        	currentAccount.setBuywechat("");
        	accountRespsitory.save(currentAccount);
        }     
    } 
	

	 @RequestMapping("accounts/download")
	    public String downLoadFile(HttpServletRequest request, HttpServletResponse response) {
	        // 文件名可以从request中获取, 这儿为方便, 写死了
	        String fileName = "baobao.apk";
	        // String path = request.getServletContext().getRealPath("/");
	        String path = "E:/img/baobao.apk";
//	        File file = new File(path, fileName);
	        File file = new File("E:/img/baobao.apk");
	        LOGGER.warn("completeUrl " + file.getAbsolutePath());
	        if (file.exists()) {
	            // 设置强制下载打开
	        	response.setHeader("content-type", "application/octet-stream");
	        	
	        	ServletContext context = request.getServletContext();  
	        	  
	            // get MIME type of the file  
	        	String mimeType = context.getMimeType(path);  
	        	
	            if (mimeType == null) {  
	                // set to binary type if MIME mapping not found  
	                mimeType = "application/octet-stream";  
	            }  
	            
	            LOGGER.warn("lock解密后的字串是：" + mimeType);
	        	response.setContentType(mimeType);
	            //response.setContentType("application/force-download");
	         
	            response.setContentLength((int) file.length());  
	            // 文件名乱码, 使用new String() 进行反编码
	            //response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
	            
	            String headerKey = "Content-Disposition";  
	            String headerValue = String.format("attachment; filename=\"%s\"",  file.getName());  
	            response.setHeader(headerKey, headerValue);  
	      

//	            try {  
//	                InputStream myStream = new FileInputStream(file);  
//	                IOUtils.copy(myStream, response.getOutputStream());  
//	                response.flushBuffer();  
//	            } catch (IOException e) {  
//	                e.printStackTrace();  
//	            } 
	            
	            // 读取文件
	            BufferedInputStream bi = null;
	            try {
	                byte[] buffer = new byte[1024];
	                bi = new BufferedInputStream(new FileInputStream(file));
	                ServletOutputStream outputStream = response.getOutputStream();
	                int i = -1;
	                while (-1 != (i = bi.read(buffer))) {
	                    outputStream.write(buffer, 0, i);
	                }
	                return "下载成功";
	            } catch (Exception e) {
	                return "下载失败了";
	            } finally {
	                if (bi != null) {
	                    try {
	                        bi.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	            
	        } else {
	        	return "文件不存在";
	        }
			
	    }
}
