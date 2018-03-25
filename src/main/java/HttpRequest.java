public class HttpRequest {
    
    private String method; // 요청 방식
    private String path; // 요청 주소
    private String host;
    private int contentLength;
    private String userAgent; // 브라우저 정보
    private String contentType; // 사용자가 요청한 컨턴츠 타입
    
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getContentLength() {
        return contentLength;
    }
    
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", host='" + host + '\'' +
                ", contentLength='" + contentLength + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
    
    public void parseFirstRequestLine(String line) {
        String[] firstLineArray = line.split("\\s");
        
        setMethod(firstLineArray[0]);
        setPath(firstLineArray[1]);
    }
    
    public void parseRequestHeaderLine(String line) {
        String[] headerArray = line.split(":\\s");
        switch (headerArray[0]) {
            case "Host":
                setHost(headerArray[1]);
                break;
        
            case "Content-Type":
                setContentType(headerArray[1]);
                break;
        
            case "User-Agent":
                setUserAgent(headerArray[1]);
                break;
        
            case "Content-Length":
                setContentLength(Integer.parseInt(headerArray[1]));
                break;
        }
    }
    
    
}
