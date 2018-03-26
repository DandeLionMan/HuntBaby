package com.dandelion.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.dandelion.domain.ResetPasswordForm;
import com.dandelion.domain.User;
import com.dandelion.domain.UserCreateForm;

/**
 * 用户服务网接口定义
 * 
 * @author jiekechoo
 *
 */
public interface UserService {

	User create(UserCreateForm form);

	User getUserByUsername(String username);

	Object uploadImage(MultipartFile file, HttpServletRequest request);

	User getCurrentUser();

	String getCurrentUsername();

	Object listAllUsers(Pageable p);

	Object getUserList(int current, int rowCount, String searchPhrase);

	User resetPassword(ResetPasswordForm form);
	
}
