package com.example.practike1;

public class Applicant {
    private String fullName;
    private int age;
    private int salary;
    private boolean workExperience;
    private boolean workTeamDevelopmentSkills;
    private boolean willingnessTravel;
    private int countPoint;

    public Applicant(String fullName, int age, int salary, boolean workExperience, boolean workTeamDevelopmentSkills, boolean willingnessTravel) {
        this.fullName = fullName;
        this.age = age;
        this.salary = salary;
        this.countPoint = 0;
        this.workExperience = workExperience;
        this.workTeamDevelopmentSkills = workTeamDevelopmentSkills;
        this.willingnessTravel = willingnessTravel;
        countPoint = ((workExperience ? 1 : 0) + (workTeamDevelopmentSkills ? 1 : 0) + (willingnessTravel ? 1 : 0));
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public int getSalary() {
        return salary;
    }

    public int getCountPoint() {
        return countPoint;
    }

    public void setCountPoint(int countPoint) {
        this.countPoint = countPoint;
    }
}
