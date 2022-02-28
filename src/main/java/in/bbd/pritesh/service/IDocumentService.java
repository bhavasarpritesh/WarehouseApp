package in.bbd.pritesh.service;

import java.util.List;
import java.util.Optional;

import in.bbd.pritesh.model.Document;

public interface IDocumentService {

	void saveDocument(Document document);
	List<Object[]> getDocumentIdAndNames();
	Optional<Document> getOneDocument(Integer id);
}
