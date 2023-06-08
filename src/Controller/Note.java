/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Home Tech
 */
public class Note {
    private int id;
    private int appointmentId;
    private int userId;
    private String doctorStatus;
    private String doctorComment;

  

  public Note(String id, String appointmentId, String userId, String doctorStatus, String doctorComment) {
    this.id = Integer.parseInt(id);
    this.appointmentId = Integer.parseInt(appointmentId);
    this.userId = Integer.parseInt(userId);
    this.doctorStatus = doctorStatus;
    this.doctorComment = doctorComment;
}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDoctorStatus() {
        return doctorStatus;
    }

    public void setDoctorStatus(String doctorStatus) {
        this.doctorStatus = doctorStatus;
    }

    public String getDoctorComment() {
        return doctorComment;
    }

    public void setDoctorComment(String doctorComment) {
        this.doctorComment = doctorComment;
    }
    
}

