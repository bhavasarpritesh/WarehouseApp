package in.bbd.pritesh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="docs_tab")
public class Document {
	@Id
	//@GeneratedValue
	@Column(name="doc_id")
	private Integer docId;
	@Column(name="doc_name")
	private String docName;
	@Column(name="doc_data")
	@Lob // byte[] + @Lob   = BLOB
	private byte[] docData;
		
}
