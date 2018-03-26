package com.dandelion.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import net.sf.json.JSONObject;
import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.User;
import com.dandelion.repository.AccountRepository;
import com.dandelion.repository.AuthorityRepository;
import com.dandelion.utils.CommonUtils;

/**
 * 用户服务接口实现
 * 
 * @author king
 *
 */
@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	private final AuthorityRepository authorityRepository;
	private final AccountRepository accountRepository;
	public List<AccountBusinessCreateInfo> accounts;
	private UserService userService;
	@Autowired
	private Environment env;

	/**
	 * 装载userRepository
	 * 
	 * @param userRepository
	 */
	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository, AuthorityRepository authorityRepository,
			UserService userService) {
		this.accountRepository = accountRepository;
		this.authorityRepository = authorityRepository;
		this.userService = userService;
	}

	@Override
	public boolean createAccount(JSONObject accountCreateForm, List<MultipartFile> files,
			HttpServletRequest request) {
		User user = userService.getCurrentUser();

		LOGGER.warn("getCurrentUsername " + userService.getCurrentUsername());
		if (user == null) {
			LOGGER.warn("current user null");
		}
		
		try{
			if (user != null) {
				AccountBusinessCreateInfo accountBusinessCreateInfo = new AccountBusinessCreateInfo();
				accountBusinessCreateInfo.setUser(user);
				accountBusinessCreateInfo.setUserid(user.getId());
				accountBusinessCreateInfo.setPlatform(1);
				accountBusinessCreateInfo.setWechat(user.getWeixin());
				accountBusinessCreateInfo.setNickname(accountCreateForm.optString("nickname"));
				accountBusinessCreateInfo.setAccountforprice(accountCreateForm.optDouble("accountforprice"));
				accountBusinessCreateInfo.setAccountforpricesell(accountCreateForm.optDouble("accountforpricesell"));
				accountBusinessCreateInfo.setAccountforpricebuy(accountCreateForm.optDouble("accountforpricebuy"));
				accountBusinessCreateInfo.setServer(Integer.parseInt(accountCreateForm.optString("server")));
				accountBusinessCreateInfo.setAnthortype(Integer.parseInt(accountCreateForm.optString("anthortype")));
				accountBusinessCreateInfo.setRecharge(Integer.parseInt(accountCreateForm.optString("recharge")));

				accountBusinessCreateInfo.setOrdnance(Integer.parseInt(accountCreateForm.optString("ordnance")));
				if (accountCreateForm.optString("macordnance").equals("") || accountCreateForm.optString("macordnance") == null) {
					accountBusinessCreateInfo.setMacordnance(0);
				} else {
					accountBusinessCreateInfo.setMacordnance(Integer.parseInt(accountCreateForm.optString("macordnance")));
				}
				

				accountBusinessCreateInfo.setVip(accountCreateForm.optInt("vip"));
				accountBusinessCreateInfo.setLevel(Integer.parseInt(accountCreateForm.optString("level")));
				accountBusinessCreateInfo.setIsconvert(Integer.parseInt(accountCreateForm.optString("isconvert")));
				accountBusinessCreateInfo.setIshat(Integer.parseInt(accountCreateForm.optString("ishat")));
				accountBusinessCreateInfo.setGoldmoney(Integer.parseInt(accountCreateForm.optString("goldmoney")));
				accountBusinessCreateInfo.setBackmoney(Integer.parseInt(accountCreateForm.optString("backmoney")));
				accountBusinessCreateInfo.setRedress(Integer.parseInt(accountCreateForm.optString("redress")));
				accountBusinessCreateInfo.setDiamond(Integer.parseInt(accountCreateForm.optString("diamond")));
				if (accountCreateForm.optString("redpackage").equals("") || accountCreateForm.optString("redpackage") == null) {
					accountBusinessCreateInfo.setRedpackage(0);
				} else {
					accountBusinessCreateInfo.setRedpackage(Integer.parseInt(accountCreateForm.optString("redpackage")));
				}
				
				accountBusinessCreateInfo.setDetail(accountCreateForm.optString("detail"));
				SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				accountBusinessCreateInfo.setDatetime(dateFormater.format(new Date()));
				accountBusinessCreateInfo.setAccountforresult(Integer.parseInt(accountCreateForm.optString("accountforresult")));
				LOGGER.warn("准备保存图片   " + accountBusinessCreateInfo.getId());
				List<String> listFiles = CommonUtils.saveFiles(request, files , CommonUtils.SAVE_TYPE_ACCOUNT ,true);
				for (int i = 0; i < listFiles.size(); i++) {
					switch (i) {
						case 0:
							accountBusinessCreateInfo.setThumb1(listFiles.get(0));
							break;
						case 1:
							accountBusinessCreateInfo.setThumb2(listFiles.get(1));
							break;
						case 2:
							accountBusinessCreateInfo.setThumb3(listFiles.get(2));
							break;
						case 3:
							accountBusinessCreateInfo.setThumb4(listFiles.get(3));
							break;
						case 4:
							accountBusinessCreateInfo.setThumb5(listFiles.get(4));
							break;
					}
				}
				accountRepository.save(accountBusinessCreateInfo);
				return true;
			}
			return false;
		} catch(Exception e){
			return false;
		}

	}

	@Override
	public List<AccountBusinessCreateInfo> getAccountList(Long startid, int serviceIndex, int vipIndex, Pageable p) {
		// Page<AccountBusinessCreateInfo> accounts =
		// accountRepository.findByIdGreaterThan(startid, p);
		// List<AccountBusinessCreateInfo> accounts = Lists.newArrayList();
		// accounts.addAll(accountRepository.findByServerAndVip(serviceIndex,
		// 3));

		if (accounts == null) {
			accounts = Lists.newArrayList();
		}
		List<AccountBusinessCreateInfo> accountSave = Lists.newArrayList();
		accounts.clear();
		if (serviceIndex == 100 && vipIndex == 100) {
			accountSave.addAll(accountRepository.findByIdGreaterThan(startid, p));
		} else {
			accountSave.addAll(accountRepository.findByServerAndVip(serviceIndex, vipIndex));
		}
		
		for (int i = 0 ; i < accountSave.size() ; i++) {
			if (accountSave.get(0).getAccountforresult() == CommonUtils.RESULT_TYPE_BUSIN || accountSave.get(0).getAccountforresult() == CommonUtils.RESULT_TYPE_LOCK) {
				accounts.add(accountSave.get(i));
			}
		}
		
		LOGGER.info("size", "" + accounts.size());
		return accounts;
	}

	/**
	 * 用户上传头像
	 */

	@Override
	public Object uploadThumb(MultipartFile file, HttpServletRequest request) {
		// User user = getCurrentUser();
		HashMap<String, Object> ret = new HashMap<String, Object>();
		if (file != null) {
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();

					// 当前app根目录
					String rootPath = request.getServletContext().getRealPath("/");

					// 需要上传的相对地址（application.properties中获取）
					String relativePath = env.getProperty("image.file.upload.dir");

					// 文件夹是否存在，不存在就创建
					File dir = new File(rootPath + File.separator + relativePath);
					if (!dir.exists())
						dir.mkdirs();
					String fileExtension = getFileExtension(file);

					// 生成UUID样式的文件名
					String filename = java.util.UUID.randomUUID().toString() + "." + fileExtension;

					// 文件全名
					String fullFilename = dir.getAbsolutePath() + File.separator + filename;

					// 用户头像被访问路径
					String relativeFile = relativePath + File.separator + filename;

					// 保存图片
					File serverFile = new File(fullFilename);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
					LOGGER.info("Server File Location = " + serverFile.getAbsolutePath());

					String serverPath = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
							request.getContextPath()).toString();
					ret.put("url", serverPath + "/" + relativeFile);

					// user.setImage(relativeFile);
					// userRepository.save(user);

				} catch (Exception e) {
					LOGGER.info("error: {}", e);
					ret.put("url", "none");
				}
			}
		}
		return null;
	}

	/**
	 * 返回文件后缀名，如果有的话
	 */
	public static String getFileExtension(MultipartFile file) {
		if (file == null) {
			return null;
		}

		String name = file.getOriginalFilename();
		int extIndex = name.lastIndexOf(".");

		if (extIndex == -1) {
			return "";
		} else {
			return name.substring(extIndex + 1);
		}
	}

}
