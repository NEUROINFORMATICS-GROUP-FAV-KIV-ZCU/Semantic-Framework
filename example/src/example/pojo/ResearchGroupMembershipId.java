package example.pojo;
// Generated 16.11.2009 12:22:26 by Hibernate Tools 3.2.1.GA

import thewebsemantic.annotations.Id;

/**
 * ResearchGroupMembershipId generated by hbm2java
 */
public class ResearchGroupMembershipId implements java.io.Serializable {

    @Id
    private int personId;
    private int researchGroupId;

    public ResearchGroupMembershipId() {
    }

    public ResearchGroupMembershipId(int personId, int researchGroupId) {
        this.personId = personId;
        this.researchGroupId = researchGroupId;
    }

    public int getPersonId() {
        return this.personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getResearchGroupId() {
        return this.researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof ResearchGroupMembershipId)) {
            return false;
        }
        ResearchGroupMembershipId castOther = (ResearchGroupMembershipId) other;

        return (this.getPersonId() == castOther.getPersonId()) && (this.getResearchGroupId() == castOther.getResearchGroupId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getPersonId();
        result = 37 * result + this.getResearchGroupId();
        return result;
    }
}


