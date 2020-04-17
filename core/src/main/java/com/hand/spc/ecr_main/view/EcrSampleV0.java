package com.hand.spc.ecr_main.view;

import java.io.Serializable;

public class EcrSampleV0 implements Serializable {
	private String name;
	private String sampleId;
	private String sampleNumber;
	private String meaning;
	private String attachment;
	private Long kid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public String getMeaning() {
		return meaning;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
    public String getAttachment() {
        return attachment;
    }
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    public Long getKid() {
        return kid;
    }
    public void setKid(Long kid) {
        this.kid = kid;
    }
}
