package org.intro.retojfxhib;

import org.intro.retojfxhib.dao.MovieDAO;
import org.intro.retojfxhib.dto.CopyDTO;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<CopyDTO> dtos = new MovieDAO(HibUtils.getSessionFactory()).getDtoObjOfUser(1L);
        dtos.forEach(System.out::println);
    }
}
