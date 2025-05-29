package xyz.tbvns.rpmap;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import xyz.tbvns.EZConfig;
import xyz.tbvns.rpmap.Configs.Save;

@SpringBootApplication
public class RpmapApplication {

	@SneakyThrows
	public static void main(String[] args) {
		EZConfig.setConfigFolder(new ApplicationHome(RpmapApplication.class).getDir().getPath() + "/Config");
		EZConfig.getRegisteredClasses().add(Save.class);
		EZConfig.load();

		SpringApplication.run(RpmapApplication.class, args);
	}

}
