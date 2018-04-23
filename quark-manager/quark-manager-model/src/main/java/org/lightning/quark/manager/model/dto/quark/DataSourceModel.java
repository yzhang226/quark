package org.lightning.quark.manager.model.dto.quark;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by cook on 2018/4/23
 */
@Getter
@Setter
public class DataSourceModel {

    /**     */
    private Integer id;
    /** 数据源类型    */
    private Integer dataSourceType;
    /** 驱动类名  默认值:   */
    private String driverClass;
    /** 连接字符串  默认值:   */
    private String connectString;
    /** 连接用户名    */
    private String userName;
    /** 连接密码  默认值:   */
    private String password;
    /**     */
    private Integer createUser;
    /**   默认值: CURRENT_TIMESTAMP  */
    private LocalDateTime createTime;
    /**     */
    private Integer updateUser;
    /**     */
    private LocalDateTime updateTime;
    /** 连接属性    */
    private ConnectProperties connectProperties;
    /** 连接池属性    */
    private ConnectPoolProperties poolProperties;

}
