package engine.blog.testutils;


public enum HttpMethod {

    GET("GET"), POST("POST"), DELETE("DELETE");

    private String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
