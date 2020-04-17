package com.hand.spc.ecr_main.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HPM_ECR_SOLUTION_MAIN")
public class EcrSolutionMain extends BaseDTO {

     public static final String FIELD_SOLUTION_ID = "solutionId";
     public static final String FIELD_SOLUTION_NUM = "solutionNum";
     public static final String FIELD_ECRNO = "ecrno";
     public static final String FIELD_ISSUE_TYPE = "issueType";
     public static final String FIELD_ISSUE_MAG = "issueMag";
     

     @Id
     @GeneratedValue
     private Long solutionId;

     @Length(max = 80)
     private String solutionNum;

     @Length(max = 80)
     private String ecrno;

     @Length(max = 30)
     private String issueType;

     @Length(max = 2000)
     private String issueMsg;

     
     private String status;
     
     
     
     public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSolutionId(Long solutionId){
         this.solutionId = solutionId;
     }

     public Long getSolutionId(){
         return solutionId;
     }

     public void setSolutionNum(String solutionNum){
         this.solutionNum = solutionNum;
     }

     public String getSolutionNum(){
         return solutionNum;
     }

     public void setEcrno(String ecrno){
         this.ecrno = ecrno;
     }

     public String getEcrno(){
         return ecrno;
     }

     public void setIssueType(String issueType){
         this.issueType = issueType;
     }

     public String getIssueType(){
         return issueType;
     }

     public void setIssueMsg(String issueMsg){
         this.issueMsg = issueMsg;
     }

     public String getIssueMsg(){
         return issueMsg;
     }

     }
