package com.dandelion.repository;

import org.springframework.data.repository.CrudRepository;

import com.dandelion.domain.Sms;

public interface SmsRepository extends CrudRepository<Sms, Long> {
	
	Sms findFirstByMobileAndVcodeOrderByExpiredDatetimeDesc(String mobile, String vcode);

}
