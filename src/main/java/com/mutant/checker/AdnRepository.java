package com.mutant.checker;

import com.mutant.checker.service.dto.AdnRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdnRepository extends MongoRepository<AdnRecord, String> {
	AdnRecord findFirstByResult(Boolean result);
	
	Integer countAdnRecordsByResultEquals(Boolean result);
}
