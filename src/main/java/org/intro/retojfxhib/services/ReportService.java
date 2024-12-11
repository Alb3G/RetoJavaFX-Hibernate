package org.intro.retojfxhib.services;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.intro.retojfxhib.SessionManager;
import org.intro.retojfxhib.dto.CopyDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class ReportService {
    private static Connection conn;
    private static final HashMap params = new HashMap<>();

    static {
        try {
            conn = DriverManager.getConnection(
                    System.getenv("DB_URL"),
                    System.getenv("HIBERNATE_USER"),
                    System.getenv("HIBERNATE_PASSWORD")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateReportForCopy(CopyDTO copyDTO) throws FileNotFoundException {
        params.put("userId", SessionManager.getInstance().getCurrentUser().getId());
        params.put("movieId", copyDTO.getMovie().getId());
        params.put("movieCopyId", copyDTO.getCopy().getCopyId());


        try {
            JasperPrint jp = JasperFillManager.fillReport("CopyInfoReport.jasper", params, conn);
            JasperExportManager.exportReportToPdfFile(jp, "reports/" + copyDTO.getTitle() + ".pdf");
//            File report = new File("reports/" + copyDTO.getMovie().getTitle() + ".pdf");
//            if(!report.exists())
//                throw  new FileNotFoundException("El archivo pdf no se ha encontrado");
//
//            new Thread(new MailReportService(SessionManager.getInstance().getCurrentUser().getEmail(), report)).start();
//
////            if(report.delete())
////                System.out.println("Archivo eliminado despues de enviarlo!");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
