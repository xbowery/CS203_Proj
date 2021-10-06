package com.app.APICode.ctest;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;

import com.app.APICode.user.User;

@Entity
public class Ctest {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    // One test result has only one user linked to it
    // but one user can have multiple tests
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "type of test should not be null")
    @Pattern(regexp = "(PCR|ART)")
    private String type;

    @NotNull(message = "date should not be null")
    private Date date;

    @NotNull(message = "result should not be null")
    @Pattern(regexp = "(Positive|Negative)")
    private String result;

    public Ctest (User user, String type, Date date, String result){
        this.user = user;
        this.type = type;
        this.date = date;
        this.result = result;
    }

}
