package ctl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 12/2/20 11:19 AM
 */
@RestController
@RequestMapping("/test")
public class HttpCtl {
    @GetMapping("**")
    ResponseEntity<?> ddd(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            print(request);
            System.out.println("GET->" + request.getRequestURL() + "?" + request.getQueryString());

            if (request.getRequestURI().endsWith("soa-infra/PublicEvent/catalog")) {

                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/PublicEventCatalog.json"), response.getOutputStream());
            } else if (request.getRequestURI().endsWith("helpPortalApi/otherResources/latest/interfaceCatalogs")) {
                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/interfaceCatalogs.json"), response.getOutputStream());
            } else if (request.getRequestURI().endsWith("fndAppCoreServices/ServiceCatalogService")) {
                response.setContentType("text/xml");

                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/ServiceCatalogService.xml"), response.getOutputStream());
            } else if (request.getRequestURI().endsWith("fscmService/ErpIntegrationService")) {
                response.setContentType("text/xml");

                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/ErpIntegrationService.xml"), response.getOutputStream());


            } else {
                System.out.println("dddd");
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("**")
    public @ResponseBody
    ResponseEntity<?> downloadOriginalFile(HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            System.out.println("POST->" + request.getRequestURL() + "?" + request.getQueryString());
            print(request);

            print(request.getInputStream());
            if (request.getRequestURI().endsWith("fscmService/ServiceCatalogService")) {
                response.setContentType("text/xml");
                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/ServiceCatalogServiceGetAllServiceEndPoints.xml"), response.getOutputStream());
            }

            if (request instanceof StandardMultipartHttpServletRequest) {
                StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) request;

                Iterator<String> fileNames = multipartRequest.getFileNames();

                while (fileNames.hasNext()) {
                    String name = fileNames.next();
                    MultipartFile file = multipartRequest.getFile(name);
                    print(file.getInputStream());
                }
            }


//            for (Part part : request.getParts()) {
//                FileCopyUtils.copy(part.getInputStream(), System.out);
//            }
            System.out.print("\n\n\n\n\n\n\n");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("\n\n\n\n\n\n\n");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public static void print(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("Http Header ");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + "->" + request.getHeader(headerName));
        }
    }

    public static void print(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.ready()) {
                String line = reader.readLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
