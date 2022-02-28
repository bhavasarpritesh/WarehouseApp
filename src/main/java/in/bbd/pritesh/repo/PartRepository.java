package in.bbd.pritesh.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.bbd.pritesh.model.Part;

public interface PartRepository extends JpaRepository<Part, Integer>{

	@Query("SELECT p.id,p.partCode FROM Part p")
	List<Object[]> getPartIdAndCode();
}
