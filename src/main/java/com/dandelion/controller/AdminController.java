package com.dandelion.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.Cheat;
import com.dandelion.domain.CheatItem;
import com.dandelion.domain.Core;
import com.dandelion.domain.Material;
import com.dandelion.domain.News;
import com.dandelion.domain.User;
import com.dandelion.repository.AccountRepository;
import com.dandelion.repository.AuthorityRepository;
import com.dandelion.repository.CheatListRepository;
import com.dandelion.repository.CoreRepository;
import com.dandelion.repository.MaterialRepository;
import com.dandelion.repository.NewsRepository;
import com.dandelion.repository.ThirdpartyRepository;
import com.dandelion.repository.UserRepository;
import com.dandelion.service.AppPushService;
import com.dandelion.service.CheatService;
import com.dandelion.service.CoreServiceImpl;
import com.dandelion.service.SendSMSService;
import com.dandelion.service.UserService;
import com.dandelion.utils.CommonUtils;
import com.dandelion.utils.TimeManager;

/**
 * 后台管理控制器
 * 
 * @author qing
 *
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	private UserRepository userRepository;
	private NewsRepository newsRepository;
	private AccountRepository accountRepository;
	private CheatListRepository cheatListRepository;
	private MaterialRepository materialRepository;

	private UserService userService;
	private AuthorityRepository authorityRepository;

	private CheatService cheatService;
	private SendSMSService sendSMSService;
	private ThirdpartyRepository thirdpartyRepository;
	private AppPushService appPushService;
	private CoreServiceImpl coreService;
	private CoreRepository coreRepository;

	@Autowired
	public AdminController(NewsRepository newsRepository, AccountRepository accountRepository, MaterialRepository materialRepository, CheatListRepository cheatListRepository,
			UserRepository userRepository, AuthorityRepository authorityRepository ,UserService userService, CheatService cheatService , SendSMSService sendSMSService,
			ThirdpartyRepository thirdpartyRepository, AppPushService appPushService , CoreServiceImpl coreService , CoreRepository coreRepository) {
		this.userRepository = userRepository;
		this.newsRepository = newsRepository;
		this.authorityRepository = authorityRepository;
		this.materialRepository = materialRepository;
		this.cheatService = cheatService;
		this.accountRepository = accountRepository;
		this.cheatListRepository = cheatListRepository;
		this.userService = userService;
		this.sendSMSService = sendSMSService;
		this.thirdpartyRepository = thirdpartyRepository;
		this.appPushService = appPushService;
		this.coreService = coreService;
		this.coreRepository = coreRepository;
		initTime();
	}
	
	private void initTime() {
		AdminDataTimerTask task = new AdminDataTimerTask();
		
		new TimeManager(task);
	}
	
	public class AdminDataTimerTask extends TimerTask {
		private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		@Override
		public void run() {
			try {
				LOGGER.warn("总数量" +userRepository.count());
				for (int i = 1 ; i <= userRepository.count() ; i++){
					Long currentSum = (long) i;
					User user = userRepository.findById(currentSum);
					if (user.getRole().equals("ROLE_ADMIN")) {
						user.setSubmitcountbusin(200);
						user.setSubmitcountmoney(200);
						user.setSubmitcountrent(200);
						user.setSubmitcountmaterial(200);
						user.setSubmitcountcheat(200);
					} else if(user.getRole().equals("ROLE_USER")){
						user.setSubmitcountbusin(5);
						user.setSubmitcountmoney(5);
						user.setSubmitcountrent(5);
						user.setSubmitcountmaterial(5);
						user.setSubmitcountcheat(1);
					}
					LOGGER.warn("用户信息" + user.toString());
					userRepository.save(user);
				}
				LOGGER.warn("执行当前时间" + formatter.format(Calendar.getInstance().getTime()));
			} catch (Exception e) {
				System.out.println("-------------解析信息发生异常--------------");
			}
		}

	}

	/**
	 * 管理主界面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/")
	public String adminIndex(Model model) {
		model.addAttribute("dashboard", true);
		model.addAttribute("userscount", userRepository.count());
		model.addAttribute("newscount", newsRepository.count());
		model.addAttribute("accountcount", accountRepository.count());
		model.addAttribute("materialcount", materialRepository.count());
		model.addAttribute("accountlock", accountRepository.findByAccountforresult(3).size());
		model.addAttribute("core" , coreRepository.findOne((long) 1));
		LOGGER.info("access admin /");
		return "admin/index";
	}

	/**
	 * 用户管理
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/user")
	public String adminUser(Model model) {
		model.addAttribute("user", true);
		return "admin/user";
	}

	/**
	 * 账号管理
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/accountbusiness")
	public String adminAccountBusiness(Model model) {
		model.addAttribute("accountbusiness", true);
		Iterable<AccountBusinessCreateInfo> newslist = accountRepository.findAll();
		model.addAttribute("accountbusinesslist", newslist);
		return "admin/accountbusiness";
	}

	/**
	 * 账号交易状态修改
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/account/edit")
	public String accountEdit(Model model, @RequestParam Long id) {
		model.addAttribute("accountEdit", accountRepository.findOne(id));
		return "admin/accountEdit";
	}

	/**
	 * 账号种类筛选
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/account/updateAnthortype")
	public String updateAccountType(Model model, @RequestParam int anthortype) {
		LOGGER.warn("anthortype == " + anthortype);
		Iterable<AccountBusinessCreateInfo> accountlist = accountRepository.findByAnthortype(anthortype);
		model.addAttribute("accountbusinesslist", accountlist);
		LOGGER.warn("accountlist == " + accountlist);
		return "admin/accountbusiness";
	}

	/**
	 * 账号服务器筛选
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/account/updateaccountservice")
	public String updateService(Model model, @RequestParam int service) {
		LOGGER.warn("service == " + service);
		Iterable<AccountBusinessCreateInfo> accountlist = accountRepository.findByServer(service);
		model.addAttribute("accountbusinesslist", accountlist);
		LOGGER.warn("accountlist == " + accountlist);
		return "admin/accountbusiness";
	}

	/**
	 * 账号交易状态筛选
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/account/updateaccountstatus")
	public String updateStatus(Model model, @RequestParam int accountforresult) {
		LOGGER.warn("accountforresult == " + accountforresult);
		Iterable<AccountBusinessCreateInfo> accountlist = accountRepository.findByAccountforresult(accountforresult);
		model.addAttribute("accountbusinesslist", accountlist);
		LOGGER.warn("accountlist == " + accountlist);
		return "admin/accountbusiness";
	}

	/**
	 * 账号修改提交操作
	 * 
	 * @return
	 */
	@PostMapping("/admin/account/edit")
	public String accountSubmit(@ModelAttribute AccountBusinessCreateInfo account) {
		 LOGGER.warn("account toString == " + account.getId());
		 AccountBusinessCreateInfo accountInfo = accountRepository.findOne(account.getId());
		 if (accountInfo.getAccountforresult() == CommonUtils.RESULT_TYPE_NO) { //0  审核未通过后的账号不允许改变任何状态
			 return "redirect:/admin/accountbusiness";
		 } else if (accountInfo.getAccountforresult() == CommonUtils.RESULT_TYPE_VERIFY) { // 1  待审核 只能修改成状态为0 或者为2
			 if (account.getAccountforresult() == CommonUtils.RESULT_TYPE_LOCK ) {   
				 return "redirect:/admin/accountbusiness";
			 } 
			 if (account.getAccountforresult() == CommonUtils.RESULT_TYPE_FINISH ) {  
				 return "redirect:/admin/accountbusiness";
			 } 
		 } else if (accountInfo.getAccountforresult() == CommonUtils.RESULT_TYPE_BUSIN) { // 2 可交易状态 只能修改状态为 0 3 4 
//			 if (account.getAccountforresult() == CommonUtils.RESULT_TYPE_NO) {  // 暂时隐藏  如果客户不继续交易可以取消    
//				 return "redirect:/admin/accountbusiness";
//			 }  
//				 
			 if (account.getAccountforresult() == CommonUtils.RESULT_TYPE_VERIFY) {
				 return "redirect:/admin/accountbusiness";
			 }
			 
		 } else if (accountInfo.getAccountforresult() == CommonUtils.RESULT_TYPE_FINISH) { //4 交易完成后的账号不允许改变任何状态
			 return "redirect:/admin/accountbusiness";
		 } 
		 
		 accountInfo.setAccountforresult(account.getAccountforresult());
		 accountInfo.setReport(account.getReport()); //添加反馈信息
		 
		 accountInfo.setAccountforprice(account.getAccountforprice()); //修改价格
		 accountInfo.setAccountforpricesell((account.getAccountforprice() * 0.02) + account.getAccountforprice());
		 LOGGER.warn("修改后的买家价格  == " + account.getAccountforpricesell());
		 if (!TextUtils.isEmpty(account.getBuywechat())) {
			 accountInfo.setReport(account.getBuywechat()); //添加购买人
		 }
		 accountInfo.setReport(account.getReport()); //添加反馈信息
		 accountRepository.save(accountInfo);
//		
		return "redirect:/admin/accountbusiness";
	}
	
	/**
	 * 材料管理
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/materialManager")
	public String adminMateriaManager(Model model) {
		model.addAttribute("materials", true);
		Iterable<Material> materiallist = materialRepository.findAll();
		model.addAttribute("materiallist", materiallist);
		return "admin/material";
	}
	
	/**
	 * 材料交易状态修改
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/material/edit")
	public String materialEdit(Model model, @RequestParam Long id) {
		model.addAttribute("materialEdit", materialRepository.findOne(id));
		return "admin/materialEdit";
	}
	
	/**
	 * 材料交易状态筛选
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/material/updatematerialstatus")
	public String updatematerialStatus(Model model, @RequestParam int materialforresult) {
		LOGGER.warn("materialforresult == " + materialforresult);
		Iterable<Material> materiallist = materialRepository.findByMaterialforresult(materialforresult);
		model.addAttribute("materiallist", materiallist);
		LOGGER.warn("materiallist == " + materiallist);
		return "admin/material";
	}
	
	/**
	 * 材料修改提交操作
	 * 
	 * @return
	 */
	@PostMapping("/admin/material/sub")
	public String materialSubmit(@ModelAttribute Material material) {
		 Material materialInfo = materialRepository.findOne(material.getId());
		 
		 
		 if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_NO) { //0  审核未通过后的账号不允许改变任何状态
			 return "redirect:/admin/materialManager";
		 } else if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_VERIFY) { // 1  待审核 只能修改成状态为0 或者为2
			 if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_LOCK ) {   
				 return "redirect:/admin/materialManager";
			 } 
			 if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_FINISH ) {  
				 return "redirect:/admin/materialManager";
			 } 
		 } else if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_BUSIN) { // 2 可交易状态 只能修改状态为 0 3 4 
//			 if (account.getAccountforresult() == CommonUtils.RESULT_TYPE_NO) {  // 暂时隐藏  如果客户不继续交易可以取消    
//				 return "redirect:/admin/accountbusiness";
//			 }  
//				 
			 if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_VERIFY) {
				 return "redirect:/admin/materialManager";
			 }
			 
		 } else if (materialInfo.getMaterialforresult() == CommonUtils.RESULT_TYPE_FINISH) { //4 交易完成后的账号不允许改变任何状态
			 return "redirect:/admin/materialManager";
		 } 
		 
		 
		 materialInfo.setMaterialforresult(material.getMaterialforresult());
		 
		 materialRepository.save(materialInfo);
		return "redirect:/admin/materialManager"; // 反射的是接口
	}

	
	/**
	 * 举报审核管理
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/cheatitem")
	public String adminCheatItem(Model model) {
		model.addAttribute("cheatitem", true);
		Iterable<CheatItem> newslist = cheatListRepository.findAll();
		model.addAttribute("cheatitems", newslist);
		return "admin/cheatItem";
	}
	
	/**
	 * 举报审核修改表单
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/cheatitem/edit")
	public String adminCheatItemEdit(Model model, @RequestParam Long id) {
		model.addAttribute("cheatItemEdit", cheatListRepository.findOne(id));
		return "admin/cheatItemEdit";
	}
	
	/**
	 * 举报状态筛选
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/cheatitem/updatecheatitemstatus")
	public String updateCheatStatus(Model model, @RequestParam int status) {
		Iterable<CheatItem> cheatitemlist = cheatListRepository.findByStatus(status);
		model.addAttribute("cheatitems", cheatitemlist);
		return "admin/cheatitem";
	}
	
	
	/**
	 * 举报提交操作
	 * 
	 * @return
	 */
	@PostMapping("/admin/cheatitem/submit")
	public String cheatItemSubmit(@ModelAttribute CheatItem cheatItem) {
		 LOGGER.warn("account toString == " + cheatItem.getThumb1());
		 CheatItem currentItem = cheatListRepository.findOne(cheatItem.getId());
		 SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 currentItem.setDatetime(dateFormater.format(new Date()));
		 currentItem.setStatus(cheatItem.getStatus());
		 if (currentItem.getStatus() == 2) { // 审核通过
			 Cheat cheat = cheatService.saveCheat(currentItem);
			 currentItem.setCount(cheat.getCheatcount());
		 }
		 cheatListRepository.save(currentItem);
		return "redirect:/admin/cheatitem";  //返回到举报审核管理
	}
	
	/**
	 * 新闻管理
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/news")
	public String adminNews(Model model) {
		model.addAttribute("news", true);
		Iterable<News> newslist = newsRepository.findAll();
		model.addAttribute("newslist", newslist);
		return "admin/news";
	}

	/**
	 * 新闻增加表单
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/news/add")
	public String newsAdd(Model model) {
		model.addAttribute("newsAdd", new News());
		return "admin/newsAdd";
	}

	/**
	 * 新闻修改表单
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/news/edit")
	public String newsEdit(Model model, @RequestParam Long id) {
		model.addAttribute("newsEdit", newsRepository.findOne(id));
		return "admin/newsEdit";
	}

	/**
	 * 新闻修改提交操作
	 * 
	 * @param news
	 * @return
	 */
	@PostMapping("/admin/news/edit")
	public String newsSubmit(@ModelAttribute News news) {
		news.setDatetime(new Date());
		User user = userService.getCurrentUser();
		news.setUser(user);
		newsRepository.save(news);
		return "redirect:/admin/news";
	}

	/**
	 * 新闻删除操作
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/admin/news/del")
	public String delNews(Model model, @RequestParam Long id) {
		newsRepository.delete(id);
		return "redirect:/admin/news";
	}
	
	

	/**
	 * 后台配置管理
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/configuration")
	public String configuration(Model model) {
		return "admin/configuration";
	}
	
	/**
	 * 配置核心参数
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/coreConfig")
	public String coreConfig(Model model) {
		LOGGER.warn("Get cornConfig.getVersion == " + coreRepository.findById(1).getVersion());
		LOGGER.warn("Get cornConfig.newprice == " + coreRepository.findById(1).getNewprice());
		model.addAttribute("version", coreRepository.findById(1).getVersion());
		model.addAttribute("newprice", coreRepository.findById(1).getNewprice());
		model.addAttribute("versionprompt1", coreRepository.findById(1).getVersionprompt1());
		// 这里return的地址是resources中的结构地址
		return "admin/coreLayout";
	}
	
	/**
	 * 提交操作 核心控制台
	 * 
	 * @return
	 */
	@PostMapping("/admin/coreConfig")
	public String coreControll(@ModelAttribute Core core) {
		LOGGER.warn("Post core.getVersion == " + core.getVersion());
		LOGGER.warn("Post core.getNewprice == " + core.getNewprice());
		coreService.saveCore(core);
		return "redirect:/admin/coreConfig";
	}
	

	/**
	 * 第三方配置管理
	 * 
	 * @param model
	 * @return
	 */

	@GetMapping("/admin/thirdparty")
	public String thirdparty(Model model) {
		model.addAttribute("thirdparty", true);
		String smsUsername = null, smsPassword = null, pushAppID = null, pushAppKey = null, pushMasterSecret = null;
		try {
			smsUsername = thirdpartyRepository.findOne("smsUsername").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			smsPassword = thirdpartyRepository.findOne("smsPassword").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			pushAppID = thirdpartyRepository.findOne("pushAppID").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			pushAppKey = thirdpartyRepository.findOne("pushAppKey").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			pushMasterSecret = thirdpartyRepository.findOne("pushMasterSecret").getValue();
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!sendSMSService.checkSmsAccountStatus(smsUsername, smsPassword)) {
			model.addAttribute("error", true);// 状态不正常
		}
		if (smsUsername == null && smsPassword == null) {
			model.addAttribute("init", true);// 第一次初始化
			model.addAttribute("error", false);
		}

		if (pushAppID == null && pushAppKey == null && pushMasterSecret == null) {
			model.addAttribute("initpush", true);// 第一次初始化
		}
		model.addAttribute("smsUsername", smsUsername);
		model.addAttribute("smsPassword", smsPassword);
		model.addAttribute("pushAppID", pushAppID);
		model.addAttribute("pushAppKey", pushAppKey);
		model.addAttribute("pushMasterSecret", pushMasterSecret);
		return "admin/thirdparty";
	}

	/**
	 * 提交短信平台账号信息
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping("/admin/thirdpartysms")
	public String thirdpartysms(@RequestParam String username, @RequestParam String password) {
		sendSMSService.saveSmsConfig(username, password);
		if (sendSMSService.checkSmsAccountStatus(username, password)) {
			return "redirect:/admin/thirdparty?ok";// 账号正常
		} else {
			return "redirect:/admin/thirdparty?error";// 状态不正常
		}

	}

	/**
	 * 提交推送平台信息
	 * 
	 * @param appID
	 * @param appKey
	 * @param masterSecret
	 * @return
	 */
	@PostMapping("/admin/thirdpartypush")
	public String thirdPartyPush(@RequestParam String pushAppID, @RequestParam String pushAppKey,
			@RequestParam String pushMasterSecret) {
		appPushService.savePushConfig(pushAppID, pushAppKey, pushMasterSecret);

		return "redirect:/admin/thirdparty?pok#push";
	}

	@PostMapping("/admin/thirdpartypushtest")
	public String thirdPartyPushTest(@RequestParam String title, @RequestParam String text,
			@RequestParam String openUrl) throws IOException {
		if (appPushService.sendPushMsg(title, text, openUrl)) {
			return "redirect:/admin/thirdparty?testok#push";
		} else {
			return "redirect:/admin/thirdparty?testerror#push";
		}

	}
}
