package com.wedasoft.simpleJavaFxApplicationBase.hibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    @SuppressWarnings("unused")
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build(); // Create registry
                MetadataSources sources = new MetadataSources(registry); // Create MetadataSources
                Metadata metadata = sources.getMetadataBuilder().build(); // Create Metadata
                sessionFactory = metadata.getSessionFactoryBuilder().build(); // Create SessionFactory
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    @SuppressWarnings("unused")
    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

}