package example.pojo;
// Generated 16.11.2009 12:22:26 by Hibernate Tools 3.2.1.GA

import thewebsemantic.annotations.Id;

/**
 * ResearchGroupMembership generated by hbm2java
 */
public class ResearchGroupMembership implements java.io.Serializable {

    @Id
    private ResearchGroupMembershipId id;
    private ResearchGroup researchGroup;
    private Person person;
    private String authority;

    public ResearchGroupMembership() {
    }

    public ResearchGroupMembership(ResearchGroupMembershipId id, ResearchGroup researchGroup, Person person, String authority) {
        this.id = id;
        this.researchGroup = researchGroup;
        this.person = person;
        this.authority = authority;
    }

    public ResearchGroupMembershipId getId() {
        return this.id;
    }

    public void setId(ResearchGroupMembershipId id) {
        this.id = id;
    }

    public ResearchGroup getResearchGroup() {
        return this.researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}


