package ctl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.Iterator;

/**
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 12/2/20 11:19 AM
 */
@RestController
@RequestMapping("/idcws/GenericSoapPort")
public class Http2Ctl {
    @GetMapping("**")
    ResponseEntity<?> ddd(HttpServletRequest request,
                          HttpServletResponse response) {
        try {

            System.out.println("GET->" + request.getRequestURL() + "?" + request.getQueryString());

           if (request.getRequestURI().endsWith("idcws/GenericSoapPort")) {
                response.setContentType("text/xml");

                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/GenericSoapPort.xml"), response.getOutputStream());


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
                                           HttpServletResponse response,MultiPart request2) {
        try {
            System.out.println("POST->" + request.getRequestURL() + "?" + request.getQueryString());
            HttpCtl.print(request);
            HttpCtl.print(request.getInputStream());
            if (request.getRequestURI().endsWith("idcws/GenericSoapPort")) {
                response.setContentType("text/xml");
                FileCopyUtils.copy(new FileInputStream("/Users/xk/Documents/GitHub/javaHttpServer/src/main/resources/GenericSoapPortRes.xml"), response.getOutputStream());
            }

            if (request instanceof StandardMultipartHttpServletRequest) {
                StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) request;

                Iterator<String> fileNames = multipartRequest.getFileNames();

                while (fileNames.hasNext()) {
                    String name = fileNames.next();
                    MultipartFile file = multipartRequest.getFile(name);
                    FileCopyUtils.copy(file.getInputStream(), System.out);
                }
            }

                return new ResponseEntity<>(HttpStatus.OK);
            } catch(Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }

    }
