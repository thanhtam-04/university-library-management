package thuvien.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // Lấy đường dẫn tuyệt đối đến thư mục static/uploads trong project
	    String userDir = System.getProperty("user.dir");
	    String uploadPath = "file:" + userDir + "/src/main/resources/static/uploads/";

	    registry.addResourceHandler("/uploads/**")
	            .addResourceLocations(uploadPath)
	            .setCachePeriod(0); // Quan trọng: Tắt cache để ảnh hiện ngay
	}
}