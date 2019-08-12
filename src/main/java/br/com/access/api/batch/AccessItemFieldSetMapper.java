

package br.com.access.api.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import br.com.access.api.model.Access;


@Component
public class AccessItemFieldSetMapper implements FieldSetMapper<Access>{

	@Override
	public Access mapFieldSet(FieldSet fieldSet) throws BindException {
		Access access = new Access();
		access.setIp(fieldSet.readString("ip"));
		access.setRequest(fieldSet.readString("request"));
		access.setStatus(Integer.valueOf(fieldSet.readString("status")));
		access.setUserAgent(fieldSet.readString("userAgent"));
		access.setData(fieldSet.readDate("data", "yyyy-MM-dd HH:mm:ss.SSS"));
		return access;
	}




}
