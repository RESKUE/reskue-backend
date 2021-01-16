package kueres.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import kueres.utility.Utility;

@Repository
public class CustomFileSystemRepository implements FileSystemRepository {

	@Value("${media.dir}")
	private String MEDIA_DIR;
	
	public String save(long id, byte[] content) throws IOException {
		
		Utility.LOG.info(MEDIA_DIR);
		Utility.LOG.info("id: {}, content: {}", id, content.length);
		
		Path newFile = Paths.get(MEDIA_DIR + new Date().getTime() + "-CUSTOM-" + id);
		Files.createDirectories(newFile.getParent());
		Files.write(newFile, content);
		return newFile.toAbsolutePath().toString();
		
	}
	
	public FileSystemResource findByLocation(String location) {
	    try {
	        return new FileSystemResource(Paths.get(location));
	    } catch (Exception e) {
	        throw new ResourceNotFoundException(location + " not found in filesystem");
	    }
	}
	
	@Bean
	public boolean set(MediaService mediaService, CustomFileSystemRepository customFileSystemRepository) {
		mediaService.setFileSystemRepository(customFileSystemRepository);
		return true;
	}
	
}