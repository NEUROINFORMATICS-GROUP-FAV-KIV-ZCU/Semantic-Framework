package example.pojo;
// Generated 16.11.2009 12:22:26 by Hibernate Tools 3.2.1.GA

import thewebsemantic.annotations.Id;

/**
 * MeasAddParamsValues generated by hbm2java
 */
public class MeasAddParamsValues implements java.io.Serializable {

    @Id
    private MeasAddParamsValuesId id;
    private MeasurationAdditionalParams measurationAdditionalParams;
    private Measuration measuration;
    private String paramValue;

    public MeasAddParamsValues() {
    }

    public MeasAddParamsValues(MeasAddParamsValuesId id, MeasurationAdditionalParams measurationAdditionalParams, Measuration measuration, String paramValue) {
        this.id = id;
        this.measurationAdditionalParams = measurationAdditionalParams;
        this.measuration = measuration;
        this.paramValue = paramValue;
    }

    public MeasAddParamsValuesId getId() {
        return this.id;
    }

    public void setId(MeasAddParamsValuesId id) {
        this.id = id;
    }

    public MeasurationAdditionalParams getMeasurationAdditionalParams() {
        return this.measurationAdditionalParams;
    }

    public void setMeasurationAdditionalParams(MeasurationAdditionalParams measurationAdditionalParams) {
        this.measurationAdditionalParams = measurationAdditionalParams;
    }

    public Measuration getMeasuration() {
        return this.measuration;
    }

    public void setMeasuration(Measuration measuration) {
        this.measuration = measuration;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}


