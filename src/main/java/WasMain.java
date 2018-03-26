import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class WasMain {
    
    public static final String ROOT_CONTEXT = "/";
    public static final String BASE_DIR = "C:/tmp/wasroot";

    
    public static void main(String[] args) {
    
        try (ServerSocket listener = new ServerSocket(8080);) {
            
            while (true) {
                System.out.println("Client 요청 대기중");
                Socket client = listener.accept();
                System.out.println("Client 접속!!");
                
                new Thread(() ->
                    handleSocket(client)
                ).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    private static void handleSocket(Socket client) {
        try (InputStream inputStream = client.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             OutputStream outputStream = client.getOutputStream();
             PrintWriter pw = new PrintWriter(outputStream);) {
    
            HttpRequest request = parseRequestHeader(reader);
    
            String fileName = request.getPath();
            if(isRootPath(fileName)) {
                fileName = "index.html";
            }
    
            String contentType = chooseContentType(request.getPath());
            String status = "200 OK";
            File responseFile = createResponseFile(fileName);
            if(!responseFile.exists()) {
                status = "404 Not Found";
                responseFile = createResponseFile("404.html");
            }
            
            writeResponseHeaders(pw, contentType, status, responseFile.length());
    
            writeResponseBody(outputStream, responseFile);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static boolean isRootPath(String path) {
        return ROOT_CONTEXT.equals(path);
    }
    
    private static String chooseContentType(String requestPath) {
        
        if(requestPath.endsWith(".png")) {
            return "image/png";
        }
        
        return "text/html; charset=UTF-8";
    }
    
    private static File createResponseFile(String fileName) {
        if(!fileName.startsWith("/")) {
            fileName = "/" + fileName;
        }
        return new File(BASE_DIR+fileName);
    }
    
    private static void writeResponseHeaders(PrintWriter pw, String contentType, String status, long contentLength) {
        pw.println("HTTP/1.1 "+status);
        pw.println("Content-Type: " + contentType);
        pw.println("Content-Encoding: UTF-8");
        pw.println("Content-Length: " + contentLength);
        pw.println();
        pw.flush();
    }
    
    private static void writeResponseBody(OutputStream outputStream, File responseFile) throws IOException {
        if(responseFile.exists()) {
            Path filePath = responseFile.toPath();
            Files.copy(filePath, outputStream);
        }
    }
    
    private static HttpRequest parseRequestHeader(BufferedReader reader) throws IOException {
        HttpRequest request = new HttpRequest();
        
        String line = reader.readLine();
        
        request.parseFirstRequestLine(line);
        
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            request.parseRequestHeaderLine(line);
        }
        return request;
    }
    
}
