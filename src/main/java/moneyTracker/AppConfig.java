package moneyTracker;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
public class AppConfig 
{
	@Bean
	@Scope("singleton")
	public Db database()
	{
		Db database = new Db();
		return database;
	}
}
