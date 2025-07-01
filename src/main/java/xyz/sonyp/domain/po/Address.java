package xyz.sonyp.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author killLog
 * @since 2025-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("address")
@Schema(description = "Address对象")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "县/区")
    private String town;

    @Schema(description = "手机")
    private String mobile;

    @Schema(description = "详细地址")
    private String street;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "是否是默认 1默认 0否")
    private Boolean isDefault;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "逻辑删除")
    private Boolean deleted;


}
