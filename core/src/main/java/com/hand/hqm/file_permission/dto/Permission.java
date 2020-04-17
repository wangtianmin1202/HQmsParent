package com.hand.hqm.file_permission.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "FILE_PERMISSION")
public class Permission extends BaseDTO {

     public static final String FIELD_LAST_UPDATED_DATE = "lastUpdatedDate";
     public static final String FIELD_PERMISSION_ID = "permissionId";
     public static final String FIELD_FILE_ID = "fileId";
     public static final String FIELD_CHARACTER_ID = "characterId";
     public static final String FIELD_USER_ID = "userId";
     public static final String FIELD_ENABLE_FLAG = "enableFlag";


     @Id
     @GeneratedValue
     private Float permissionId;

     private Float fileId;

     private Float characterId;

     private Float userId;

     
     @Length(max = 50)
     private String permissionCode;
  
     @Transient
     private String userName;

     @Transient
     private Float roleUserId;
     
     @Transient
     private Float roleId;
     
     @Transient
     private String roleCode;
     
     @Transient
     private String roleName;
     
     public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Float getRoleUserId() {
		return roleUserId;
	}

	public void setRoleUserId(Float roleUserId) {
		this.roleUserId = roleUserId;
	}

	public Float getRoleId() {
		return roleId;
	}

	public void setRoleId(Float roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

     public void setPermissionId(Float permissionId){
         this.permissionId = permissionId;
     }

     public Float getPermissionId(){
         return permissionId;
     }

     public void setFileId(Float fileId){
         this.fileId = fileId;
     }

     public Float getFileId(){
         return fileId;
     }

     public void setCharacterId(Float characterId){
         this.characterId = characterId;
     }

     public Float getCharacterId(){
         return characterId;
     }

     public void setUserId(Float userId){
         this.userId = userId;
     }

     public Float getUserId(){
         return userId;
     }


     }
