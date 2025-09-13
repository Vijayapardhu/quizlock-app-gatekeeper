package com.smartappgatekeeper.network.dto;

/**
 * Registration request DTO
 */
public class RegisterRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private int age;
    private String goal;
    private String deviceId;
    private String deviceType;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String email, String password, String confirmPassword, String name, 
                          int age, String goal, String deviceId, String deviceType) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.age = age;
        this.goal = goal;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }
    
    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
}
