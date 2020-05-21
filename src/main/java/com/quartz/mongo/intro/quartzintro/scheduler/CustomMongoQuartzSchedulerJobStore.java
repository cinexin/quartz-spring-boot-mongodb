package com.quartz.mongo.intro.quartzintro.scheduler;

import com.novemberain.quartz.mongodb.MongoDBJobStore;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * <p>
 * We extend the {@link MongoDBJobStore} because we need to set the custom mongo
 * db parameters. Some of the configuration comes from system properties set via
 * docker and the others come via the application.yml files we have for each
 * environment.
 * </p>
 * 
 * < These are set as part of initialization. This class is initialized by
 * {@link StdSchedulerFactory} and defined in the quartz.properties file.
 * 
 * </p>
 * 
 * @author dinuka
 *
 */


public class CustomMongoQuartzSchedulerJobStore extends MongoDBJobStore {

	private static String mongoAddresses;

	private static String userName;

	private static String password;

	private static String dbName;

	private static String host;

	private static String port;

	private static String mongoUri(String user,
								  String pass,
								  String host,
								  String port,
								  String database) {

		return user + ":" + pass + "@" + host + ":" + port + "/?authSource=" + database;
	}

	public CustomMongoQuartzSchedulerJobStore() {
		super();
		host = System.getenv("MONGODB_HOST");
		port = System.getenv("MONGODB_PORT");
		dbName = System.getenv("MONGODB_DATABASE");
		userName = System.getenv("MONGODB_USER");
		password = System.getenv("MONGODB_PASSWORD");
		initializeMongo();
		setMongoUri("mongodb://" + mongoAddresses);
		setUsername(userName);
		setPassword(password);
		setDbName(dbName);
		setMongoOptionEnableSSL(true);
		setMongoOptionSslInvalidHostNameAllowed(true);
	}


	/**
	 * <p>
	 * This method will initialize the mongo instance required by the Quartz scheduler
	 * </p>
	 *
	 */
	private static void initializeMongo() {
		mongoAddresses = mongoUri(userName, password, host, port, dbName);
	}

}
