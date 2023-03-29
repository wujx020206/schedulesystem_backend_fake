package cn.edu.fc.dao.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Staff {
    private String name;
    private String position;
    private String phone;
    private String email;
    private String shopName;
}
