package org.intro.retojfxhib;

import org.intro.retojfxhib.utils.Util;
import org.mindrot.jbcrypt.BCrypt;

public class Main {
    public static void main(String[] args) {
        String randomToken = Util.randomRegisterCode(20);
        System.out.println(randomToken);
        System.out.println(BCrypt.hashpw(randomToken, BCrypt.gensalt()));
    }
}
