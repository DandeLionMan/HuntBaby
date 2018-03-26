package com.dandelion.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dandelion.domain.News;
import com.dandelion.domain.NewsCreateForm;
import com.dandelion.message.Message;
import com.dandelion.service.NewsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
//@RequestMapping(value = "/download", name = "下载API")

public class DownloadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
	private Message message = new Message();

	private NewsService newsService;

	@Autowired
	public DownloadController(NewsService newsService) {
		this.newsService = newsService;
	}

	@GetMapping("controller/download")
    public String downLoadFile(HttpServletRequest request, HttpServletResponse response) {
        // 文件名可以从request中获取, 这儿为方便, 写死了
        String fileName = "baobao.apk";
        LOGGER.warn("进入下载页：" );
        // String path = request.getServletContext().getRealPath("/");
        String path = "E:/img/baobao.apk";
//        File file = new File(path, fileName);
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
      

//            try {  
//                InputStream myStream = new FileInputStream(file);  
//                IOUtils.copy(myStream, response.getOutputStream());  
//                response.flushBuffer();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            } 
            
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
