package com.mutant.checker.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "adnRecords")
public class AdnRecord {
	@Id
	private String id;
	@Indexed(unique = true)
	private String adnMatrix;
	private Boolean result;
	private Date createdAt;
	private Date modifiedAt;
}
