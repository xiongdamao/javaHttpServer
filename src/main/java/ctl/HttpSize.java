package ctl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 12/7/20 3:34 PM
 */
@RestController
@RequestMapping("/size")
public class HttpSize {
    @GetMapping("**")
    ResponseEntity<?> ddd(HttpServletRequest request,
                          HttpServletResponse response) {
        byte[] bytes = new byte[1];
        bytes[0] = 86;
        try {
            Integer size = Integer.valueOf(request.getParameter("size"));
            for (int i = 0; i < size; i++) {
                response.getOutputStream().write(bytes);
            }
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
