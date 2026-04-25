/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Users;

import java.time.LocalDate;

/**
 *
 * @author nasry
 */
public class Usuario {

    private String username;
    private String password;
    private int puntos;
    private LocalDate fechaIngreso;
    private boolean activo;

    private String[] logs;
    private int logIndex;

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.fechaIngreso = LocalDate.now();
        this.activo = true;

        this.logs = new String[10];
        this.logIndex = 0;
    }

    // ===== GETTERS =====
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPuntos() {
        return puntos;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    // ===== SETTERS =====
    public void setPassword(String password) {
        this.password = password;
    }

    public void desactivar() {
        this.activo = false;
    }

    // ===== PUNTOS =====
    public void agregarPuntos(int pts) {
        puntos += pts;
    }

    // ===== LOGS (últimos 10) =====
    public void registrarLog(String log) {
        logs[logIndex] = log;
        logIndex = (logIndex + 1) % 10;
    }

    public String[] getLogs() {
        String[] resultado = new String[10];
        int k = 0;

        for (int i = logIndex; i < 10; i++) {
            if (logs[i] != null) resultado[k++] = logs[i];
        }
        for (int i = 0; i < logIndex; i++) {
            if (logs[i] != null) resultado[k++] = logs[i];
        }

        return resultado;
    }
    
}
