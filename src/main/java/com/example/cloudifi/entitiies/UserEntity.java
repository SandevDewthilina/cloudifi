package com.example.cloudifi.entitiies;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "user_table")
public class UserEntity {

    public UserEntity() {}

    private String document_id;
    @NonNull
    @PrimaryKey
    private String username;
    private String status;
    private String registered_date;
    private String last_seen;

    public UserEntity(String document_id, @NonNull String username, String status, String registered_date, String last_seen) {
        this.document_id = document_id;
        this.username = username;
        this.status = status;
        this.registered_date = registered_date;
        this.last_seen = last_seen;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRegistered_date(String registered_date) {
        this.registered_date = registered_date;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getDocument_id() {
        return document_id;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getRegistered_date() {
        return registered_date;
    }

    public String getLast_seen() {
        return last_seen;
    }
}
