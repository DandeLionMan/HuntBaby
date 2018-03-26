package com.dandelion.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;



/**
 * 常用的一些方法的封装
 * 
 * @author qing
 *
 */
public class CommonUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
	
	public static final int RESULT_TYPE_NO = 0; // 审核未通过
	public static final int RESULT_TYPE_VERIFY = 1; // 待审
	public static final int RESULT_TYPE_BUSIN = 2; // 可交易
	public static final int RESULT_TYPE_LOCK = 3; // 已锁定
	public static final int RESULT_TYPE_FINISH = 4; // 交易完成
	
	public static final String BASE_Img_URL = "/root/source/temp";
	
	public static final String BASE_Img_URL_Account = "/root/source/temp/account";
	public static final String BASE_Img_URL_Cheat = "/root/source/temp/cheat";
	
	public static final int SAVE_TYPE_ACCOUNT = 1; // 保存的类型 账号
	public static final int SAVE_TYPE_CHEAT = 2; // 保存的类型  ju

//	public static final String BASE_URL = "http://localhost:8080/"; // 测试地址
	
	public static final String BASE_URL = "https://www.huntfishbao.com/"; // 线上地址
	
	@Autowired
	private static Environment env;

	public static Sort basicSort() {
		return basicSort("desc", "id");
	}

	public static Sort basicSort(String orderType, String orderField) {
		Sort sort = new Sort(Sort.Direction.fromString(orderType), orderField);
		return sort;
	}
	
	public static String convertCurrentTie(){
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormater.format(new Date());
	}
	
	

	public static String getVip(int type) {
		String typeStr = "";
		switch (type) {
		case 0:
			typeStr = "VIP5";
			break;
		case 1:
			typeStr = "VIP5";
			break;
		case 2:
			typeStr = "VIP6";
			break;
		case 3:
			typeStr = "VIP7";
			break;
		case 4:
			typeStr = "VIP8";
			break;
		case 5:
			typeStr = "VIP9";
			break;
		default:
			break;
		}
		return typeStr;
	}

	public static String getService(int type) {
		String typeStr = "";
		switch (type) {
		case 0:
			typeStr = "所有平台";
			break;
		case 1:
			typeStr = "官方";
			break;
		case 2:
			typeStr = "腾讯";
			break;
		case 3:
			typeStr = "百度";
			break;
		case 4:
			typeStr = "360";
			break;
		case 5:
			typeStr = "华为";
			break;
		case 6:
			typeStr = "vivo";
			break;
		case 7:
			typeStr = "oppo";
			break;
		case 8:
			typeStr = "联想";
			break;
		case 9:
			typeStr = "三星";
			break;
		case 10:
			typeStr = "搜狗";
			break;
		case 11:
			typeStr = "九游";
			break;
		case 12:
			typeStr = "魅族";
			break;
		case 13:
			typeStr = "卓易";
			break;
		case 14:
			typeStr = "金立";
			break;
		
		default:
			break;
		}
		return typeStr;
	}
	
	public static String getMaterial(int type) {
        String materStr = "";
        switch (type) {
            case 0:
                materStr = "食人鱼号角";
                break;
            case 1:
                materStr = "狂暴";
                break;
            case 2:
                materStr = "锁定";
                break;
            case 3:
                materStr = "冰冻";
                break;
            case 4:
                materStr = "神灯";
                break;
            case 5:
                materStr = "四色材料";
                break;
            case 6:
                materStr = "机械碎片";
                break;
            case 7:
                materStr = "原石精华";
                break;
            case 8:
                materStr = "核弹";
                break;
            case 9:
                materStr = "白金弹头";
                break;
            case 10:
                materStr = "黄金弹头";
                break;
            case 11:
                materStr = "白银弹头";
                break;
            case 12:
                materStr = "青铜弹头";
                break;
            case 13:
                materStr = "时光沙漏";
                break;
            case 14:
                materStr = "时光沙漏";
                break;
        }
        return materStr;
    }

	public static String saveFile(MultipartFile file, HttpServletRequest request) {
		HashMap<String, Object> retet = new HashMap<String, Object>();
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
				// LOGGER.info("Server File Location = " +
				// serverFile.getAbsolutePath());
				System.out.print("" + "Server File Location = " + serverFile.getAbsolutePath());
				String serverPath = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
						request.getContextPath()).toString();
				retet.put("url", serverPath + "/" + relativeFile);

				// accountBusinessCreateInfo.setThumb1(relativeFile);
				// user.setImage(relativeFile);
				// userRepository.save(user);
				return relativeFile;

			} catch (Exception e) {
				String message = e.getMessage();
				if (message.contains("permitted size of")) {
					request.setAttribute("msg", "文件上传过大");
				}
				// LOGGER.info("error: {}", e);
				retet.put("url", "none");
			}
		}
		return null;
	}

	public static List<String> saveFiles(HttpServletRequest request ,List<MultipartFile> files ,  int type, boolean isCopy) {
		List<String> saveFileList = Lists.newArrayList();
		MultipartFile multfile = null;
		BufferedOutputStream stream = null;
		// 当前app根目录
		String rootPath = request.getServletContext().getRealPath("/");
		LOGGER.warn("rootPath " + rootPath);
		// 需要上传的相对地址（application.properties中获取）
		//String relativePath = env.getProperty("image.file.upload.dir");
		String relativePath = "static/upload/";
		// 文件夹是否存在，不存在就创建
		File dir = new File(rootPath + File.separator + relativePath);
		File dirCopy = null;
		if (type == SAVE_TYPE_ACCOUNT) {
			dirCopy = new File(BASE_Img_URL_Account);
		} else {
			dirCopy = new File(BASE_Img_URL_Cheat);
		}
		
		LOGGER.warn("dir " + dir);
		LOGGER.warn("dirCopy " + dirCopy);
		
		if (!dir.exists())
			dir.mkdirs();
		if (!dirCopy.exists()) {
			dirCopy.mkdirs();
		}
		for (int i = 0; i < files.size(); i++) {
			multfile = files.get(i);
			if (!multfile.isEmpty()) {
				try {
					byte[] bytes = multfile.getBytes();

					String fileExtensions = getFileExtension(multfile); // 获取后缀
					String filename = java.util.UUID.randomUUID().toString() + "." + fileExtensions;// 生成UUID样式的文件名
					// 文件全名
					String fullFilename =  dir.getAbsolutePath() + File.separator + filename;

					// 用户头像被访问路径
					String relativeFile =  relativePath + File.separator + filename;
					
					// 保存图片
					File serverSaveFile = new File(fullFilename);
					
					stream = new BufferedOutputStream(new FileOutputStream(serverSaveFile));

					stream.write(bytes);

					stream.close();
					
					String completeUrl = BASE_URL + relativeFile;
					LOGGER.warn("completeUrl " + completeUrl);
					saveFileList.add(completeUrl);
					
					if (isCopy) {
						String fullFilenameCppy = dirCopy.getAbsolutePath() + File.separator + filename;
						LOGGER.warn("fullFilenameCppy " + fullFilenameCppy);
						File serverSaveFileCopy = new File(fullFilenameCppy);
						stream = new BufferedOutputStream(new FileOutputStream(serverSaveFileCopy));
						stream.write(bytes);
						stream.close();
					}
					
				} catch (Exception e) {
					stream = null;
					System.out.print("Save Files Error ");
					return null;
				}
			} else {
				System.out.print("Save Files isEmpty ");
				return null;
			}
		}
		System.out.print("saveFileList " + saveFileList.size());
		return saveFileList;
		
		
		
		
//		List<String> saveFileList = Lists.newArrayList();
//		MultipartFile multfile = null;
//		BufferedOutputStream stream = null;
//		// 当前app根目录
//		String rootPath = request.getServletContext().getRealPath("/");
//		LOGGER.warn("rootPath " + rootPath);
//		String rootPath1 = "E:/img/";
//		// 需要上传的相对地址（application.properties中获取）
//		//String relativePath = env.getProperty("image.file.upload.dir");
//		String relativePath = "static/upload/";
//		// 文件夹是否存在，不存在就创建
//		File dir = new File(rootPath + File.separator + relativePath);
//		File dirCopy = new File(BASE_Img_URL + relativePath);
//		LOGGER.warn("dir " + dir);
//		LOGGER.warn("dirCopy " + dirCopy);
//		
//		if (!dir.exists())
//			dir.mkdirs();
//		if (!dirCopy.exists()) {
//			dirCopy.mkdirs();
//		}
//		for (int i = 0; i < files.size(); i++) {
//			multfile = files.get(i);
//			if (!multfile.isEmpty()) {
//				try {
//					byte[] bytes = multfile.getBytes();
//
//					String fileExtensions = getFileExtension(multfile); // 获取后缀
//					String filename = java.util.UUID.randomUUID().toString() + "." + fileExtensions;// 生成UUID样式的文件名
//					// 文件全名
//					String fullFilename = dir.getAbsolutePath() + File.separator + filename;
//
//					// 用户头像被访问路径
//					String relativeFile = relativePath + File.separator + filename;
//					
//					// 保存图片
//					File serverSaveFile = new File(fullFilename);
//					
//					stream = new BufferedOutputStream(new FileOutputStream(serverSaveFile));
//
//					stream.write(bytes);
//
//					stream.close();
//					
//					String completeUrl = BASE_URL + relativeFile;
//					LOGGER.warn("completeUrl " + completeUrl);
//					saveFileList.add(completeUrl);
//					
//					if (isCopy) {
//						String fullFilenameCppy = dirCopy.getAbsolutePath() + File.separator + filename;
//						LOGGER.warn("fullFilenameCppy " + fullFilenameCppy);
//						File serverSaveFileCopy = new File(fullFilenameCppy);
//						stream = new BufferedOutputStream(new FileOutputStream(serverSaveFileCopy));
//						stream.write(bytes);
//						stream.close();
//					}
//					
//				} catch (Exception e) {
//					stream = null;
//					System.out.print("Save Files Error ");
//					return null;
//				}
//			} else {
//				System.out.print("Save Files isEmpty ");
//				return null;
//			}
//		}
//		System.out.print("saveFileList " + saveFileList.size());
//		return saveFileList;
		
		
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
	
	/**
     * 文件下载
     * 
     * @return
     */
    @RequestMapping("https://www.huntfishbao.com/download")
    public String downLoadFile(HttpServletRequest request, HttpServletResponse response) {
        // 文件名可以从request中获取, 这儿为方便, 写死了
        String fileName = "baobao.apk";
        // String path = request.getServletContext().getRealPath("/");
        String path = "E:/img/";
        File file = new File(path, fileName);

        if (file.exists()) {
            // 设置强制下载打开
            response.setContentType("application/force-download");
            // 文件名乱码, 使用new String() 进行反编码
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

            // 读取文件
            BufferedInputStream bi = null;
            try {
                byte[] buffer = new byte[1024];
                bi = new BufferedInputStream(new FileInputStream(new File("")));
                ServletOutputStream outputStream = response.getOutputStream();
                int i = -1;
                while (-1 != (i = bi.read(buffer))) {
                    outputStream.write(buffer, 0, i);
                }
                return "下载成功";
            } catch (Exception e) {
                return "程序猿真不知道为什么, 反正就是下载失败了";
            } finally {
                if (bi != null) {
                    try {
                        bi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "文件不存在";
    }
	
}
