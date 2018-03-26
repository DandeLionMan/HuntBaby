package com.dandelion.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dandelion.domain.AccountBusinessCreateInfo;
import com.dandelion.domain.Material;
import com.dandelion.domain.User;

/**
 * 材料表Repository定义
 * 
 * @author qing
 *
 */
@RestResource(exported = false)

public interface MaterialRepository extends PagingAndSortingRepository<Material, Long>  {
	
	ArrayList<Material> findByMaterialindex (int index );

	Material findByBuywechat (String buywechat);
	
	Collection<Material> findByMaterialforresult(int materialforresult);
}
