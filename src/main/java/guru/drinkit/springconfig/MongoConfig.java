package guru.drinkit.springconfig;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import guru.drinkit.repository.RepositoryPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

/**
 * Created by pkolmykov on 12/8/2014.
 */
@Configuration
@EnableMongoRepositories(basePackageClasses = RepositoryPackage.class)
public class MongoConfig extends AbstractMongoConfiguration {

    @Resource
    Environment env;

    @Override
    protected String getDatabaseName() {
        return env.getRequiredProperty("mongo.dbname");
    }

    @Override
    @Bean
    public MongoClient mongo() throws Exception {
        String port = env.getProperty("mongo.port");
        return new MongoClient(env.getRequiredProperty("mongo.host"),
                port == null ? ServerAddress.defaultPort() : Integer.valueOf(port));
    }

    @Override
    protected String getMappingBasePackage() {
        return "guru.drinkit.domain";
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String username = env.getProperty("mongo.username");
        String password = env.getProperty("mongo.password");
        if (username != null && password != null) {
            return new UserCredentials(username, password);
        }
        return super.getUserCredentials();
    }

    @Override
    protected String getAuthenticationDatabaseName() {
        return "admin";
    }
}
