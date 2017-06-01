package br.com.elos.logging;

public enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARNING(2),
    ERROR(3),
    CRITICAL(4),
    EMERGENCY(5);
    
    private final int severity;
    
    LogLevel(int ref) {
        this.severity = ref;
    }
    
    public int severity() {
        return this.severity;
    }
}
