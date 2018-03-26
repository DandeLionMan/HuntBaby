package com.dandelion.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dandelion.domain.AccountBusinessCreateInfo;

/**
 * 账号表Repository定义
 * 
 * @author qing
 *
 */
@RestResource(exported = false)

public interface AccountRepository extends PagingAndSortingRepository<AccountBusinessCreateInfo, Long>  {

//	Page<AccountBusinessCreateInfo> findByIdGreaterThan(Long startid, Pageable p);
	List<AccountBusinessCreateInfo> findByIdGreaterThan(Long startid, Pageable p);
	
	List<AccountBusinessCreateInfo> findByServerAndVip(int server, int vip);
	
	Collection<AccountBusinessCreateInfo> findByServer(int server);


	List<AccountBusinessCreateInfo> findByUserid(Long userid);
	
	Collection<AccountBusinessCreateInfo> findByAnthortype(int anthortype);
	
	Collection<AccountBusinessCreateInfo> findByAccountforresult(int accountforresult);
	
	AccountBusinessCreateInfo findByBuywechat(String buywechat);


}
