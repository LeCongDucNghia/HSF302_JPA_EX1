package fu.de200635.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("hsf302FU");

    private JPAUtil() {
    }

    public static EntityManagerFactory getEmf() {
        return EMF;
    }

    public static void close(){
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
