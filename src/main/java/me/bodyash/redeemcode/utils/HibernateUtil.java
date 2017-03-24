package me.bodyash.redeemcode.utils;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import me.bodyash.redeemcode.dao.Code;

public class HibernateUtil {
	private SessionFactory sessionFactory;
	private ConfigUtil config;
	public HibernateUtil(ConfigUtil config) {
		this.config = config;
	}
	public SessionFactory createSessionFactory() {
		try {
			Properties prop= new Properties();
			prop.setProperty("hibernate.connection.url", "jdbc:mysql://" + config.getDbAdress() + ":" + config.getDbPort() + "/" + config.getDbDatabase());
			prop.setProperty("hibernate.connection.username", config.getDbUser());
			prop.setProperty("hibernate.connection.password", config.getDbPassword());
			prop.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
			
			this.sessionFactory = new AnnotationConfiguration()
		   .addPackage("com.concretepage.persistence")
				   .addProperties(prop)
				   .addAnnotatedClass(Code.class)
				   .buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
		return sessionFactory;
	}
}