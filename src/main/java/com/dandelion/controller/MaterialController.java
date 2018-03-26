package com.dandelion.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dandelion.domain.Material;
import com.dandelion.domain.SignForm;
import com.dandelion.domain.User;
import com.dandelion.message.Message;
import com.dandelion.repository.AccountRepository;
import com.dandelion.repository.MaterialRepository;
import com.dandelion.repository.UserRepository;
import com.dandelion.service.AccountService;
import com.dandelion.service.MaterialService;
import com.dandelion.service.UserService;
import com.dandelion.utils.AES;
import com.dandelion.utils.CommonUtils;
import com.dandelion.utils.TimeManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 处理材料类接口
 * 
 * @author qing
 *
 */
@RestController
@PropertySource("classpath:message.properties")
@RequestMapping(value = "/api/v1/", name = "材料API")
@Api(description = "材料API")
public class MaterialController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialController.class);
	private static MaterialRepository materialRepository;
	private MaterialService materialService;
	private UserRepository userRepository;
	private UserService userService;
	public ArrayList<Material> materialList;
	public int currentCreateBusin ;
	private int submitCountMaterial = 0;
	private int lockDelay = 1000 * 60 * 6 ;
	public static Material currentMaterial ;
	private Message message = new Message();

	@Autowired
	public MaterialController(UserRepository userRepository, MaterialRepository materialRepository ,MaterialService materialService,UserService userService) {
		this.materialRepository = materialRepository;
		this.materialService = materialService;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	/**
	 * 创建材料接口
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "i/material/createMaterial", method = RequestMethod.POST)
	@ApiOperation(value = "创建材料接口", notes = "创建材料，接口POST请求，提交用户创建json")
	public ResponseEntity<Message> handleMaterialCreateForm(@Valid @RequestBody SignForm form, BindingResult bindingResult ) {
		LOGGER.warn("i/material/createMaterial......");

		User user = userService.getCurrentUser();
		LOGGER.warn("当前用户是 ： " + user.toString());
		if (user != null) {
			try {
				String aesString = form.getSign();
				String decodeString = AES.getInstance().decrypt(aesString);
				LOGGER.warn("createAccount解密后的字串是：" + decodeString);
				JSONObject convertStr = JSONObject.fromObject(decodeString);

				JSONObject.toBean(convertStr, form.getClass());
				
				int materialLabel = convertStr.optInt("materialindex");
				int count = convertStr.optInt("count");
				LOGGER.warn("当前材料提交次数" + user.getSubmitcountmaterial());
				if (user.getSubmitcountmaterial() > submitCountMaterial) {
					if (materialLabel >= 8 && materialLabel <= 12) {
						if (count < 10) {
							message.setMsg(0, "创建材料失败: 弹头数量不能低于10个");
							return new ResponseEntity<Message>(message, HttpStatus.OK);
						}
					}
					boolean isSuccess = materialService.saveMaterial(convertStr);
					if (isSuccess) {
						user.setSubmitcountmaterial(user.getSubmitcountmaterial()-1);
						userRepository.save(user);
						message.setMsg(1, "材料发布成功,请等待审核");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					} else {
						message.setMsg(0, "材料发布失败");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
				} else {
					message.setMsg(0, "您今天已经提交5次,请明天再来");
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				}
			} catch (DataIntegrityViolationException e) {
				LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate username", e);
				bindingResult.reject("username.exists", "username already exists");
				message.setMsg(0, "创建材料失败：材料已存在");
				return new ResponseEntity<Message>(message, HttpStatus.OK);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "material/getMaterialList")
	@ApiOperation(value = "获取材料列表接口", notes = "获取材料列表，接口startid默认0，下拉时根据最大值获取后续列表，可分页查询")
	public ResponseEntity<Message> getMaterialList(@RequestBody SignForm form, Pageable p, BindingResult bindingResult) {
		LOGGER.warn("material/getMaterialList......");
		if (materialList == null) {
			materialList = new ArrayList<Material>();
		}
		try {
			String aesString = form.getSign();
			String decodeString = AES.getInstance().decrypt(aesString);
			LOGGER.warn("获取材料列表 解密后的字串是：" + decodeString);
			JSONObject jsStr = JSONObject.fromObject(decodeString);

			Long startid = Long.parseLong(jsStr.optString("startid"));
			int materialLabel = jsStr.optInt("materialLabel");
			LOGGER.warn("materialLabel......" + materialLabel);
			materialList.clear();
			if (materialLabel == 100) {
				materialList = (ArrayList<Material>) materialRepository.findAll();
			} else {
				materialList = materialRepository.findByMaterialindex(materialLabel);
			}
			message.setMsg(1, "材料列表获取成功"  , materialList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	/**
	 * 材料账号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "i/material/lock", method = RequestMethod.POST)
	@ApiOperation(value = "锁定材料接口", notes = "用户点击锁定,更改材料状态")
	public ResponseEntity<Message> handleLockMaterial(@Valid @RequestBody SignForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.warn("i/material/lock......");
		if (userService.getCurrentUser() == null) {
			LOGGER.info("用户不存在");
			message.setMsg(0, "用户不存在");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
		User user = userService.getCurrentUser();
		if (form != null && user != null) {
			
			try {
				String dataSource = form.getSign();
				String decodeString = AES.getInstance().decrypt(dataSource);
				LOGGER.warn("材料lock解密后的字串是：" + decodeString);
				JSONObject jsStr = JSONObject.fromObject(decodeString);

				JSONObject.toBean(jsStr, form.getClass());
				
				String buywechatStr = user.getWeixin();
				if (buywechatStr != null && buywechatStr != ""){
					Material clearMaterial = materialRepository.findByBuywechat(buywechatStr); // 查询buywechat字段是否存在当前的购买人  如果存在则清空
					if (clearMaterial != null && clearMaterial.getMaterialforresult() == CommonUtils.RESULT_TYPE_LOCK) {
						clearMaterial.setBuywechat("");
						clearMaterial.setMaterialforresult(CommonUtils.RESULT_TYPE_BUSIN);
						materialRepository.save(clearMaterial);
					}
				}
				Long currentId = jsStr.optLong("id");
				currentMaterial = materialRepository.findOne(currentId);
				if (currentMaterial != null) {
					if (currentMaterial.getMaterialforresult() == 3) {
						message.setMsg(0, "当前账号已被锁定,请稍后重试");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
					if (currentMaterial.getMaterialforresult() == 4) {
						message.setMsg(0, "此账号已经交易结束,请刷新");
						return new ResponseEntity<Message>(message, HttpStatus.OK);
					}
					LOGGER.warn("找到的材料" + CommonUtils.getMaterial(currentMaterial.getMaterialindex()));
					currentMaterial.setMaterialforresult(3);  //设置成已锁定
					currentMaterial.setBuywechat(user.getWeixin());
					materialRepository.save(currentMaterial);
					Timer timer = new Timer();     
			        timer.schedule(new MyTask(), lockDelay); //测试阶段   没10秒重置状态
					message.setMsg(1, "账号已经锁定,请联系服务人员");
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				} else  {
					message.setMsg(0, "没有查到当前的材料！");
					return new ResponseEntity<Message>(message, HttpStatus.OK);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	static class MyTask extends java.util.TimerTask{      
        public void run(){     
        	currentMaterial.setMaterialforresult(2); 
        	currentMaterial.setBuywechat("");
        	materialRepository.save(currentMaterial);
        }     
    } 

}
