package br.com.access.api.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.access.api.model.Access;

public interface AccessRepository extends PagingAndSortingRepository<Access, Long> {

	@Query("SELECT new Access( c.ip, c.userAgent, COUNT(c) as quantidade ) FROM Access c "
			+ "GROUP BY c.ip, c.userAgent HAVING COUNT(c) > 1 ORDER BY 3 DESC ") 
	List<Access> findRequestsForIpAndUserAgent();
	

	@Query("SELECT c FROM Access c where c.ip like :ip and c.userAgent like :userAgent ")
	Page<Access> filtrarIpAndUserAgent( @Param("ip") String ip, @Param("userAgent") String userAgente, Pageable pageable);
	
	@Query("SELECT c FROM Access c where c.ip like :ip or c.userAgent like :userAgent ")
	Page<Access> filtrarIpOrUserAgent( @Param("ip") String ip, @Param("userAgent") String userAgente, Pageable pageable);
	
	@Query("SELECT c FROM Access c where c.userAgent like :userAgent ")
	Page<Access> filtrarUserAgent( @Param("userAgent") String userAgente, Pageable pageable);
	
	@Query("SELECT c FROM Access c where c.ip like :ip")
	Page<Access> filtrarIp( @Param("ip") String ip, Pageable paginacao);
}
