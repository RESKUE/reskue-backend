package kueres.media;

import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public interface FileSystemRepository {

	public String save(long id, byte[] content) throws IOException;
	public FileSystemResource findByLocation(String location) throws ResourceNotFoundException;
	
}
