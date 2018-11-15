package com.kajan.iworkflows.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by User on 11/12/2018.
 */
@Entity
public class GroupMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String iworkflowsRole;
    private String learnorgRole;

    protected GroupMapper() {
    }

    public GroupMapper(String iworkflowsRole, String learnorgRole) {
        this.iworkflowsRole = iworkflowsRole;
        this.learnorgRole = learnorgRole;

    }

    public Long getId() {
        return id;
    }

    public String getIworkflowsRole() {
        return iworkflowsRole;
    }

    public void setIworkflowsRole(String iworkflowsRole) {
        this.iworkflowsRole = iworkflowsRole;
    }

    public String getLearnorgRole() {
        return learnorgRole;
    }

    public void setLearnorgRole(String learnorgRole) {
        this.learnorgRole = learnorgRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMapper that = (GroupMapper) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GroupMapper{" +
                "id=" + id +
                ", iworkflowsRole=" + iworkflowsRole +
                ", learnorgRole=" + learnorgRole +
                '}';
    }
}
