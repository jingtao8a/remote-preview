package org.jingtao8a.remote_preview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"org.jingtao8a.remote_preview"})
@MapperScan(basePackages = {"org.jingtao8a.remote_preview.mapper"})
@EnableTransactionManagement
public class RemotePreviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemotePreviewApplication.class, args);
	}

}
